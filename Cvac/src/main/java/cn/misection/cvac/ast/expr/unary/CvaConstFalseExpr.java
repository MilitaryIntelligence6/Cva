package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.unary.AbstractUnaryExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaFalseExpr
 * @Description TODO
 * @CreateTime 2021年02月14日 19:18:00
 */
public final class CvaConstFalseExpr extends AbstractConstExpr
{
    public CvaConstFalseExpr(int lineNum)
    {
        super(lineNum);
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.CVA_BOOLEAN;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.CONST_FALSE;
    }
}
