package cn.misection.cvac.codegen.bst.instructor;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName IDec
 * @Description TODO
 * @CreateTime 2021年02月21日 11:18:00
 */
public class IDec
        extends BaseInstructor implements Instructable
{
    private static final int DECREMENT_SIZE = 1;

    private int index;

    public IDec(int index)
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
        return String.format("idec %d  %d", index, DECREMENT_SIZE);
    }
}
