package cn.misection.cvac.codegen.ast.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:45:00
 */
public class CvaClassType extends AbstractType
{
    private String literal;

    public CvaClassType(String literal)
    {
        this.literal = literal;
    }

    @Override
    public String toString()
    {
        return String.format("@class:%s", literal);
    }

    public String getLiteral()
    {
        return literal;
    }
}
