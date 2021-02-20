package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.BitOperatorExpr;
import cn.misection.cvac.ast.expr.EnumCvaExpr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaLeftShiftExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:13:00
 */
public class CvaLeftShiftExpr
        extends AbstractBinaryExpr implements BitOperatorExpr
{
    public CvaLeftShiftExpr(int lineNum,
                            AbstractExpression left,
                            AbstractExpression right)
    {
        super(lineNum, left, right);
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.LEFT_SHIFT;
    }
}
