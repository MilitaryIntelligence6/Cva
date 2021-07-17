package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.type.ICvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 18:28:00
 */
public final class CvaAssignStatement extends AbstractStatement {
    /**
     * 被赋值变量名, 一般在表达式左边;
     */
    private String varName;

    private AbstractExpression expr;

    private ICvaType type;

    public CvaAssignStatement(int lineNum, String varName, AbstractExpression expr) {
        super(lineNum);
        this.varName = varName;
        this.expr = expr;
        init();
    }

    private void init() {
        this.type = null;
    }

    @Override
    public EnumCvaStatement toEnum() {
        return EnumCvaStatement.ASSIGN;
    }

    public String getVarName() {
        return varName;
    }

    public AbstractExpression getExpr() {
        return expr;
    }

    public ICvaType getType() {
        return type;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public void setExpr(AbstractExpression expr) {
        this.expr = expr;
    }

    public void setType(ICvaType type) {
        this.type = type;
    }
}
