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
    private String literal;

    private AbstractExpression expr;

    private AbstractType type;

    public CvaAssign(int lineNum, String literal, AbstractExpression expr, AbstractType type)
    {
        super(lineNum);
        this.literal = literal;
        this.expr = expr;
        this.type = type;
    }

    public String getLiteral()
    {
        return literal;
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
