package cn.misection.cvac.ast.type;

import cn.misection.cvac.ast.IASTreeNode;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Type
 * @Description TODO
 * @CreateTime 2021年02月14日 17:57:00
 */
public interface ICvaType extends IASTreeNode {
    /**
     * 转string;
     *
     * @return toString;
     */
    @Override
    String toString();

    /**
     * 转换成最基本的enum;
     *
     * @return
     */
    EnumCvaType toEnum();
}
