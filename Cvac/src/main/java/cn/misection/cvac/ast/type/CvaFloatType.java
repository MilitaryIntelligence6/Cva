package cn.misection.cvac.ast.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaFloat
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public class CvaFloatType extends AbstractType
{
    public static final String TYPE_LITERAL = "@float";

    public CvaFloatType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }
}
