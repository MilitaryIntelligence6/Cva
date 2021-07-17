package cn.misection.cvac.codegen.bst.instructor;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumInstruction
 * @Description TODO
 * @CreateTime 2021年02月20日 23:57:00
 */
public enum EnumInstructor implements IInstructor, Instructable {
    /**
     * JVM指令集;
     *
     * @TODO 重构成桥接模式;
     */
    A_LOAD,

    A_RETURN("areturn"),

    A_STORE,

    GET_FIELD,

    GOTO,

    IF_I_CMP_LT,

    I_LOAD,

    INVOKE_VIRTUAL,

    I_RETURN("ireturn"),

    I_STORE,

    LABEL_J,

    /**
     * LoaD Const;
     */
    LDC,

    NEW,

    PUT_FIELD,

    WRITE_INSTRUCTION,

    /**
     * 操作数yype;
     */
    OPERAND_TYPE,

    /**
     * 操作;
     */
    OPERATOR,
    ;

    private String instruction;

    EnumInstructor() {
    }

    EnumInstructor(String instruction) {
        this.instruction = instruction;
    }

    @Override
    public String toInst() {
        return instruction;
    }
}
