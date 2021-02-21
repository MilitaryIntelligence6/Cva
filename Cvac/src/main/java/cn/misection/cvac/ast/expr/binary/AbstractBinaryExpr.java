package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName BinaryExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:06:00
 */
public abstract class AbstractBinaryExpr extends AbstractExpression
{
    protected AbstractExpression left;

    protected AbstractExpression right;

    protected AbstractBinaryExpr() {}

    public AbstractBinaryExpr(int lineNum,
                              AbstractExpression left,
                              AbstractExpression right)
    {
        super(lineNum);
        this.left = left;
        this.right = right;
    }

    public AbstractExpression getLeft()
    {
        return left;
    }

    public void setLeft(AbstractExpression left)
    {
        this.left = left;
    }

    public AbstractExpression getRight()
    {
        return right;
    }

    public void setRight(AbstractExpression right)
    {
        this.right = right;
    }
}
