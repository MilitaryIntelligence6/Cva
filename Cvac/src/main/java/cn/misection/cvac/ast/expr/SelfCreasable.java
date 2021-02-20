package cn.misection.cvac.ast.expr;

import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName SelfCreable
 * @Description 自增自减运算;
 * @CreateTime 2021年02月20日 21:55:00
 */
@FunctionalInterface
public interface SelfCreasable extends IExpression
{
    /**
     * 自增自减只有 int能用;
     * @return
     */
    @Override
    default EnumCvaType resType()
    {
        return EnumCvaType.CVA_INT;
    }
}
