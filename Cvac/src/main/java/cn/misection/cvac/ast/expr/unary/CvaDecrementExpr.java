package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDecrementExpr
 * @Description TODO
 * @CreateTime 2021年02月21日 12:44:00
 */
public class CvaDecrementExpr extends AbstractUnaryExpr
{
    protected CvaDecrementExpr(int lineNum)
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
        return null;
    }
}
