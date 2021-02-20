package cn.misection.cvac.ast.type.basic;

import cn.misection.cvac.ast.type.ICvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaType
 * @Description TODO
 * @CreateTime 2021年02月20日 14:04:00
 */
public enum EnumCvaType implements ICvaType
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

    CVA_POINTER("@pointer"),

    CVA_STRING("@string"),

    CVA_ARRAY("@array"),

    CVA_STRUCT("@struct"),

    CVA_CLASS("@class"),

    CVA_ENUM("@enum"),
    ;

    /**
     * instance domain;
     */
    private final String literal;

    EnumCvaType(String literal)
    {
        this.literal = literal;
    }

    public String getLiteral()
    {
        return literal;
    }

    @Override
    public String toString()
    {
        return literal;
    }

    @Override
    public EnumCvaType toEnum()
    {
        return this;
    }

    /*
     * end instance domain;
     */

    /**
     * class static domain;
     * @param type t
     * @return
     */
    public static boolean isBasicType(EnumCvaType type)
    {
        return type.ordinal() >= CVA_VOID.ordinal()
                && type.ordinal() <= CVA_DOUBLE.ordinal();
    }

    public static boolean isAdvanceType(EnumCvaType type)
    {
        return type.ordinal() >= CVA_POINTER.ordinal()
                && type.ordinal() <= CVA_ARRAY.ordinal();
    }

    public static boolean isReferenceType(EnumCvaType type)
    {
        return type.ordinal() >= CVA_STRUCT.ordinal();
    }
}
