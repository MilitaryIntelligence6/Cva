package cn.misection.cvac.ast.expr.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.binary.AbstractBinaryExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMoreThanExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:13:00
 */
public class CvaMoreThanExpr extends AbstractBinaryExpr
{
    public CvaMoreThanExpr(int lineNum,
                           AbstractExpression left,
                           AbstractExpression right)
    {
        super(lineNum, left, right);
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.CVA_BOOLEAN;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.MORE_THAN;
    }
}
