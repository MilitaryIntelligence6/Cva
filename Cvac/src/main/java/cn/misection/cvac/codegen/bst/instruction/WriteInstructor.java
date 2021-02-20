package cn.misection.cvac.codegen.bst.instruction;

import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Write
 * @Description TODO
 * @CreateTime 2021年02月16日 01:00:00
 */
public final class WriteInstructor extends BaseInstructor
{
    private byte writeMode;

    /**
     * expr的type, 尽量早确定, 不然判定很丑, 判定时确定传入也行;
     */
    private EnumCvaType writeType;

    public WriteInstructor(byte writeMode, EnumCvaType writeType)
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

    public EnumCvaType getWriteType()
    {
        return writeType;
    }

    public void setWriteType(EnumCvaType writeType)
    {
        this.writeType = writeType;
    }
}
