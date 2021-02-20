package cn.misection.cvac.codegen.bst.instruction;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumInstruction
 * @Description TODO
 * @CreateTime 2021年02月20日 23:57:00
 */
public enum EnumInstruction implements IInstruction
{
    /**
     * JVM指令集;
     */
    A_LOAD,

    A_RETURN,

    A_STORE,

    GET_FIELD,

    GOTO,

    I_ADD,

    IF_I_CMP_LT,

    I_LOAD,

    I_MUL,

    INVOKE_VIRTUAL,

    I_RETURN,

    I_STORE,

    I_SUB,

    LABEL_J,

    /**
     * LoaD Const;
     */
    LDC,

    NEW,

    PUT_FIELD,

    WRITE_INSTRUCTION,
    ;
}
