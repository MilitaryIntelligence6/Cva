package cn.misection.cvac.codegen.bst.instructor.write;

import cn.misection.cvac.codegen.bst.instructor.Instructable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName WriteMode
 * @Description TODO
 * @CreateTime 2021年02月24日 22:07:00
 */
public enum EnumWriteMode implements Instructable
{
    /**
     * 空, ln, f;
     */
    PRINT("print"),

    PRINT_LINE("println"),

    PRINT_FORMAT("printf"),
    ;

    private final String instruction;

    EnumWriteMode(String instruction)
    {
        this.instruction = instruction;
    }

    @Override
    public String toInst()
    {
        return instruction;
    }
}
