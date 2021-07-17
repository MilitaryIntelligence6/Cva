package cn.misection.cvac.codegen.bst.instructor.operand;

import cn.misection.cvac.codegen.bst.instructor.IInstructor;
import cn.misection.cvac.codegen.bst.instructor.Instructable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumOperator
 * @Description 桥接模式底层操作符;
 * @CreateTime 2021年02月21日 22:24:00
 */
public enum EnumOperator implements IInstructor, Instructable {
    /**
     * 底层操作符;
     */

    ADD("add"),

    SUB("sub"),

    MUL("mul"),

    DIV("div"),

    /**
     * 求余;
     */
    /*
     * neg 其实不应该出现, 其是一元的;
     */
    BIT_NEG("neg"),

    REM("rem"),

    BIT_AND("and"),

    BIT_OR("or"),

    BIT_XOR("xor"),

    LEFT_SHIFT("shl"),

    RIGHT_SHIFT("shr"),

    /**
     * 无符号右移;
     */
    UNSIGNED_RIGHT_SHIFT("ushr"),

    RETURN("return"),
    ;

    private final String opInst;

    EnumOperator(String opInst) {
        this.opInst = opInst;
    }

    @Override
    public String toInst() {
        return opInst;
    }
}
