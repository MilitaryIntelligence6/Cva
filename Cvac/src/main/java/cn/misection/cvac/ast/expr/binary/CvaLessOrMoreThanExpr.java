package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaLTExpr
 * @Description TODO
 * @TODO 这是啥;
 * @CreateTime 2021年02月14日 19:22:00
 */
public final class CvaLessOrMoreThanExpr extends AbstractBinaryExpr
{
    public CvaLessOrMoreThanExpr(int lineNum,
                                 AbstractExpression left,
                                 AbstractExpression right)
    {
        super(lineNum, left, right);
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.BOOLEAN;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.LESS_THAN;
    }
}
