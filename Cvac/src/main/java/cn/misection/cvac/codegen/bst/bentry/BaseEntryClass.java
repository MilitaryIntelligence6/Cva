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
public abstract class BaseEntryClass implements IEntryClass
{
    protected String name;

    protected List<BaseInstruction> statementList;

    protected BaseEntryClass(String name, List<BaseInstruction> statementList)
    {
        this.name = name;
        this.statementList = statementList;
    }

    public String getName()
    {
        return name;
    }

    public List<BaseInstruction> getStatementList()
    {
        return statementList;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setStatementList(List<BaseInstruction> statementList)
    {
        this.statementList = statementList;
    }
}
