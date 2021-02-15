package cn.misection.cvac.codegen.bst.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaType
 * @Description TODO
 * @CreateTime 2021年02月14日 17:52:00
 */
public abstract class AbstractType implements IType
{
    public AbstractType()
    {
    }

    /**
     * 转string;
     * @return
     */
    @Override
    public abstract String toString();
}
