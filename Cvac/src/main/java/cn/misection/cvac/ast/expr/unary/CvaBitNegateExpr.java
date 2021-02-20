package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.BitOperable;
import cn.misection.cvac.ast.expr.EnumCvaExpr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaBitNegateExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:15:00
 */
public class CvaBitNegateExpr
        extends AbstractUnaryExpr implements BitOperable
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
