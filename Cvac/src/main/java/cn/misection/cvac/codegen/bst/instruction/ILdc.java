package cn.misection.cvac.codegen.bst.instruction;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Ldc
 * @Description TODO
 * @CreateTime 2021年02月16日 00:59:00
 */
public final class ILdc extends BaseLdc<Integer>
{
    private Integer value;

    public ILdc(Integer value)
    {
        this.value = value;
    }

    @Override
    public Integer value()
    {
        return value;
    }

    public void setValue(Integer value)
    {
        this.value = value;
    }
}
