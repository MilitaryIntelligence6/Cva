package cn.misection.cvac.codegen.bst.bentry;

import cn.misection.cvac.codegen.bst.instruction.BaseInstruction;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMain
 * @Description main 方法入口;
 * @CreateTime 2021年02月14日 17:54:00
 */
public abstract class BaseEntry implements IEntry
{
    protected String literal;

    protected List<BaseInstruction> statementList;

    protected BaseEntry(String literal, List<BaseInstruction> statementList)
    {
        this.literal = literal;
        this.statementList = statementList;
    }

    public String getLiteral()
    {
        return literal;
    }

    public List<BaseInstruction> getStatementList()
    {
        return statementList;
    }

    public void setLiteral(String literal)
    {
        this.literal = literal;
    }

    public void setStatementList(List<BaseInstruction> statementList)
    {
        this.statementList = statementList;
    }
}
