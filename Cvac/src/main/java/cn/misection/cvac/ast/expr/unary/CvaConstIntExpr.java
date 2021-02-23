package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaNumberInt
 * @Description TODO
 * @CreateTime 2021年02月14日 19:28:00
 */
public final class CvaConstIntExpr extends AbstractConstExpr
{
    private int value;

    public CvaConstIntExpr(int lineNum, int value)
    {
        super(lineNum);
        this.value = value;
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.INT;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.CONST_INT;
    }

    public int getValue()
    {
        return value;
    }
}
