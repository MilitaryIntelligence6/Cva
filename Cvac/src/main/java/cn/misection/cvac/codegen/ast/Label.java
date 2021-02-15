package cn.misection.cvac.codegen.ast;

/**
 * Created by Mengxu on 2017/1/17.
 */
public class Label
{
    private int i;
    private static int count = 0;

    public Label()
    {
        i = count++;
    }

    @Override
    public String toString()
    {
        // 这个地方是JVM汇编规范, 不能改!;
        return String.format("Label_%d", i);
    }
}
