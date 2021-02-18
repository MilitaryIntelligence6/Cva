package cn.misection.cvac.codegen.bst.instruction;

import cn.misection.cvac.codegen.bst.btype.BaseType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Write
 * @Description TODO
 * @CreateTime 2021年02月16日 01:00:00
 */
public final class WriteInstruction extends BaseInstruction
{
    public static final byte WRITE = 0;

    public static final byte WRITELN = 1;

    public static final byte WRITE_FORMAT = 2;

    private byte writelnMode;

    private BaseType writeType;

    public WriteInstruction()
    {

    }

    public WriteInstruction(byte writelnMode, BaseType writeType)
    {
        this.writelnMode = writelnMode;
        this.writeType = writeType;
    }

    public static class Builder
    {

    }
}
