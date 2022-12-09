package mysql;

import com.salk.lib.antlr.mysql.lexer.MySqlLexer;
import com.salk.lib.antlr.mysql.listener.MySqlParserContext;
import com.salk.lib.antlr.mysql.listener.MySqlParserListenerExample;
import com.salk.lib.antlr.mysql.parser.MySqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author salkli
 * @since 2022/12/2
 **/
public class AntlrMySqlTest {

    @BeforeAll
    public static void before() {

    }

    @Test
    public void testNestedWhere() {
        String sql =
            "SELECT a.field1,field2,field3 from tableA a where a=6 and b=5 and ((field_2='xx' and field1 = 1) or (field2 like '234' and (field4=4 or field5 <5)))";
        execute(sql);
    }

    @Test
    public void testNormalWhere() {
        String sql = "SELECT field1,field2,field3 from tableA a where a=6 and b=5 or c<4";
        execute(sql);
    }

    @Test
    public void testGroupBy() {
        String sql =
            "SELECT field1,field2,field3 from tableA a where a=6 and b=5 or c<4 group by field1,field2 order by field1 desc,field2 asc";
        execute(sql);
    }

    @Test
    public void testLimitClause() {
        String sql =
            "SELECT field1,field2,field3 from tableA a where a=6 and b=5 or c<4 group by field1,field2 order by field1 desc,field2 asc limit 3,100";
        execute(sql);
    }

    @Test
    public void testjoin() {
        String sql =
            "SELECT t1.column1,t1.column2,t1.column3,t2.xy from tableC t1,tableD t2 inner join tableA t2 on t1.id=t2.oid  where t1.field1 = 1 and field2 like '234'";
        execute(sql);
    }

    @Test
    public void testNestSelect() {
        String sql = "select field1 from (select field1 from table1) as a";
        execute(sql);
    }

    public void execute(String sql) {
        // 词法分析器
        MySqlLexer mySqlLexer = new MySqlLexer(CharStreams.fromString(sql.toUpperCase()));
        // 词法符号的缓冲区,用于存储词法分析器生成的词法符号
        CommonTokenStream commonTokenStream = new CommonTokenStream(mySqlLexer);
        // 新建一个语法分析器，处理词法符号缓冲区内容
        MySqlParser mySqlParser = new MySqlParser(commonTokenStream);
        // 获取出selectStatement
        // ParseTree tree = mySqlParser.selectStatement();
        // System.out.println(printSyntaxTree(mySqlParser,tree));
        getValueByQmlListener(mySqlParser);
    }

    public static void getValueByQmlListener(MySqlParser parser) {
        MySqlParserContext qmlParserContext = new MySqlParserContext();
        MySqlParserListenerExample qmlParserListenerExample = new MySqlParserListenerExample(qmlParserContext);
        ParseTreeWalker.DEFAULT.walk(qmlParserListenerExample, parser.sqlStatements());
        System.out.println(qmlParserContext.toSql());
    }

}
