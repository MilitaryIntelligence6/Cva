package cn.misection.cvac.ast.expr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaNegateExpr
 * @Description TODO
 * @CreateTime 2021年02月14日 19:27:00
 */
public class CvaNegateExpr extends AbstractExpression
{
    private AbstractExpression expr;

    public CvaNegateExpr(int lineNum, AbstractExpression expr)
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
