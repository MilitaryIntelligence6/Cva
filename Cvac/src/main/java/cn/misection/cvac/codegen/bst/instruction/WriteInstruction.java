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
    private byte writeMode;

    /**
     * expr的type, 尽量早确定, 不然判定很丑, 判定时确定传入也行;
     */
    private byte writeType;

    public WriteInstruction(byte writeMode, byte writeType)
    {
        this.writeMode = writeMode;
        this.writeType = writeType;
    }

    public byte getWriteMode()
    {
        return writeMode;
    }

    public void setWriteMode(byte writeMode)
    {
        this.writeMode = writeMode;
    }

    public byte getWriteType()
    {
        return writeType;
    }

    public void setWriteType(byte writeType)
    {
        this.writeType = writeType;
    }
}
