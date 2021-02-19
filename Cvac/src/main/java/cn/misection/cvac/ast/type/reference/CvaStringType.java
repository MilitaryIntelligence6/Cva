package cn.misection.cvac.ast.type.reference;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaString
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public final class CvaStringType extends AbstractReferenceType
{
    public static final String TYPE_LITERAL = "@string";

    public CvaStringType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }
}
