package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.BitOperable;
import cn.misection.cvac.ast.expr.EnumCvaExpr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaBitOrExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:14:00
 */
public class CvaBitOrExpr
        extends AbstractBinaryExpr implements BitOperable
{
    public CvaBitOrExpr(int lineNum,
                        AbstractExpression left,
                        AbstractExpression right)
    {
        super(lineNum, left, right);
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.BIT_OR;
    }
}
