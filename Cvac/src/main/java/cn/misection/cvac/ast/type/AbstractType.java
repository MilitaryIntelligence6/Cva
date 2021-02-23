package cn.misection.cvac.ast.type;

import cn.misection.cvac.ast.CvaNullable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaType
 * @Description TODO
 * @CreateTime 2021年02月14日 17:52:00
 */
public abstract class AbstractType implements ICvaType, CvaNullable
{
    @Override
    public boolean isNull()
    {
        return false;
    }
}
