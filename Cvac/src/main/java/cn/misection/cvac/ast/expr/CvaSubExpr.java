package cn.misection.cvac.ast.expr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaSubExpr
 * @Description TODO
 * @CreateTime 2021年02月14日 19:30:00
 */
public class CvaSubExpr extends AbstractExpression
{
    private AbstractExpression left;

    private AbstractExpression right;

    public CvaSubExpr(int lineNum, AbstractExpression left, AbstractExpression right)
    {
        super(lineNum);
        this.left = left;
        this.right = right;
    }

    public AbstractExpression getLeft()
    {
        return left;
    }

    public AbstractExpression getRight()
    {
        return right;
    }

    public void setLeft(AbstractExpression left)
    {
        this.left = left;
    }

    public void setRight(AbstractExpression right)
    {
        this.right = right;
    }
}
