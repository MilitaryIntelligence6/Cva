package cn.misection.cvac.ast.expr.nonterminal.unary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.terminator.CvaIdentifierExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.constant.EnumIncDirection;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaIncrementExpr
 * @Description TODO
 * @CreateTime 2021年02月21日 12:44:00
 */
public final class CvaIncDecExpr extends AbstractUnaryExpr
{
    private final CvaIdentifierExpr identifier;

    private final EnumIncDirection direction;

    public CvaIncDecExpr(int lineNum,
                         CvaIdentifierExpr identifier,
                         EnumIncDirection direction)
    {
        super(lineNum);
        this.identifier = identifier;
        this.direction = direction;
    }

    public String getLiteral()
    {
        return identifier.getLiteral();
    }

    public EnumIncDirection getDirection()
    {
        return direction;
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.INT;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.INCREMENT;
    }
}