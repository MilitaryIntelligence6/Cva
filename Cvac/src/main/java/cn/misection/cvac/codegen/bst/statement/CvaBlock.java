package cn.misection.cvac.codegen.bst.statement;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaBlock
 * @Description TODO
 * @CreateTime 2021年02月14日 18:35:00
 */
public class CvaBlock extends AbstractStatement
{
    private List<AbstractStatement> statementList;

    public CvaBlock(int lineNum, List<AbstractStatement> statementList)
    {
        super(lineNum);
        this.statementList = statementList;
    }

    public List<AbstractStatement> getStatementList()
    {
        return statementList;
    }

    public void setStatementList(List<AbstractStatement> statementList)
    {
        this.statementList = statementList;
    }
}
