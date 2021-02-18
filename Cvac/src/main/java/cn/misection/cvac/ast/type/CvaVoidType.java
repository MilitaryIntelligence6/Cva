package cn.misection.cvac.ast.type;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaVoid
 * @Description TODO
 * @CreateTime 2021年02月16日 20:12:00
 */
public class CvaVoidType extends AbstractType
{
    public static final String TYPE_LITERAL = "@void";

    public CvaVoidType() {}

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }
}
