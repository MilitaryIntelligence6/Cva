package cn.misection.cvac.codegen.bst.btype.refer;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaString
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public final class GenStringType extends BaseReferenceType
{
    private static final String LITERAL = "java/lang/String";

    public GenStringType()
    {
    }

    @Override
    public String toString()
    {
        return "@String";
    }

    @Override
    public String literal()
    {
        return LITERAL;
    }
}
