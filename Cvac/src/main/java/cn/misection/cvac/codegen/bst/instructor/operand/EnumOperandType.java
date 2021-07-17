package cn.misection.cvac.codegen.bst.instructor.operand;

import cn.misection.cvac.codegen.bst.instructor.IInstructor;
import cn.misection.cvac.codegen.bst.instructor.Instructable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumTargetOperand
 * @Description 桥接模式底层操作数;
 * @CreateTime 2021年02月21日 22:23:00
 */
public enum EnumOperandType implements IInstructor, Instructable {
    /**
     * 底层操作数类型;
     */
    VOID(""),

    BYTE("b"),

    SHORT("s"),

    CHAR("c"),

    INT("i"),

    LONG("l"),

    FLOAT("f"),

    DOUBLE("d"),

    REFERENCE("a"),
    ;

    private final String typeInst;

    EnumOperandType(String typeInst) {
        this.typeInst = typeInst;
    }


    @Override
    public String toInst() {
        return typeInst;
    }
}
