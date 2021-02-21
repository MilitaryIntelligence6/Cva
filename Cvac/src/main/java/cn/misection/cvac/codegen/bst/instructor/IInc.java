package cn.misection.cvac.codegen.bst.instructor;

import cn.misection.cvac.constant.EnumIncDirection;

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
    private int index;

    private EnumIncDirection direction;

    public IInc(int index, EnumIncDirection direction)
    {
        this.index = index;
        this.direction = direction;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public EnumIncDirection getDirection()
    {
        return direction;
    }

    public void setDirection(EnumIncDirection direction)
    {
        this.direction = direction;
    }

    @Override
    public String instruction()
    {
        return String.format("iinc %d %d",
                index, direction.direction());
    }
}
