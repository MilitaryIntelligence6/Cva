package cn.misection.cvac.ast.expr.nonterminal.unary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.expr.terminator.CvaIdentifierExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.constant.EnumIncDirection;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaIncrementExpr
 * @Description 自增自减只能作用于int型变量, 所以采用装饰者模式;
 * 同时也可以其继承identifier自己, 就可以包装自己; 但这样逻辑很复杂, 会导致类关系混乱;
 * @CreateTime 2021年02月21日 12:44:00
 */
public final class CvaIncDecExpr extends AbstractExpression
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

    /**
     * 被自增者的变量名;
     * @return 名;
     */
    public String name()
    {
        return identifier.name();
    }

    public CvaIdentifierExpr getIdentifier()
    {
        return identifier;
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