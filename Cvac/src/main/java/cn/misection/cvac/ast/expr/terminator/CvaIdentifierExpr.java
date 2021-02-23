package cn.misection.cvac.ast.expr.terminator;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.ICvaType;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Identifier
 * @Description TODO
 * @CreateTime 2021年02月14日 19:19:00
 */
public final class CvaIdentifierExpr extends AbstractTerminator
{
    private String literal;

    private ICvaType type;

    /**
     * whether or not a field;
     */
    private boolean fieldFlag;

    public CvaIdentifierExpr(int lineNum, String literal)
    {
        super(lineNum);
        this.literal = literal;
    }

    public CvaIdentifierExpr(int lineNum,
                             String literal,
                             ICvaType type,
                             boolean fieldFlag)
    {
        super(lineNum);
        this.literal = literal;
        this.type = type;
        this.fieldFlag = fieldFlag;
    }

    @Override
    public EnumCvaType resType()
    {
        return type.toEnum();
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.IDENTIFIER;
    }

    public String literal()
    {
        return literal;
    }

    public ICvaType getType()
    {
        return type;
    }

    public boolean isField()
    {
        return fieldFlag;
    }

    public void setLiteral(String literal)
    {
        this.literal = literal;
    }

    public void setType(ICvaType type)
    {
        this.type = type;
    }

    public void setField(boolean fieldFlag)
    {
        this.fieldFlag = fieldFlag;
    }
}
