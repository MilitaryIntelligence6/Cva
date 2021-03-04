package cn.misection.cvac.ast.type.basic;

import cn.misection.cvac.ast.type.ICvaType;
import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;
import cn.misection.cvac.codegen.bst.instructor.operand.EnumOperandType;

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

    VOID("@void", EnumTargetType.VOID, EnumOperandType.VOID),

    BYTE("@byte", EnumTargetType.BYTE, EnumOperandType.BYTE),

    CHAR("@char", EnumTargetType.CHAR, EnumOperandType.CHAR),

    SHORT("@short", EnumTargetType.SHORT, EnumOperandType.SHORT),

    INT("@int", EnumTargetType.INT, EnumOperandType.INT),

    LONG("@long", EnumTargetType.LONG, EnumOperandType.LONG),

    BOOLEAN("@boolean", EnumTargetType.INT),

    FLOAT("@float", EnumTargetType.FLOAT, EnumOperandType.FLOAT),

    DOUBLE("@double", EnumTargetType.DOUBLE, EnumOperandType.DOUBLE),

    POINTER("@pointer", EnumTargetType.POINTER),

    STRING("@string", EnumTargetType.STRING, EnumOperandType.REFERENCE),

    ARRAY("@array", EnumTargetType.ARRAY),

    STRUCT("@struct", EnumTargetType.STRUCT),

    CLASS("@class", EnumTargetType.CLASS, EnumOperandType.REFERENCE),

    ENUM("@enum", EnumTargetType.ENUM),

    UNKNOWN("unknown"),
    ;

    /**
     * instance domain;
     */
    private final String typeName;

    private EnumTargetType targetType;

    private EnumOperandType operandType;

    EnumCvaType(String typeName)
    {
        this.typeName = typeName;
    }

    EnumCvaType(String typeName, EnumTargetType targetType)
    {
        this.typeName = typeName;
        this.targetType = targetType;
    }

    EnumCvaType(String typeName,
                EnumTargetType targetType,
                EnumOperandType operandType)
    {
        this.typeName = typeName;
        this.targetType = targetType;
        this.operandType = operandType;
    }

    public EnumTargetType toTarget()
    {
        return targetType;
    }

    public EnumOperandType toOperand()
    {
        return operandType;
    }

    public String getTypeName()
    {
        return typeName;
    }

    @Override
    public String toString()
    {
        return typeName;
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
