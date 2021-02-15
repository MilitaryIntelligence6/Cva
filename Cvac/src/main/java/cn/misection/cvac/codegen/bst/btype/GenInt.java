package cn.misection.cvac.codegen.bst.btype;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaInt
 * @Description TODO
 * @CreateTime 2021年02月14日 19:44:00
 */
public class GenInt extends BaseType
{
    /**
     * 后端没有boolean, 都是int;
     */
    public GenInt()
    {
    }

    @Override
    public String toString()
    {
        return "@int";
    }
}
