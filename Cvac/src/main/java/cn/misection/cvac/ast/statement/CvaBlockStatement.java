package cn.misection.cvac.ast.statement;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaBlock
 * @Description TODO
 * @CreateTime 2021年02月14日 18:35:00
 */
public final class CvaBlockStatement extends AbstractStatement {
    private List<AbstractStatement> statementList;

    public CvaBlockStatement(int lineNum, List<AbstractStatement> statementList) {
        super(lineNum);
        this.statementList = statementList;
    }

    @Override
    public EnumCvaStatement toEnum() {
        return EnumCvaStatement.BLOCK;
    }

    public List<AbstractStatement> getStatementList() {
        return statementList;
    }

    public void setStatementList(List<AbstractStatement> statementList) {
        this.statementList = statementList;
    }
}
