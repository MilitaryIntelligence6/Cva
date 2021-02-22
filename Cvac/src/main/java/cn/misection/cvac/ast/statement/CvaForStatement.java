package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaForStatement
 * @Description TODO
 * @CreateTime 2021年02月22日 21:00:00
 */
public final class CvaForStatement extends CvaWhileStatement
{
    private AbstractExpression forInit;

    public CvaForStatement(int lineNum,
                           AbstractExpression condition,
                           AbstractStatement body,
                           AbstractExpression forInit)
    {
        super(lineNum, condition, body);
        this.forInit = forInit;
    }
}
