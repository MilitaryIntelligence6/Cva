package cn.misection.cvac.ast.expr;

import cn.misection.cvac.ast.IASTreeNode;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Expression
 * @Description TODO
 * @CreateTime 2021年02月14日 17:59:00
 */
public interface ICvaExpression extends IASTreeNode {
    /**
     * type 传入 int string byte等, 放一个常量池里;
     *
     * @return expr 的 type;
     */
    EnumCvaType resType();

    /**
     * 取enum;
     *
     * @return enum 值;
     */
    EnumCvaExpr toEnum();
}
