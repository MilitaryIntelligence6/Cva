package cn.misection.cvac.ast.type.basic;

import cn.misection.cvac.ast.type.ICvaType;
import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;

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
    NULL_POINTER("nullPointer"),

    VOID("@void", EnumTargetType.VOID),

    BYTE("@byte", EnumTargetType.BYTE),

    CHAR("@char", EnumTargetType.CHAR),

    SHORT("@short", EnumTargetType.SHORT),

    INT("@int", EnumTargetType.INT),

    LONG("@long", EnumTargetType.LONG),

    BOOLEAN("@boolean", EnumTargetType.INT),

    FLOAT("@float", EnumTargetType.FLOAT),

    DOUBLE("@double", EnumTargetType.DOUBLE),

    POINTER("@pointer", EnumTargetType.POINTER),

    STRING("@string", EnumTargetType.STRING),

    ARRAY("@array", EnumTargetType.ARRAY),

    STRUCT("@struct", EnumTargetType.STRUCT),

    CLASS("@class", EnumTargetType.CLASS),

    ENUM("@enum", EnumTargetType.ENUM),

    UNKNOWN("unknown"),
    ;

    /**
     * instance domain;
     */
    private final String literal;

    private EnumTargetType targetType;

    EnumCvaType(String literal)
    {
        this.literal = literal;
    }

    EnumCvaType(String literal, EnumTargetType targetType)
    {
        this.literal = literal;
        this.targetType = targetType;
    }

    public EnumTargetType toTarget()
    {
        return targetType;
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
