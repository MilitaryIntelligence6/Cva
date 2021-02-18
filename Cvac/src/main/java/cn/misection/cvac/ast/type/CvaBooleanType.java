package cn.misection.cvac.ast.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaBoolean
 * @Description TODO
 * @CreateTime 2021年02月14日 19:41:00
 */
public class CvaBooleanType extends AbstractType
{
    public static final String TYPE_LITERAL = "@boolean";

    public CvaBooleanType()
    {
    }

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }
}
