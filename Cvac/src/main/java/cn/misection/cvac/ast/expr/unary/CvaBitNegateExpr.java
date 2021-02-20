package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.BitOperatorExpr;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.unary.AbstractUnaryExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaBitNegateExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:15:00
 */
public class CvaBitNegateExpr
        extends AbstractUnaryExpr implements BitOperatorExpr
{
    protected CvaBitNegateExpr(int lineNum)
    {
        super(lineNum);
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.BIT_NEGATE;
    }
}
