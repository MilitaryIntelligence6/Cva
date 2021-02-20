package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.BitOperatorExpr;
import cn.misection.cvac.ast.expr.EnumCvaExpr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaRightShiftExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:14:00
 */
public class CvaRightShiftExpr
        extends AbstractBinaryExpr implements BitOperatorExpr
{
    public CvaRightShiftExpr(int lineNum,
                             AbstractExpression left,
                             AbstractExpression right)
    {
        super(lineNum, left, right);
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.RIGHT_SHIFT;
    }
}
