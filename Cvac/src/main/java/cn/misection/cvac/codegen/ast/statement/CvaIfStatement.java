package cn.misection.cvac.codegen.ast.statement;

import cn.misection.cvac.codegen.ast.expr.AbstractExpression;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaIfStatem
 * @Description TODO
 * @CreateTime 2021年02月14日 18:40:00
 */
public class CvaIfStatement extends AbstractStatement
{
    private AbstractExpression condition;

    private AbstractStatement thenStatement;

    private AbstractStatement elseStatement;

    public CvaIfStatement(int lineNum, AbstractExpression condition, AbstractStatement thenStatement, AbstractStatement elseStatement)
    {
        super(lineNum);
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    public AbstractExpression getCondition()
    {
        return condition;
    }

    public AbstractStatement getThenStatement()
    {
        return thenStatement;
    }

    public AbstractStatement getElseStatement()
    {
        return elseStatement;
    }

    public void setCondition(AbstractExpression condition)
    {
        this.condition = condition;
    }

    public void setThenStatement(AbstractStatement thenStatement)
    {
        this.thenStatement = thenStatement;
    }

    public void setElseStatement(AbstractStatement elseStatement)
    {
        this.elseStatement = elseStatement;
    }
}
