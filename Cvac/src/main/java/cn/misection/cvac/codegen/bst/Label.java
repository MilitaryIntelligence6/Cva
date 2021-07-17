package cn.misection.cvac.codegen.bst;

/**
 * Created by MI6 root 1/17.
 */
public final class Label {
    private static int globalLabelCount = 0;

    private final int instanceCount;

    public Label() {
        instanceCount = globalLabelCount++;
    }

    @Override
    public String toString() {
        // 这个地方是JVM汇编规范, 不能改!;
        return String.format("Label_%d", instanceCount);
    }
}
