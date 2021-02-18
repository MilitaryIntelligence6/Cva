package cn.misection.cvac.codegen.bst.binstruct;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName New
 * @Description TODO
 * @CreateTime 2021年02月16日 01:00:00
 */
public final class New extends BaseInstruction
{
    private String clazz;

    public New(String clazz)
    {
        this.clazz = clazz;
    }

    public String getClazz()
    {
        return clazz;
    }

    public void setClazz(String clazz)
    {
        this.clazz = clazz;
    }
}
