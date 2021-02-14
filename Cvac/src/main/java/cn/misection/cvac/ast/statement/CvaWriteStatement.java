package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaWriteStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 18:43:00
 */
public class CvaWriteStatement extends AbstractStatement
{
    private AbstractExpression expr;

    public CvaWriteStatement(int lineNum, AbstractExpression expr)
    {
        super(lineNum);
        this.expr = expr;
    }

    public AbstractExpression getExpr()
    {
        return expr;
    }
}
