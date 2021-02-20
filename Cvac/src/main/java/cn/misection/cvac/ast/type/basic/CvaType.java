package cn.misection.cvac.ast.type.basic;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaType
 * @Description TODO
 * @CreateTime 2021年02月20日 14:04:00
 */
public enum CvaType
{
    /**
     * void;
     */
    CVA_VOID("@void"),

    CVA_BYTE("@byte"),

    CVA_CHAR("@char"),

    CVA_SHORT("@short"),

    CVA_INT("@int"),

    CVA_LONG("@long"),

    CVA_BOOLEAN("@boolean"),

    CVA_FLOAT("@float"),

    CVA_DOUBLE("@double"),
    ;

    private final String literal;

    CvaType(String literal)
    {
        this.literal = literal;
    }

    public String getLiteral()
    {
        return literal;
    }
}
