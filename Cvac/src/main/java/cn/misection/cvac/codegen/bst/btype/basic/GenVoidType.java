package cn.misection.cvac.codegen.bst.btype.basic;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName GenVoid
 * @Description TODO
 * @CreateTime 2021年02月16日 20:13:00
 */
public final class GenVoidType extends BaseBasicType
{
    public GenVoidType() {}

    @Override
    public String toString()
    {
        return "@void";
    }

    @Override
    public String instruction()
    {
        return null;
    }
}
