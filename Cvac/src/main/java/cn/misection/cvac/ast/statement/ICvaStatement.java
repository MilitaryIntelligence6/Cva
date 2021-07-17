package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.IASTreeNode;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName AbstractStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 18:02:00
 */
@FunctionalInterface
public interface ICvaStatement extends IASTreeNode {
    /**
     * 拿到enum;
     *
     * @return toEnum;
     */
    EnumCvaStatement toEnum();
}
