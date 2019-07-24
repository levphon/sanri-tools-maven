package com.sanri.app.sqlparser.items;

import com.sanri.app.sqlparser.ParserItem;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import sanri.utils.PropertyEditUtil;

public class SqlJoin implements ParserItem {
    private Join join;

    private boolean outer ;
    private boolean right ;
    private boolean left ;
    private boolean natural;
    private boolean full ;
    private boolean inner ;
    private boolean simple;;
    private boolean cross ;

    private SqlFromItem sqlFromItem;

    public SqlJoin(Join join) {
        this.join = join;
    }

    public void parser() {
        //复制所有 bool 值选项
        PropertyEditUtil.copyInclude(this,join,"outer","right","left","natural","full","inner","simple","cross");
        Expression onExpression = join.getOnExpression();

        FromItem rightItem = join.getRightItem();
        sqlFromItem = new SqlFromItem(rightItem);
        sqlFromItem.parser();

    }

    public boolean isOuter() {
        return outer;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isNatural() {
        return natural;
    }

    public boolean isFull() {
        return full;
    }

    public boolean isInner() {
        return inner;
    }

    public boolean isSimple() {
        return simple;
    }

    public boolean isCross() {
        return cross;
    }

    public SqlFromItem getSqlFromItem() {
        return sqlFromItem;
    }
}
