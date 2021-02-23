package cn.misection.cvac.ast.expr.terminator;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaTrueExpr
 * @Description TODO
 * @CreateTime 2021年02月14日 19:33:00
 */
public final class CvaConstTrueExpr extends AbstractTerminator
{
    public CvaConstTrueExpr(int lineNum)
    {
        super(lineNum);
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.BOOLEAN;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.CONST_TRUE;
    }
}
