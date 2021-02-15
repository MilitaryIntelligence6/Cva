package cn.misection.cvac.codegen.bst.bprogram;

import cn.misection.cvac.codegen.bst.clas.BaseClass;
import cn.misection.cvac.codegen.bst.bentry.BaseEntry;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaProgram
 * @Description TODO
 * @CreateTime 2021年02月14日 17:56:00
 */
public abstract class BaseProgram implements IProgram
{
    protected BaseEntry entry;

    protected List<BaseClass> classList;

    protected BaseProgram(BaseEntry entry, List<BaseClass> classList)
    {
        this.entry = entry;
        this.classList = classList;
    }

    public BaseEntry getEntry()
    {
        return entry;
    }

    public List<BaseClass> getClassList()
    {
        return classList;
    }

    public void setEntry(BaseEntry entry)
    {
        this.entry = entry;
    }

    public void setClassList(List<BaseClass> classList)
    {
        this.classList = classList;
    }
}
