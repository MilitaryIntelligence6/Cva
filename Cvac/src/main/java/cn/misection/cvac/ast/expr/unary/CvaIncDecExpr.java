package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaIncrementExpr
 * @Description TODO
 * @CreateTime 2021年02月21日 12:44:00
 */
public final class CvaIncDecExpr extends AbstractUnaryExpr
{
    private String literal;

    public CvaIncDecExpr(int lineNum)
    {
        super(lineNum);
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.CVA_INT;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.INCREMENT;
    }
}
