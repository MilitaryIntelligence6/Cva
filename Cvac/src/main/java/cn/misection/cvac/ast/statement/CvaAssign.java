package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.type.AbstractType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 18:28:00
 */
public class CvaAssign extends AbstractStatement
{
    private String id;

    private AbstractExpression expr;

    private AbstractType type;

    public CvaAssign(int lineNum, String id, AbstractExpression expr, AbstractType type)
    {
        super(lineNum);
        this.id = id;
        this.expr = expr;
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public AbstractExpression getExpr()
    {
        return expr;
    }

    public AbstractType getType()
    {
        return type;
    }
}
