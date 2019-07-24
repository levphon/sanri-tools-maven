package com.sanri.app.sqlparser;

import com.sanri.app.sqlparser.items.SqlSelectItem;
import com.sanri.app.sqlparser.items.SqlStatement;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;

public class SqlParserUtil {

    public static SqlStatement parser(String sql) throws JSQLParserException {
        SqlStatement statementParser = new SqlStatement(sql);
        statementParser.parser();
        return statementParser;
    }

    /**
     * 获取顶层查询所有查询列
     * @param sqlStatement
     * @return
     */
    public static List<SqlSelectItem> topQuerySelectItems(SqlStatement sqlStatement){
        return sqlStatement.getSelectStatement().getSqlSelectBody().getSqlPlainSelects().get(0).getSqlSelectItems();
    }
    /**
     * 只支持 select 查询
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public static List<String> allTableNames(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        TablesNamesFinder tablesNamesFinder  = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList((Select) statement);
        return  tableList;
    }

    public static void main(String[] args) throws JSQLParserException {
//        SqlStatement sqlStatement = parser("select a,b,c from table_student s " +
//                "inner join table_people p on s.idcard = p.idcard and p.status = 0 " +
//                "inner join table_other o on o.idcard = s.idcard or o.mm = s.mm " +
//                "where a =1");
//        System.out.println(sqlStatement);


    }
}
