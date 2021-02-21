package cn.misection.cvac.codegen.bst.instructor;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName New
 * @Description TODO
 * @CreateTime 2021年02月16日 01:00:00
 */
public final class New extends BaseInstructor
{
    private String newClassName;

    public New(String newClassName)
    {
        this.newClassName = newClassName;
    }

    public String getNewClassName()
    {
        return newClassName;
    }

    public void setNewClassName(String newClassName)
    {
        this.newClassName = newClassName;
    }
}
