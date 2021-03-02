package cn.misection.cvac.codegen.bst.btype.advance;

import cn.misection.cvac.codegen.bst.btype.BaseType;
import cn.misection.cvac.codegen.bst.instructor.Instructable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName BaseAdvanceType
 * @Description TODO
 * @CreateTime 2021年02月20日 22:30:00
 */
public abstract class BaseAdvanceType
        extends BaseType implements Instructable
{
    /**
     * 获得写入文本;
     * @return literal;
     */
    public abstract String fullName();
}
