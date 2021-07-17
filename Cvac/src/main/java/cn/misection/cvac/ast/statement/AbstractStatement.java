package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.CvaNullable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 17:54:00
 */
public abstract class AbstractStatement implements ICvaStatement, CvaNullable {
    protected int lineNum;

    protected AbstractStatement(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getLineNum() {
        return lineNum;
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
