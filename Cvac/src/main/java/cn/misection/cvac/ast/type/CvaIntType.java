package cn.misection.cvac.ast.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaInt
 * @Description TODO
 * @CreateTime 2021年02月14日 19:44:00
 */
public final class CvaIntType extends AbstractType
{
    public static final String TYPE_LITERAL = "@int";

    public CvaIntType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }
}
