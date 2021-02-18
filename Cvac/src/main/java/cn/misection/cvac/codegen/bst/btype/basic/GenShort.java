package cn.misection.cvac.codegen.bst.btype.basic;

import cn.misection.cvac.codegen.bst.btype.BaseType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaShort
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public final class GenShort extends BaseBasicType
{
    public GenShort()
    {
    }

    @Override
    public String toString()
    {
        return "@short";
    }

    @Override
    public String requireInstruct()
    {
        return null;
    }
}
