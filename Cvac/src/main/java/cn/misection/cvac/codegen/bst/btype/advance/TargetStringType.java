package cn.misection.cvac.codegen.bst.btype.advance;

import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaString
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public final class TargetStringType extends BaseAdvanceType
{
    public static final String TYPE_LITERAL = "@string";

    public static final String FULL_LITERAL = "java/lang/String";

    public TargetStringType() {}

    @Override
    public String literal()
    {
        return FULL_LITERAL;
    }

    @Override
    public String toInst()
    {
        return FULL_LITERAL;
    }

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }

    @Override
    public EnumTargetType toEnum()
    {
        return EnumTargetType.TARGET_STRING;
    }
}
