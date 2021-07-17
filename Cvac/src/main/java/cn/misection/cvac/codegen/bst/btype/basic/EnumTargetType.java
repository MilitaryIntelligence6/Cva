package cn.misection.cvac.codegen.bst.btype.basic;

import cn.misection.cvac.codegen.bst.btype.ITargetType;
import cn.misection.cvac.codegen.bst.instructor.Instructable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName TargetType
 * @Description TODO
 * @CreateTime 2021年02月20日 14:37:00
 */
public enum EnumTargetType
        implements ITargetType, Instructable {
    /**
     * void;
     */
    VOID("@void", "V"),

    BYTE("@byte", "B"),

    CHAR("@char", "C"),

    SHORT("@short", "S"),

    INT("@int", "I"),

    LONG("@long", "J"),

    FLOAT("@float", "F"),

    DOUBLE("@double", "D"),

    POINTER("@pointer"),

    STRING("@string", "Ljava/lang/String;"),

    ARRAY("@array", "["),

    STRUCT("@struct"),

    CLASS("@class"),

    ENUM("@enum"),
    ;

    /**
     * instance domain;
     */
    private final String typeName;

    private String instruction;

    EnumTargetType(String typeName) {
        this.typeName = typeName;
    }

    EnumTargetType(String typeName, String instruction) {
        this.typeName = typeName;
        this.instruction = instruction;
    }

    @Override
    public EnumTargetType toEnum() {
        return this;
    }

    @Override
    public String toString() {
        return typeName;
    }

    @Override
    public String toInst() {
        return instruction;
    }

    public String getTypeName() {
        return typeName;
    }
    /*
     * end instance domain;
     */

    /**
     * class static domain;
     *
     * @param type t;
     * @return boolean;
     */
    public static boolean isBasicType(EnumTargetType type) {
        return type.ordinal() >= VOID.ordinal()
                && type.ordinal() <= DOUBLE.ordinal();
    }

    public static boolean isAdvanceType(EnumTargetType type) {
        return type.ordinal() >= POINTER.ordinal()
                && type.ordinal() <= ARRAY.ordinal();
    }

    public static boolean isReferenceType(EnumTargetType type) {
        return type.ordinal() >= STRUCT.ordinal();
    }

    /**
     * 是否是整形, byte short等都是;
     *
     * @param type t;
     * @return 包括byte char short long 等;
     */
    public static boolean isInteger(EnumTargetType type) {
        return type.ordinal() >= BYTE.ordinal()
                && type.ordinal() <= LONG.ordinal();
    }

    public static boolean isFloatPoint(EnumTargetType type) {
        return type == FLOAT || type == DOUBLE;
    }

    /**
     * 是否是前端的数字, boolean在后端是, 但在前端不是;
     *
     * @return isint;
     */
    public static boolean isNumber(EnumTargetType type) {
        return isInteger(type) || isFloatPoint(type);
    }
}
