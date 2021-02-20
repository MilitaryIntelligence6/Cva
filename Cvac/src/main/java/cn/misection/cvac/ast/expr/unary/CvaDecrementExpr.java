package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.SelfCreasable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDecrementExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 21:58:00
 */
public class CvaDecrementExpr
        extends AbstractUnaryExpr implements SelfCreasable
{
    public CvaDecrementExpr(int lineNum)
    {
        super(lineNum);
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.DECREMENT;
    }
}
