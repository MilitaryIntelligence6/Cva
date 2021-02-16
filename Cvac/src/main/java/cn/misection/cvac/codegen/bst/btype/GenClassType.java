package cn.misection.cvac.codegen.bst.btype;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:45:00
 */
public final class GenClassType extends BaseType
{
    private String literal;

    public GenClassType(String literal)
    {
        this.literal = literal;
    }

    // FIXME
    // 后端这样行不行;
    @Override
    public String toString()
    {
        return String.format("@class:%s", literal);
    }

    public String getLiteral()
    {
        return literal;
    }
}
