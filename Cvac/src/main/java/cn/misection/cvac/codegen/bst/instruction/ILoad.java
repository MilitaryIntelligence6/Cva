package cn.misection.cvac.codegen.bst.instruction;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName ILoad
 * @Description TODO
 * @CreateTime 2021年02月16日 00:52:00
 */
public final class ILoad extends BaseInstruction
{
    private int index;

    public ILoad(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }
}
