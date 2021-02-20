package cn.misection.cvac.ast.expr;

import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName BitOperator
 * @Description TODO
 * @CreateTime 2021年02月20日 21:30:00
 */
@FunctionalInterface
public interface BitOperatorExpr extends IExpression
{
    /**
     * 位操作只能是int;
     * @return
     */
    @Override
    default EnumCvaType resType()
    {
        return EnumCvaType.CVA_INT;
    }
}
