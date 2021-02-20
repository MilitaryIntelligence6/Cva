package cn.misection.cvac.codegen.bst.btype.basic;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName GenVoid
 * @Description TODO
 * @CreateTime 2021年02月16日 20:13:00
 */
public final class TargetVoidType extends BaseBasicType
{
    public static final String TYPE_LITERAL = "@void";

    public TargetVoidType() {}

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
