package learntest;

import com.alibaba.fastjson.JSONObject;
import com.sanri.app.sqlparser.SqlParserUtil;
import com.sanri.app.sqlparser.items.SqlSelectItem;
import com.sanri.app.sqlparser.items.SqlStatement;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.Node;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SqlParserTest {

    @Test
    public void testSqlParser() throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse("select * from t_bo_dict; ");
        Select select = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(select);
        System.out.println(tableList);
    }



    @Test
    public void testComplexParse() throws IOException, JSQLParserException {
        InputStream resourceAsStream = SqlParserTest.class.getResourceAsStream("/complexsql.sql");
        String sql = IOUtils.toString(resourceAsStream);
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select select = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(select);
        System.out.println(tableList);
    }

    @Test
    public void testStatement() throws IOException, JSQLParserException {
        InputStream resourceAsStream = SqlParserTest.class.getResourceAsStream("/complexsql.sql");
        String sql = IOUtils.toString(resourceAsStream);
        Node node = CCJSqlParserUtil.parseAST(sql);
        System.out.println(node);
    }

    @Test
   public void testAllColumns() throws IOException , JSQLParserException{
        InputStream resourceAsStream = SqlParserTest.class.getResourceAsStream("/complexsql.sql");
        String sql = IOUtils.toString(resourceAsStream);
        Select select = (Select)CCJSqlParserUtil.parse(sql);
        SetOperationList setOperationList = (SetOperationList) select.getSelectBody();
        List<SelectBody> selects = setOperationList.getSelects();
        for (SelectBody selectBody : selects) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            for (SelectItem selectItem : selectItems) {
//                System.out.println(selectItem.getClass());
                SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
                Expression expression = selectExpressionItem.getExpression();
                Class<? extends Expression> clazz = expression.getClass();
               if(clazz == Function.class){
                   Function function = (Function) expression;
                   ExpressionList parameters = function.getParameters();
                   List<Expression> expressions = parameters.getExpressions();
               }else if(clazz == CaseExpression.class){
                   CaseExpression caseExpression = (CaseExpression) expression;

               }
                System.out.println(selectExpressionItem.getAlias().getName());
            }
        }
    }

    @Test
    public void testSqlParserMy() throws JSQLParserException, IOException {
        InputStream resourceAsStream = SqlParserTest.class.getResourceAsStream("/complexsql.sql");
        String sql = IOUtils.toString(resourceAsStream);
        SqlStatement sqlStatement = SqlParserUtil.parser(sql);
//        System.out.println(sqlStatement);
//        List<SqlSelectItem> sqlSelectItems = SqlParserUtil.topQuerySelectItems(sqlStatement);
//        System.out.println(sqlSelectItems);
        System.out.println(JSONObject.toJSONString(sqlStatement));
    }

}
