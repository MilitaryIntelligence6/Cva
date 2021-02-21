package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.binary.AbstractBinaryExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.codegen.bst.instructor.EnumInstructor;
import cn.misection.cvac.constant.EnumIncDirection;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaAdd
 * @Description TODO
 * @CreateTime 2021年02月14日 18:49:00
 */
public final class CvaAddExpr extends AbstractBinaryOperator
{
    public CvaAddExpr(int lineNum,
                      AbstractExpression left,
                      AbstractExpression right)
    {
        super(lineNum, left, right);
    }

    /**
     * 目前只实现 int, 应该改成做选择之类;
     * @return 指令;
     */
    @Override
    public EnumInstructor targetInstruction()
    {
        return EnumInstructor.I_ADD;
    }


    @Override
    public EnumCvaType resType()
    {
        return this.left.resType();
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.ADD;
    }
}
