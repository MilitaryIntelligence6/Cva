package cn.misection.cvac.codegen.bst.statement;

import cn.misection.cvac.codegen.bst.expr.AbstractExpression;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaWriteStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 18:43:00
 */
public class CvaWriteOperation extends AbstractStatement
{
    private AbstractExpression expr;

    public CvaWriteOperation(int lineNum, AbstractExpression expr)
    {
        super(lineNum);
        this.expr = expr;
    }

    public AbstractExpression getExpr()
    {
        return expr;
    }

    public void setExpr(AbstractExpression expr)
    {
        this.expr = expr;
    }
}
