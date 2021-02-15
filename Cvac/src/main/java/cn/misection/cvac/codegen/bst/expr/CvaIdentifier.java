package cn.misection.cvac.codegen.bst.expr;

import cn.misection.cvac.codegen.bst.btype.BaseType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Identifier
 * @Description TODO
 * @CreateTime 2021年02月14日 19:19:00
 */
public class CvaIdentifier extends AbstractExpression
{
    private String literal;

    private BaseType type;

    /**
     * whether or not a field;
     */
    private boolean fieldFlag;

    public CvaIdentifier(int lineNum, String literal)
    {
        super(lineNum);
        this.literal = literal;
    }

    public CvaIdentifier(int lineNum, String literal, BaseType type, boolean fieldFlag)
    {
        super(lineNum);
        this.literal = literal;
        this.type = type;
        this.fieldFlag = fieldFlag;
    }

    public String getLiteral()
    {
        return literal;
    }

    public BaseType getType()
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

    public void setType(BaseType type)
    {
        this.type = type;
    }

    public void setField(boolean fieldFlag)
    {
        this.fieldFlag = fieldFlag;
    }
}
