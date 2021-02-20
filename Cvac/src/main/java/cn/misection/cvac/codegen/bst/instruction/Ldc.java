package cn.misection.cvac.codegen.bst.instruction;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName BaseLdc
 * @Description TODO
 * @CreateTime 2021年02月19日 01:06:00
 */
public final class Ldc<T> extends BaseInstructor
{
    private T value;

    public Ldc(T value)
    {
        this.value = value;
    }

    public T value()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }
}
