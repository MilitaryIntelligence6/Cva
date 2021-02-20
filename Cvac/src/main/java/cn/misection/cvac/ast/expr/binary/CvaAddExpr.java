package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.binary.AbstractBinaryExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaAdd
 * @Description TODO
 * @CreateTime 2021年02月14日 18:49:00
 */
public final class CvaAddExpr extends AbstractBinaryExpr
{
    public CvaAddExpr(int lineNum,
                      AbstractExpression left,
                      AbstractExpression right)
    {
        super(lineNum, left, right);
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
