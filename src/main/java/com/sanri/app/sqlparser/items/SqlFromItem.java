package com.sanri.app.sqlparser.items;

import com.sanri.app.sqlparser.ParserItem;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

public class SqlFromItem implements ParserItem {
    private FromItem fromItem;

    private SqlTable sqlTable;
    private SqlSelectBody subSelect;

    public SqlFromItem(FromItem fromItem) {
        this.fromItem = fromItem;
    }

    public void parser() {
        if(fromItem instanceof SubJoin){
            parserSubJoin((SubJoin) fromItem);
        }else if(fromItem instanceof Table){
            parserTable((Table) fromItem);
        }else if(fromItem instanceof SubSelect){
            parserSubSelect((SubSelect) fromItem);
        }else if(fromItem instanceof LateralSubSelect){
            parserLateralSubSelect((LateralSubSelect) fromItem);
        }else if(fromItem instanceof ValuesList){
            parserValuesList((ValuesList) fromItem);
        }else if(fromItem instanceof TableFunction){
            parserTableFunction((TableFunction) fromItem);
        }
    }

    private void parserLateralSubSelect(LateralSubSelect lateralSubSelect) {
        System.out.println("lateralSubSelect 解析未实现  "+lateralSubSelect);
    }

    private void parserSubSelect(SubSelect subSelect) {
        SelectBody selectBody = subSelect.getSelectBody();
        this.subSelect = new SqlSelectBody(selectBody);
        this.subSelect.parser();
    }

    private void parserTable(Table table) {
        sqlTable = new SqlTable(table);
        sqlTable.parser();
    }

    private void parserValuesList(ValuesList valuesList) {
        System.out.println("valuesList 解析未实现  "+valuesList);
    }

    private void parserTableFunction(TableFunction tableFunction ) {
        System.out.println("tableFunction 解析未实现  "+tableFunction );
    }

    private void parserSubJoin(SubJoin subJoin) {
        SqlSubJoin sqlSubJoin = new SqlSubJoin(subJoin);
        sqlSubJoin.parser();
    }
}
