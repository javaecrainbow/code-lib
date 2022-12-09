package mysql;

import cn.hutool.db.sql.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.db.Page;
import cn.hutool.log.level.Level;

/**
 * @author salkli
 * @since 2022/11/29
 **/
public class HutoolDbTest {
    private static SqlLog sqlLog;

    @BeforeAll
    public static void before() {
        System.out.println("before========");
        sqlLog = SqlLog.INSTANCE;
        sqlLog.init(true, false, true, Level.INFO);
    }

    @Test
    public void test1() {
        Wrapper wrapper = new Wrapper('`', '`');
        Condition condition = new Condition("field1", "=", "212");
        condition.setLinkOperator(LogicalOperator.OR);
        Condition condition2 = new Condition(false);
        condition2.setField("field2");
        condition2.setOperator("=");
        condition2.setValue("field3");
        condition.setLinkOperator(LogicalOperator.OR);
        Page page = Page.of(0, 10);
        page.addOrder(new Order("order_field1", Direction.ASC), new Order("order_field1", Direction.DESC));
        Query query = new Query(CollectionUtil.newArrayList("field1", "field2"), new String[] {"table1", "table2"},
            new Condition[] {condition, condition2}, page);
        SqlBuilder sqlBuilder = SqlBuilder.create(wrapper).query(query);
        sqlBuilder.append(" limit ").append(page.getStartPosition()).append(",").append(page.getEndPosition());
        sqlLog.log(sqlBuilder.toString(), sqlBuilder.getParamValues());

    }
    @Test
    public void testJoin() {
        //Wrapper wrapper = new Wrapper('`', '`');
        Condition condition = new Condition("field1", "=", "212");
        condition.setLinkOperator(LogicalOperator.OR);
        Condition condition2 = new Condition(false);
        condition2.setField("field2");
        condition2.setOperator("=");
        condition2.setValue("field3");
        condition.setLinkOperator(LogicalOperator.OR);
        Page page = Page.of(0, 10);
        page.addOrder(new Order("order_field1", Direction.ASC), new Order("order_field1", Direction.DESC));
        Query query = new Query(CollectionUtil.newArrayList("field1", "field2"), new String[] {"table1", "table2"},
                new Condition[] {condition, condition2}, page);
        Condition joinCondition=new Condition(false);
        joinCondition.setField("table1.id");
        joinCondition.setOperator("=");
        joinCondition.setValue("table2.id");
        SqlBuilder sqlBuilder = SqlBuilder.create().query(query).join("table2", SqlBuilder.Join.INNER)
                .on(new Condition[]{joinCondition});
        sqlBuilder.append(" limit ").append(page.getStartPosition()).append(",").append(page.getEndPosition());
        sqlLog.log(sqlBuilder.toString(), sqlBuilder.getParamValues());

    }

    @Test
    public void testMultiCondition() {
        Wrapper wrapper = new Wrapper('`', '`');
        Condition condition = new Condition("field1", "=", "212");
        condition.setLinkOperator(LogicalOperator.OR);
        Condition condition2 = new Condition(false);
        condition2.setField("field2");
        condition2.setOperator("=");
        condition2.setValue("field3");
        condition.setLinkOperator(LogicalOperator.OR);
        Page page = Page.of(1, 10);
        page.addOrder(new Order("order_field1", Direction.ASC), new Order("order_field1", Direction.DESC));
        Query query = new Query(CollectionUtil.newArrayList("field1", "field2"), new String[] {"table1", "table2"},
            new Condition[] {condition, condition2}, page);
        SqlBuilder sqlBuilder = SqlBuilder.create(wrapper).from("table1", "table2").query(query);
        sqlLog.log(sqlBuilder.toString(), sqlBuilder.getParamValues());
    }

}
