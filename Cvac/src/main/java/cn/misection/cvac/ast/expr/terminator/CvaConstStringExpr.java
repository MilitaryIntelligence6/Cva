package cn.misection.cvac.ast.expr.terminator;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaStringExpr
 * @Description TODO
 * @CreateTime 2021年02月18日 14:54:00
 */
public final class CvaConstStringExpr extends AbstractTerminator
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
        return EnumCvaType.STRING;
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
