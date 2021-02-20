package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.unary.AbstractUnaryExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaStringExpr
 * @Description TODO
 * @CreateTime 2021年02月18日 14:54:00
 */
public final class CvaConstStringExpr extends AbstractConstExpr
{
    private String literal;

    public CvaConstStringExpr(int lineNum, String literal)
    {
        super(lineNum);
        this.literal = literal;
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.CVA_STRING;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.CONST_STRING;
    }

    public String getLiteral()
    {
        return literal;
    }

    public void setLiteral(String literal)
    {
        this.literal = literal;
    }
}
