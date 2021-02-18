package cn.misection.cvac.codegen.bst.btype.basic;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaInt
 * @Description TODO
 * @CreateTime 2021年02月14日 19:44:00
 */
public final class GenIntType extends BaseBasicType
{
    /**
     * 后端没有boolean, 都是int;
     */
    public GenIntType() {}

    @Override
    public String toString()
    {
        return "@int";
    }

    @Override
    public String instruction()
    {
        return "I";
    }
}
