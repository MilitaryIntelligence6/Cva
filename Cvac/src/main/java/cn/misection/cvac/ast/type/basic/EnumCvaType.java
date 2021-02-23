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
     * enum type;
     */
    NULL("nullPointer"),

    VOID("@void"),

    BYTE("@byte"),

    CHAR("@char"),

    SHORT("@short"),

    INT("@int"),

    LONG("@long"),

    BOOLEAN("@boolean"),

    FLOAT("@float"),

    DOUBLE("@double"),

    POINTER("@pointer"),

    STRING("@string"),

    ARRAY("@array"),

    STRUCT("@struct"),

    CLASS("@class"),

    ENUM("@enum"),

    UNKNOWN("unknown"),
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
     * @return isBasic;
     */
    public static boolean isBasicType(EnumCvaType type)
    {
        return type.ordinal() >= VOID.ordinal()
                && type.ordinal() <= DOUBLE.ordinal();
    }

    public static boolean isAdvanceType(EnumCvaType type)
    {
        return type.ordinal() >= POINTER.ordinal()
                && type.ordinal() <= ARRAY.ordinal();
    }

    public static boolean isReferenceType(EnumCvaType type)
    {
        return type.ordinal() >= STRUCT.ordinal();
    }

    /**
     * 是否是整形, byte short等都是;
     * @param type t;
     * @return isInt;
     */
    public static boolean isInteger(EnumCvaType type)
    {
        return type.ordinal() >= BYTE.ordinal()
                && type.ordinal() <= LONG.ordinal();
    }

    public static boolean isFloatPoint(EnumCvaType type)
    {
        return type == FLOAT || type == DOUBLE;
    }

    /**
     * 是否是前端的数字, boolean在后端是, 但在前端不是;
     * @return isNumber;
     */
    public static boolean isNumber(EnumCvaType type)
    {
        return isInteger(type) || isFloatPoint(type);
    }
}
