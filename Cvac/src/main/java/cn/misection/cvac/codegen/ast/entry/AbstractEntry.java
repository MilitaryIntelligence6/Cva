package cn.misection.cvac.codegen.ast.entry;

import cn.misection.cvac.codegen.ast.statement.AbstractStatement;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMain
 * @Description main 方法入口;
 * @CreateTime 2021年02月14日 17:54:00
 */
public abstract class AbstractEntry implements IEntry
{
    protected String literal;

    protected AbstractStatement statement;

    protected AbstractEntry(String literal, AbstractStatement statement)
    {
        this.literal = literal;
        this.statement = statement;
    }

    public String getLiteral()
    {
        return literal;
    }

    public AbstractStatement getStatement()
    {
        return statement;
    }

    public void setLiteral(String literal)
    {
        this.literal = literal;
    }

    public void setStatement(AbstractStatement statement)
    {
        this.statement = statement;
    }
}
