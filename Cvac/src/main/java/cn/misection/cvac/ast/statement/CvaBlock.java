package cn.misection.cvac.ast.statement;

import java.util.Queue;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaBlock
 * @Description TODO
 * @CreateTime 2021年02月14日 18:35:00
 */
public class CvaBlock extends AbstractStatement
{
    private Queue<AbstractStatement> statementQueue;

    public CvaBlock(int lineNum, Queue<AbstractStatement> statementQueue)
    {
        super(lineNum);
        this.statementQueue = statementQueue;
    }

    public Queue<AbstractStatement> getStatementQueue()
    {
        return statementQueue;
    }
}
