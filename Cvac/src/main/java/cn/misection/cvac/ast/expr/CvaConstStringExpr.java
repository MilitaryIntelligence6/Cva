package cn.misection.cvac.ast.expr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaStringExpr
 * @Description TODO
 * @CreateTime 2021年02月18日 14:54:00
 */
public final class CvaConstStringExpr extends AbstractExpression
{
    private String literal;

    public CvaConstStringExpr(int lineNum, String literal)
    {
        super(lineNum);
        this.literal = literal;
    }

    public String getLiteral()
    {
        return literal;
    }

    public void setLiteral(String literal)
    {
        this.literal = literal;
    }
}
