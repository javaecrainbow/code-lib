package com.salk.lib.tool.antrl.g4.listener;

import java.util.ArrayList;
import java.util.List;

import com.salk.lib.tool.antrl.g4.MySqlParser;
import com.salk.lib.tool.antrl.g4.MySqlParserBaseListener;

/**
 * @author salkli
 * @since 2022/11/25
 **/
public class WhereClauseListener extends MySqlParserBaseListener {
    private List<String> tableNames = new ArrayList();

    @Override
    public void enterPredicateExpression(MySqlParser.PredicateExpressionContext ctx) {
        List<MySqlParser.BinaryComparisonPredicateContext> ruleContexts = ctx.getRuleContexts(MySqlParser.BinaryComparisonPredicateContext.class);

        super.enterPredicateExpression(ctx);
    }

    public List<String> getTableNames() {
        return tableNames;
    }
}
