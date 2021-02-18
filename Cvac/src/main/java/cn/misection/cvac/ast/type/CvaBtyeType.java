package cn.misection.cvac.ast.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaBtye
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public final class CvaBtyeType extends AbstractType
{
    public static final String TYPE_LITERAL = "@byte";

    public CvaBtyeType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }
}
