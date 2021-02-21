package cn.misection.cvac.ast.statement;


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

    public CvaIncreStatement(int lineNum, String literal)
    {
        super(lineNum);
        this.literal = literal;
    }

    public String getLiteral()
    {
        return literal;
    }

    @Override
    public EnumCvaStatement toEnum()
    {
        return EnumCvaStatement.INCREMENT;
    }
}
