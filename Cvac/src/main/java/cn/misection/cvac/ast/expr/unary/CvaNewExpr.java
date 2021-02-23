package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaNew
 * @Description TODO
 * @CreateTime 2021年02月14日 19:24:00
 */
public final class CvaNewExpr extends AbstractUnaryExpr
{
    private String newClassName;

    public CvaNewExpr(int lineNum, String newClassName)
    {
        super(lineNum);
        this.newClassName = newClassName;
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.CLASS;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.NEW;
    }

    public String getNewClassName()
    {
        return newClassName;
    }
}
