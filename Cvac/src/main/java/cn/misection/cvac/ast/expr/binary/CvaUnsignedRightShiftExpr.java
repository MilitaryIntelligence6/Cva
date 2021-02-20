package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.BitOperable;
import cn.misection.cvac.ast.expr.EnumCvaExpr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaUnsignedRightShiftExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:59:00
 */
public class CvaUnsignedRightShiftExpr
        extends AbstractBinaryExpr implements BitOperable
{
    public CvaUnsignedRightShiftExpr(int lineNum,
                                     AbstractExpression left,
                                     AbstractExpression right)
    {
        super(lineNum, left, right);
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.UNSIGNED_RIGHT_SHIFT;
    }
}
