package cn.misection.cvac.codegen.bst.instruction;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName GetField
 * @Description TODO
 * @CreateTime 2021年02月16日 00:49:00
 */
public final class GetField extends BaseInstructor
{
    private String fieldSpec;

    private String descriptor;

    public GetField(String fieldSpec, String descriptor)
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
