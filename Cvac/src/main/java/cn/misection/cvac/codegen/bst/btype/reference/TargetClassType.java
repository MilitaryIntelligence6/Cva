package cn.misection.cvac.codegen.bst.btype.reference;

import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:45:00
 */
public final class TargetClassType extends BaseReferenceType
{
    public static final String TYPE_NAME = "@class";

    private final String className;

    public TargetClassType(String className)
    {
        this.className = className;
    }

    /**
     * FIXME 后端这样行不行;
     */
    @Override
    public String toString()
    {
        return String.format("@class:%s", className);
    }

    @Override
    public EnumTargetType toEnum()
    {
        return EnumTargetType.CLASS;
    }

    @Override
    public String typeName()
    {
        return className;
    }
}
