package cn.misection.cvac.codegen.bst.btype;

import cn.misection.cvac.codegen.bst.IBackendSyntaxTree;
import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Type
 * @Description TODO
 * @CreateTime 2021年02月14日 17:57:00
 */
public interface ITargetType extends IBackendSyntaxTree
{
    /**
     * 拿string值;
     * @return string;
     */
    @Override
    String toString();

    /**
     * 拿enum值;
     * @return enum;
     */
    EnumTargetType toEnum();
}
