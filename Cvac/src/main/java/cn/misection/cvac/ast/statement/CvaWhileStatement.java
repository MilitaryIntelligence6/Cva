package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaWhileStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 18:47:00
 */
public final class CvaWhileStatement extends AbstractStatement
{
    private AbstractExpression condition;

    private AbstractStatement body;

    public CvaWhileStatement(int lineNum, AbstractExpression condition, AbstractStatement body)
    {
        super(lineNum);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public EnumCvaStatement toEnum()
    {
        return EnumCvaStatement.WHILE;
    }

    public AbstractExpression getCondition()
    {
        return condition;
    }

    public AbstractStatement getBody()
    {
        return body;
    }

    public void setCondition(AbstractExpression condition)
    {
        this.condition = condition;
    }

    public void setBody(AbstractStatement body)
    {
        this.body = body;
    }
}
