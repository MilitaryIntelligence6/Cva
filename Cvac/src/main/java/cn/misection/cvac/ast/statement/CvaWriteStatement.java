package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaWriteStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 18:43:00
 */
public final class CvaWriteStatement extends AbstractStatement
{
    private AbstractExpression expr;

    private byte writeMode;

    /**
     * FIXME ,后面尽量早确定;
     * expr 的type;
     */
//    private AbstractType writeType;

    public CvaWriteStatement(int lineNum, AbstractExpression expr, byte writeMode)
    {
        super(lineNum);
        this.expr = expr;
        this.writeMode = writeMode;
    }

    public AbstractExpression getExpr()
    {
        return expr;
    }

    public void setExpr(AbstractExpression expr)
    {
        this.expr = expr;
    }

    public byte getWriteMode()
    {
        return writeMode;
    }

    public void setWriteMode(byte writeMode)
    {
        this.writeMode = writeMode;
    }
}
