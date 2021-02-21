package cn.misection.cvac.ast.statement;


import cn.misection.cvac.constant.EnumIncDirection;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaIncrementExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 21:53:00
 */
public final class CvaIncreStatement extends AbstractStatement
{
    private final String literal;

    private final EnumIncDirection direction;

    public CvaIncreStatement(int lineNum,
                             String literal,
                             EnumIncDirection direction)
    {
        super(lineNum);
        this.literal = literal;
        this.direction = direction;
    }

    public String getLiteral()
    {
        return literal;
    }

    public EnumIncDirection getDirection()
    {
        return direction;
    }

    @Override
    public EnumCvaStatement toEnum()
    {
        return EnumCvaStatement.INCREMENT;
    }
}
