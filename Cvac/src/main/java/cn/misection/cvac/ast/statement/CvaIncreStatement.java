package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.EnumCvaExpr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaIncrementExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 21:53:00
 */
public class CvaIncreStatement extends AbstractStatement
{
    private String literal;

    public CvaIncreStatement(int lineNum, String literal)
    {
        super(lineNum);
        this.literal = literal;
    }

    @Override
    public EnumCvaStatement toEnum()
    {
        return EnumCvaStatement.INCREMENT;
    }
}
