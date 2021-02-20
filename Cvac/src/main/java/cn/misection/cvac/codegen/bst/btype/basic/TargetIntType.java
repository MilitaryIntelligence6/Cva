package cn.misection.cvac.codegen.bst.btype.basic;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaInt
 * @Description TODO
 * @CreateTime 2021年02月14日 19:44:00
 */
public final class TargetIntType extends BaseBasicType
{
    public static final String TYPE_LITERAL = "@int";

    /**
     * 后端没有boolean, 都是int;
     */
    public TargetIntType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }

    @Override
    public String instruction()
    {
        return "I";
    }
}
