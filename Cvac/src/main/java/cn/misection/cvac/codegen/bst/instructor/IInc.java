package cn.misection.cvac.codegen.bst.instructor;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Iinc
 * @Description int 自增;
 * @CreateTime 2021年02月21日 10:26:00
 */
public final class IInc
        extends BaseInstructor implements Instructable
{
    private static final int INCREMENT_SIZE = 1;

    private int index;

    public IInc(int index)
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

    @Override
    public String instruction()
    {
        return String.format("iinc %d %d", index, INCREMENT_SIZE);
    }
}
