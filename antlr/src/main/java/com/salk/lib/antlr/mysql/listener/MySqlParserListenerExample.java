package com.salk.lib.antlr.mysql.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.salk.lib.antlr.mysql.parser.MySqlParser;

import cn.hutool.db.sql.SqlBuilder;

/**
 * qml解析器侦听器
 *
 * @author salkli
 * @date 2022/12/02
 */
public class MySqlParserListenerExample extends MySqlParserBaseListener {

    /**
     * qml解析器上下文
     */
    private MySqlParserContext mySqlParserContext;

    /**
     * qml解析器侦听器
     *
     * @param mySqlParserContext
     *            qml解析器上下文
     */
    public MySqlParserListenerExample(MySqlParserContext mySqlParserContext) {
        this.mySqlParserContext = mySqlParserContext;
    }

    /**
     * 进入选择列元素
     *
     * @param ctx
     *
     */
    @Override
    public void enterSelectColumnElement(MySqlParser.SelectColumnElementContext ctx) {
        List<ParseTree> children = ctx.children;
        MySqlParser.FullColumnNameContext fullColumnNameContext = (MySqlParser.FullColumnNameContext)children.get(0);
        String fieldId = getFieldId(fullColumnNameContext);
        mySqlParserContext.addColumn(fieldId);
    }

    /**
     * 退出选择元素
     *
     * @param ctx
     *            ctx
     */
    @Override
    public void exitSelectElements(MySqlParser.SelectElementsContext ctx) {
        mySqlParserContext.getSqlBuilder().select(mySqlParserContext.getColumns());
    }

    /**
     * 输入表源基地
     *
     * @param ctx
     *            ctx
     */
    @Override
    public void enterTableSourceBase(MySqlParser.TableSourceBaseContext ctx) {
        int childCount = ctx.getRuleContext().getChildCount();
        for (int i = 0; i < childCount; i++) {
            String alisa = "";
            ParseTree child = ctx.getRuleContext().getChild(i);
            if (child instanceof MySqlParser.AtomTableItemContext) {
                String tableId =
                    ((MySqlParser.AtomTableItemContext)child).tableName().fullId().uid(0).simpleId().ID().getText();
                MySqlParser.UidContext uid = ((MySqlParser.AtomTableItemContext)child).uid();
                if (uid != null) {
                    alisa = uid.simpleId().ID().getText();
                } else {
                    alisa = tableId;
                }
                mySqlParserContext.addFrom(tableId + " " + alisa);
                mySqlParserContext.addTableAs(alisa, tableId);

            } else if (child instanceof MySqlParser.JoinPartContext) {
                List<ParseTree> children = ((MySqlParser.JoinPartContext)child).children;
                SqlBuilder.Join join = SqlBuilder.Join.INNER;
                String tableId = "";
                for (ParseTree parseTree : children) {
                    if (parseTree instanceof TerminalNode) {
                        String text = parseTree.getText();
                        if ("INNER".equals(text)) {
                            join = SqlBuilder.Join.INNER;
                        } else if ("LEFT".equals(text)) {
                            join = SqlBuilder.Join.LEFT;
                        }
                    } else if (parseTree instanceof MySqlParser.AtomTableItemContext) {
                        // 提取atomtableItem visitor
                        // 获取表名
                        tableId = ((MySqlParser.AtomTableItemContext)parseTree).tableName().fullId().uid(0).simpleId()
                            .ID().getText();
                        MySqlParser.UidContext uid = ((MySqlParser.AtomTableItemContext)parseTree).uid();
                        if (uid != null) {
                            alisa = uid.simpleId().ID().getText();
                        } else {
                            alisa = tableId;
                        }
                        mySqlParserContext.addTableAs(alisa, tableId);
                    }
                }
                mySqlParserContext.getSqlBuilder().join(tableId + " " + alisa, join);
            }
        }
    }

    @Override
    public void enterSubqueryTableItem(MySqlParser.SubqueryTableItemContext ctx) {
        super.enterSubqueryTableItem(ctx);
    }

    @Override
    public void exitSubqueryTableItem(MySqlParser.SubqueryTableItemContext ctx) {
        super.enterSubqueryTableItem(ctx);
    }

    /**
     * 退出逻辑表达式 处理逻辑表达式
     *
     * @param ctx
     *            ctx
     */
    @Override
    public void exitLogicalExpression(MySqlParser.LogicalExpressionContext ctx) {
        List<ParseTree> children = ctx.children;
        MySqlParser.LogicalOperatorContext logicalOperatorContext = null;
        for (ParseTree child : children) {
            if (child instanceof MySqlParser.LogicalExpressionContext) {
                super.exitLogicalExpression(ctx);
            } else if (child instanceof MySqlParser.LogicalOperatorContext) {
                logicalOperatorContext = (MySqlParser.LogicalOperatorContext)child;
            } else if (child instanceof MySqlParser.PredicateExpressionContext) {
                // a=b or c=d
                executeComparison(logicalOperatorContext,
                    ((MySqlParser.PredicateExpressionContext)child).getChild(MySqlParser.PredicateContext.class, 0));
            }

        }
    }

    @Override
    public void enterPredicateExpression(MySqlParser.PredicateExpressionContext ctx) {
        executeComparison(null, (ctx).getChild(MySqlParser.PredicateContext.class, 0));
    }

    @Override
    public void exitFromClause(MySqlParser.FromClauseContext ctx) {
        this.mySqlParserContext.appendFrom();

    }

    /**
     * 输入嵌套表达式原子
     *
     * @param ctx
     *            ctx
     */
    @Override
    public void enterNestedExpressionAtom(MySqlParser.NestedExpressionAtomContext ctx) {
        this.mySqlParserContext.createGroup();
    }

    /**
     * 退出嵌套表达式原子
     *
     * @param ctx
     *            ctx
     */
    @Override
    public void exitNestedExpressionAtom(MySqlParser.NestedExpressionAtomContext ctx) {
        this.mySqlParserContext.addGroup();
    }

    /**
     * 执行比较
     *
     * @param logicalOperator
     *            逻辑运算符
     * @param predicateContext
     *            谓词上下文
     */
    private void executeComparison(MySqlParser.LogicalOperatorContext logicalOperator,
        MySqlParser.PredicateContext predicateContext) {
        if (predicateContext instanceof MySqlParser.BinaryComparisonPredicateContext) {
            // parse left
            MySqlParser.PredicateContext left = ((MySqlParser.BinaryComparisonPredicateContext)predicateContext).left;
            MySqlParser.FullColumnNameContext fullColumnNameContext =
                ((MySqlParser.FullColumnNameExpressionAtomContext)left.children.get(0)).fullColumnName();
            String field = getFieldId(fullColumnNameContext);
            MySqlParser.PredicateContext right = ((MySqlParser.BinaryComparisonPredicateContext)predicateContext).right;
            ParseTree child = right.children.get(0).getChild(0);
            addCondition(child, field, "=", logicalOperator);
        } else if (predicateContext instanceof MySqlParser.LikePredicateContext) {
            List<ParseTree> children1 = predicateContext.children;
            MySqlParser.FullColumnNameContext fullColumnNameContext1 =
                ((MySqlParser.FullColumnNameExpressionAtomContext)children1.get(0).getChild(0)).fullColumnName();
            String field = getFieldId(fullColumnNameContext1);
            String symbol = ((TerminalNode)children1.get(1)).getSymbol().getText();
            int paramIndex = 2;
            if (!"LIKE".equalsIgnoreCase(symbol) && "NOT".equalsIgnoreCase(symbol)) {
                symbol = "NOT LIKE";
                paramIndex = 3;
            }
            ParseTree child = children1.get(paramIndex).getChild(0).getChild(0);
            addCondition(child, field, symbol, logicalOperator);
        } else if (predicateContext instanceof MySqlParser.InPredicateContext) {
            List<ParseTree> childrenList = predicateContext.children;
            MySqlParser.FullColumnNameContext fullColumnNameContext =
                ((MySqlParser.FullColumnNameExpressionAtomContext)childrenList.get(0).getChild(0)).fullColumnName();
            String field = getFieldId(fullColumnNameContext);
            String symbol = ((TerminalNode)childrenList.get(1)).getSymbol().getText();
            int paramIndex = 3;
            if (!"IN".equalsIgnoreCase(symbol) && "NOT".equalsIgnoreCase(symbol)) {
                symbol = "NOT IN";
                paramIndex = 4;
            }
            List<String> value = ((MySqlParser.ExpressionsContext)childrenList.get(paramIndex)).children.stream()
                .filter(item -> item instanceof MySqlParser.PredicateExpressionContext).map(ParseTree::getText)
                .collect(Collectors.toList());
            mySqlParserContext.addCondition(field, symbol, value);
        } else if (predicateContext instanceof MySqlParser.IsNullPredicateContext) {
            List<ParseTree> children = predicateContext.children;
            MySqlParser.FullColumnNameContext fullColumnNameContext =
                ((MySqlParser.FullColumnNameExpressionAtomContext)children.get(0).getChild(0)).fullColumnName();
            String field = getFieldId(fullColumnNameContext);
            String symbol = ((MySqlParser.IsNullPredicateContext)predicateContext).IS().getText();

            String value = ((MySqlParser.NullNotnullContext)children.get(2)).children.stream().map(ParseTree::getText)
                .collect(Collectors.joining(" "));

            mySqlParserContext.addCondition(field, symbol, value);
        }

    }

    /**
     * 添加条件
     *
     * @param child
     *            孩子
     * @param field
     *            场
     * @param symbol
     *            象征
     * @param logicalOperator
     *            逻辑运算符
     */
    private void addCondition(ParseTree child, String field, String symbol,
        MySqlParser.LogicalOperatorContext logicalOperator) {
        Object value = new Object();
        boolean isPlaceholder = Boolean.TRUE;
        if (child instanceof MySqlParser.ScalarFunctionCallContext
            || child instanceof MySqlParser.FullColumnNameContext) {
            value = child.getText();
            isPlaceholder = Boolean.FALSE;
        } else if (child instanceof MySqlParser.ConstantContext) {
            ParseTree childChild = child.getChild(0);
            if (childChild instanceof MySqlParser.DecimalLiteralContext) {
                String valueStr = childChild.getChild(0).getText();
                value = Long.parseLong(valueStr);
            } else if (childChild instanceof MySqlParser.StringLiteralContext) {
                value = getOriginStr(childChild.getText());
            }
        }
        mySqlParserContext.addCondition(field, symbol, value);
    }

    private String getOriginStr(String text) {
        return text.substring(1, text.length() - 1);
    }

    @Override
    public void enterOrderByExpression(MySqlParser.OrderByExpressionContext ctx) {
        List<ParseTree> children = ctx.children;
        String field = "";
        String direction = "";
        for (ParseTree parseTree : children) {
            if (parseTree instanceof MySqlParser.PredicateExpressionContext) {
                MySqlParser.PredicateContext child =
                    ((MySqlParser.PredicateExpressionContext)parseTree).getChild(MySqlParser.PredicateContext.class, 0);
                MySqlParser.FullColumnNameContext fullColumnNameContext =
                    ((MySqlParser.FullColumnNameExpressionAtomContext)child.children.get(0)).fullColumnName();
                field = getFieldId(fullColumnNameContext);
            } else if (parseTree instanceof TerminalNode) {
                direction = parseTree.getText();
            }

        }
        this.mySqlParserContext.addOrders(field, direction);
    }

    @Override
    public void exitOrderByClause(MySqlParser.OrderByClauseContext ctx) {
        this.mySqlParserContext.appendOrders();
    }

    @Override
    public void enterGroupByItem(MySqlParser.GroupByItemContext ctx) {
        ParseTree child = ctx.getChild(MySqlParser.PredicateExpressionContext.class, 0);
        MySqlParser.FullColumnNameContext fullColumnNameContext1 =
            ((MySqlParser.FullColumnNameExpressionAtomContext)child.getChild(0).getChild(0)).fullColumnName();
        String field = getFieldId(fullColumnNameContext1);
        this.mySqlParserContext.addGroup(field);
    }

    @Override
    public void exitGroupByClause(MySqlParser.GroupByClauseContext ctx) {
        this.mySqlParserContext.appendGroup();
    }

    @Override
    public void enterLimitClause(MySqlParser.LimitClauseContext ctx) {
        List<ParseTree> children = ctx.children;
        // 0:limit 1: ${start} 2:COMMA 3:${end}
        String startStr = ((MySqlParser.LimitClauseAtomContext)children.get(1)).decimalLiteral().getChild(0).getText();
        int start = Integer.parseInt(startStr);
        String endStr = ((MySqlParser.LimitClauseAtomContext)children.get(3)).decimalLiteral().getChild(0).getText();
        int end = Integer.parseInt(endStr);
        this.mySqlParserContext.appendLimit(start, end);

    }

    /**
     * 获取字段id
     *
     * @param fullColumnNameContext1
     *            完整列名context1
     * @return {@link String}
     */
    private String getFieldId(MySqlParser.FullColumnNameContext fullColumnNameContext1) {
        String fieldId = fullColumnNameContext1.uid().simpleId().ID().getText();
        if (fullColumnNameContext1.dottedId().size() > 0) {
            fieldId = fieldId.concat(fullColumnNameContext1.dottedId(0).DOT_ID().getText());
        }
        return fieldId;
    }

}
