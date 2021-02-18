package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.codegen.bst.btype.BaseType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaWriteStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 18:43:00
 */
public final class CvaWriteStatement extends AbstractStatement
{
    public static final byte WRITE = 0;

    public static final byte WRITELN = 1;

    public static final byte WRITE_FORMAT = 2;

    private byte writelnMode;

    private BaseType writeType;

    private AbstractExpression expr;

    public CvaWriteStatement(int lineNum, AbstractExpression expr)
    {
        super(lineNum);
        this.expr = expr;
    }



    public AbstractExpression getExpr()
    {
        return expr;
    }

    public void setExpr(AbstractExpression expr)
    {
        this.expr = expr;
    }
}
