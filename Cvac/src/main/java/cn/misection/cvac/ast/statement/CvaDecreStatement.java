package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.EnumCvaExpr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDecrementExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 21:58:00
 */
public class CvaDecreStatement extends AbstractStatement
{
    private String literal;

    public CvaDecreStatement(int lineNum, String literal)
    {
        super(lineNum);
        this.literal = literal;
    }

    @Override
    public EnumCvaStatement toEnum()
    {
        return EnumCvaStatement.DECREMENT;
    }
}
