package cn.misection.cvac.codegen.bst.btype.refer;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaString
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public final class GenString extends BaseReferenceType
{
    private final String literal = "java/lang/String";

    public GenString()
    {
    }

    @Override
    public String toString()
    {
        return "@String";
    }

    @Override
    public String getLiteral()
    {
        return literal;
    }
}
