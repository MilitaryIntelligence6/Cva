package cn.misection.cvac.ast.type.advance;

import cn.misection.cvac.ast.type.ICvaType;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaArrayType
 * @Description TODO
 * @CreateTime 2021年02月19日 22:28:00
 */
public final class CvaArrayType extends AbstractAdvanceType
{
    public static final String TYPE_LITERAL = "@array";

    private ICvaType innerType;

    private int size;

    private static final EnumCvaType ENUM_TYPE = EnumCvaType.ARRAY;

    public CvaArrayType(ICvaType innerType, int size)
    {
        this.innerType = innerType;
        this.size = size;
    }

    public CvaArrayType(ICvaType innerType)
    {
        this.innerType = innerType;
    }

    @Override
    public EnumCvaType toEnum()
    {
        return ENUM_TYPE;
    }

    @Override
    public String toString()
    {
        return String.format("@array%s", innerType.toString());
    }

    public ICvaType getInnerType()
    {
        return innerType;
    }

    public void setInnerType(ICvaType innerType)
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
