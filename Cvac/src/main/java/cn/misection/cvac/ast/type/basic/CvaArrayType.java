package cn.misection.cvac.ast.type.basic;

import cn.misection.cvac.ast.type.AbstractType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaArrayType
 * @Description TODO
 * @CreateTime 2021年02月19日 22:28:00
 */
public final class CvaArrayType extends AbstractBasicType
{
    public static final String TYPE_LITERAL = "@array";

    private AbstractType innerType;

    private int size;

    public CvaArrayType(AbstractType innerType, int size)
    {
        this.innerType = innerType;
        this.size = size;
    }

    @Override
    public String toString()
    {
        return String.format("@array%s", innerType.toString());
    }

    public AbstractType getInnerType()
    {
        return innerType;
    }

    public void setInnerType(AbstractType innerType)
    {
        this.innerType = innerType;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }
}
