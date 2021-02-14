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
        return "Label_" + i;
    }
}
