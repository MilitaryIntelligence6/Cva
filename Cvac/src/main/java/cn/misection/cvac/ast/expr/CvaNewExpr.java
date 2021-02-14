package cn.misection.cvac.ast.expr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaNew
 * @Description TODO
 * @CreateTime 2021年02月14日 19:24:00
 */
public class CvaNewExpr
{
    private String literal;

    public CvaNewExpr(String literal)
    {
        this.literal = literal;
    }

    public String getLiteral()
    {
        return literal;
    }
}
