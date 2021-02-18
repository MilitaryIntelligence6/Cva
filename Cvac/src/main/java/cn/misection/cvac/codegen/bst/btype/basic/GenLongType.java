package cn.misection.cvac.codegen.bst.btype.basic;

import cn.misection.cvac.codegen.bst.btype.BaseType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaLong
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public final class GenLongType extends BaseBasicType
{
    public static final String TYPE_LITERAL = "@long";

    public GenLongType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }

    @Override
    public String instruction()
    {
        return null;
    }
}
