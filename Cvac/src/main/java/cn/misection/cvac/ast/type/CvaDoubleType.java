package cn.misection.cvac.ast.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDouble
 * @Description TODO
 * @CreateTime 2021年02月14日 19:45:00
 */
public final class CvaDoubleType extends AbstractType
{
    public static final String TYPE_LITERAL = "@double";

    public CvaDoubleType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }
}
