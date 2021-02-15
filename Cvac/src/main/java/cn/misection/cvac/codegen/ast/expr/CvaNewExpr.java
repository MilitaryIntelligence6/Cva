package cn.misection.cvac.codegen.ast.expr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaNew
 * @Description TODO
 * @CreateTime 2021年02月14日 19:24:00
 */
public class CvaNewExpr extends AbstractExpression
{
    private String literal;

    public CvaNewExpr(int lineNum, String literal)
    {
        super(lineNum);
        this.literal = literal;
    }

    public String getLiteral()
    {
        return literal;
    }
}
