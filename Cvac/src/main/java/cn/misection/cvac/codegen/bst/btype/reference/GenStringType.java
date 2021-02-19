package cn.misection.cvac.codegen.bst.btype.reference;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaString
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public final class GenStringType extends BaseReferenceType
{
    public static final String TYPE_LITERAL = "@string";

    public static final String FULL_LITERAL = "java/lang/String";

    public GenStringType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }

    @Override
    public String literal()
    {
        return FULL_LITERAL;
    }
}
