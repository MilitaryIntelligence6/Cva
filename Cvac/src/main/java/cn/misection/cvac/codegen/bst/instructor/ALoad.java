package cn.misection.cvac.codegen.bst.instructor;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Aload
 * @Description load 引用类型的指令;
 * @CreateTime 2021年02月16日 00:43:00
 */
public final class ALoad extends BaseInstructor {
    private int index;

    public ALoad(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
