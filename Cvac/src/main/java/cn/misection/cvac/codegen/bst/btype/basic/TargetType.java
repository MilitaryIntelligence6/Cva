package cn.misection.cvac.codegen.bst.btype.basic;

import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName TargetType
 * @Description TODO
 * @CreateTime 2021年02月20日 14:37:00
 */
public enum TargetType
{
    /**
     * void;
     */
    TARGET_VOID("@void"),

    TARGET_BYTE("@byte"),

    TARGET_CHAR("@char"),

    TARGET_SHORT("@short"),

    TARGET_INT("@int"),

    TARGET_LONG("@long"),

    TARGET_FLOAT("@float"),

    TARGET_DOUBLE("@double"),

    TARGET_POINTER("@pointer"),

    TARGET_STRING("@string"),

    TARGET_ARRAY("@array"),

    TARGET_STRUCT("@struct"),

    TARGET_CLASS("@class"),

    TARGET_ENUM("@enum"),
    ;

    private final String literal;

    TargetType(String literal)
    {
        this.literal = literal;
    }


    public static boolean isBasicType(TargetType type)
    {
        return type.ordinal() >= TARGET_VOID.ordinal()
                && type.ordinal() <= TARGET_DOUBLE.ordinal();
    }

    public static boolean isAdvanceType(TargetType type)
    {
        return type.ordinal() >= TARGET_POINTER.ordinal()
                && type.ordinal() <= TARGET_ARRAY.ordinal();
    }

    public static boolean isReferenceType(TargetType type)
    {
        return type.ordinal() >= TARGET_STRUCT.ordinal();
    }

    /**
     * 是否是整形, byte short等都是;
     * @param type
     * @return
     */
    public static boolean isInteger(TargetType type)
    {
        return type.ordinal() >= TARGET_BYTE.ordinal()
                && type.ordinal() <= TARGET_LONG.ordinal();
    }

    public static boolean isFloatPoint(TargetType type)
    {
        return type == TARGET_FLOAT || type == TARGET_DOUBLE;
    }

    /**
     * 是否是前端的数字, boolean在后端是, 但在前端不是;
     * @return
     */
    public static boolean isNumber(TargetType type)
    {
        return isInteger(type) || isFloatPoint(type);
    }
}
