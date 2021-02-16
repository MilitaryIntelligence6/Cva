package cn.misection.cvac.codegen.bst.bstatement;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName PutField
 * @Description TODO
 * @CreateTime 2021年02月16日 01:01:00
 */
public final class PutField extends BaseStatement
{
    private String fieldSpec;

    private String descriptor;

    public PutField(String fieldSpec, String descriptor)
    {
        this.fieldSpec = fieldSpec;
        this.descriptor = descriptor;
    }

    public String getFieldSpec()
    {
        return fieldSpec;
    }

    public void setFieldSpec(String fieldSpec)
    {
        this.fieldSpec = fieldSpec;
    }

    public String getDescriptor()
    {
        return descriptor;
    }

    public void setDescriptor(String descriptor)
    {
        this.descriptor = descriptor;
    }
}
