package cn.misection.cvac.codegen.bst.instruction;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName SLdc
 * @Description TODO
 * @CreateTime 2021年02月19日 01:04:00
 */
public final class SLdc extends BaseLdc<String>
{
    private String value;

    public SLdc(String value)
    {
        this.value = value;
    }

    @Override
    public String value()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
