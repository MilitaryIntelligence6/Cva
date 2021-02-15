package cn.misection.cvac.codegen.bst.btype;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaType
 * @Description TODO
 * @CreateTime 2021年02月14日 17:52:00
 */
public abstract class BaseType implements IType
{
    public BaseType()
    {
    }

    /**
     * 转string;
     * @return
     */
    @Override
    public abstract String toString();
}
