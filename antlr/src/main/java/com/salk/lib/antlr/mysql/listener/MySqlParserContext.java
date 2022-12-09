package com.salk.lib.antlr.mysql.listener;

import java.util.*;

import com.google.common.base.Joiner;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.*;
import lombok.Data;

/**
 * @author salkli
 * @since 2022/11/28
 **/
@Data
public class MySqlParserContext {
    private static SqlLog sqlLog = SqlLog.INSTANCE;

    private Map<String, String> tableAs = new HashMap();
    private List<String> fromClause = new ArrayList();

    private List<Condition> conditionList = new ArrayList();
    private Map<String, String> tableAlias = new HashMap();
    private List<String> columns = new ArrayList();

    private List<Order> orders = new ArrayList();

    private List<String> groupFields = new ArrayList();
    /**
     * 通过栈控制层级条件组
     */
    private Deque<ConditionGroup> croups = new ArrayDeque();

    public void addGroup(String field) {
        groupFields.add(field);
    }

    public void appendGroup() {
        this.getSqlBuilder().groupBy(groupFields.toArray(new String[0]));
        groupFields.clear();

    }

    public void addColumn(String column) {
        columns.add(column);
    }

    public void addOrders(String field, String direction) {
        Order order = new Order(field, Direction.valueOf(direction));
        orders.add(order);
        columns.clear();
    }

    public void appendOrders() {
        this.getSqlBuilder().orderBy(orders.toArray(new Order[0]));
    }

    public void addFrom(String table) {
        fromClause.add(table);
    }

    public void addTableAs(String alias, String tableId) {
        tableAs.put(alias, tableId);
    }

    public void addCondition(Condition condition) {
        ConditionGroup group = this.peakGroup();
        if (group != null) {
            group.addConditions(condition);
        } else {
            conditionList.add(condition);
        }
    }

    public void addCondition(String field, String symbol, Object value) {
        Condition condition = new Condition(field, symbol, value);
        if (symbol != null) {
            condition.setLinkOperator(LogicalOperator.valueOf(symbol));
        }
        addCondition(condition);
    }

    public void addGroup() {
        ConditionGroup conditionGroup = this.poolGroup();
        ConditionGroup last = this.peakGroup();
        if (last != null) {
            // 放到前一个group里面
            last.addConditions(conditionGroup);
        } else {
            addCondition(conditionGroup);
        }

    }

    public void createGroup() {
        ConditionGroup conditionGroup = new ConditionGroup();
        croups.push(conditionGroup);
    }

    public ConditionGroup poolGroup() {
        return croups.poll();
    }

    public ConditionGroup peakGroup() {
        return croups.peek();
    }

    private SqlBuilder sqlBuilder;

    public MySqlParserContext() {
        sqlBuilder = SqlBuilder.create();
    }

    public String toSql() {
        return StrUtil.format("\n[SQL] -> {}\nParams -> {}\nParamMap:{}\nPlaceholderMap:{}", sqlBuilder.toString(),
            sqlBuilder.getParamValues());
    }

    public void appendFrom() {
        this.sqlBuilder.from(Joiner.on(",").join(fromClause));
        // this.sqlBuilder.where(conditionList.toArray(new Condition[0]));

    }

    public void appendLimit(Integer start, Integer end) {
        sqlBuilder.append(" limit ").append(start).append(",").append(end);
    }

}
