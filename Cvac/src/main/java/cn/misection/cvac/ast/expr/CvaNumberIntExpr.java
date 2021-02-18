package cn.misection.cvac.ast.expr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaNumberInt
 * @Description TODO
 * @CreateTime 2021年02月14日 19:28:00
 */
public final class CvaNumberIntExpr extends AbstractExpression
{
    private int value;

    public CvaNumberIntExpr(int lineNum, int value)
    {
        super(lineNum);
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
