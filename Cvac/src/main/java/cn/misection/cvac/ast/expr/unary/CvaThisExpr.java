package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.unary.AbstractUnaryExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaThisExpr
 * @Description TODO
 * @CreateTime 2021年02月14日 19:31:00
 */
public final class CvaThisExpr extends AbstractUnaryExpr
{
    public CvaThisExpr(int lineNum)
    {
        super(lineNum);
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.CVA_CLASS;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.THIS;
    }
}
