package cn.misection.cvac.ast.type.advance;

import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaPointer
 * @Description TODO
 * @CreateTime 2021年02月16日 16:15:00
 */
public final class CvaPointerType extends AbstractUnsafe
{
    public static final String TYPE_LITERAL = "@pointer";

    private static final EnumCvaType ENUM_TYPE = EnumCvaType.CVA_POINTER;

    public CvaPointerType() {}

    @Override
    public EnumCvaType toEnum()
    {
        return ENUM_TYPE;
    }

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }
}
