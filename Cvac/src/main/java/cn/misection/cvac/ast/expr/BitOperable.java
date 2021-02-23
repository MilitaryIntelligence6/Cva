package cn.misection.cvac.ast.expr;

import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName BitOperator
 * @Description 这是一个假的函数式接口, 因为方法不是抽象的;
 * @CreateTime 2021年02月20日 21:30:00
 */
@FunctionalInterface
public interface BitOperable extends ICvaExpression
{
    /**
     * 位操作只能是int;
     * @return int;
     */
    @Override
    default EnumCvaType resType()
    {
        return EnumCvaType.CVA_INT;
    }
}
