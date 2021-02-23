package cn.misection.cvac.ast.type.reference;

import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:45:00
 */
public final class CvaClassType extends AbstractReferenceType
{
    public static final String TYPE_LITERAL = "@class";

    private static final EnumCvaType ENUM_TYPE = EnumCvaType.CLASS;

    private final String name;

    public CvaClassType(String name)
    {
        this.name = name;
    }

    @Override
    public EnumCvaType toEnum()
    {
        return ENUM_TYPE;
    }

    @Override
    public String toString()
    {
        return String.format("@class:%s", name);
    }

    public String getName()
    {
        return name;
    }
}
