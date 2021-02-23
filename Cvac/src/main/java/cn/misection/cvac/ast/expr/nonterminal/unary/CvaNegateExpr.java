package cn.misection.cvac.ast.expr.nonterminal.unary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaNegateExpr
 * @Description TODO
 * @CreateTime 2021年02月14日 19:27:00
 */
public final class CvaNegateExpr extends AbstractUnaryExpr
{
    private AbstractExpression expr;

    public CvaNegateExpr(int lineNum, AbstractExpression expr)
    {
        super(lineNum);
        this.expr = expr;
    }

    @Override
    public EnumCvaType resType()
    {
        return expr.resType();
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.NEGATE;
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
