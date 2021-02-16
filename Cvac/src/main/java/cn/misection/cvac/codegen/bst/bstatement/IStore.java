package cn.misection.cvac.codegen.bst.bstatement;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName IStore
 * @Description TODO
 * @CreateTime 2021年02月16日 00:57:00
 */
public final class IStore extends BaseStatement
{
    private int index;

    public IStore(int index)
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
