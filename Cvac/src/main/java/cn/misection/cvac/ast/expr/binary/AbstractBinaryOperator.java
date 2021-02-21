package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.codegen.bst.instructor.EnumInstructor;
import cn.misection.cvac.constant.EnumIncDirection;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName AbstractBinaryOperator
 * @Description TODO
 * @CreateTime 2021年02月21日 22:13:00
 */
public abstract class AbstractBinaryOperator extends AbstractBinaryExpr
{
    public AbstractBinaryOperator(int lineNum,
                                  AbstractExpression left,
                                  AbstractExpression right)
    {
        super(lineNum, left, right);
    }

    /**
     * 协助后端翻译者的运算符, 格式是统一的;
     * @return 最后一条指令;
     */
    public abstract EnumInstructor targetInstruction();
}
