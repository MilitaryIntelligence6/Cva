package cn.misection.cvac.codegen.bst.binstruct;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName AStore
 * @Description TODO
 * @CreateTime 2021年02月16日 00:47:00
 */
public final class AStore extends BaseInstruction
{
    private int index;

    public AStore(int index)
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
