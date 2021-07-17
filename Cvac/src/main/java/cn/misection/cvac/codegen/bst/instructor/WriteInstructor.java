package cn.misection.cvac.codegen.bst.instructor;

import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;
import cn.misection.cvac.codegen.bst.instructor.write.EnumWriteMode;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Write
 * @Description TODO
 * @CreateTime 2021年02月16日 01:00:00
 */
public final class WriteInstructor extends BaseInstructor {
    private EnumWriteMode writeMode;

    /**
     * expr的type, 尽量早确定, 不然判定很丑, 判定时确定传入也行;
     */
    private EnumTargetType writeType;

    public WriteInstructor(EnumWriteMode writeMode, EnumTargetType writeType) {
        this.writeMode = writeMode;
        this.writeType = writeType;
    }

    public String requireInvoke() {
        return String.format("%s(%s)V", writeMode.toInst(), writeType.toInst());
    }

    public EnumWriteMode getWriteMode() {
        return writeMode;
    }

    public void setWriteMode(EnumWriteMode writeMode) {
        this.writeMode = writeMode;
    }

    public EnumTargetType getWriteType() {
        return writeType;
    }

    public void setWriteType(EnumTargetType writeType) {
        this.writeType = writeType;
    }
}
