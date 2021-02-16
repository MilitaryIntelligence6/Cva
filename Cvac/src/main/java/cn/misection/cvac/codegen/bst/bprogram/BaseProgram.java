package cn.misection.cvac.codegen.bst.bprogram;

import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bentry.BaseEntry;
import cn.misection.cvac.codegen.bst.bentry.GenEntry;

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
    protected GenEntry entry;

    protected List<GenClass> classList;

    protected BaseProgram(GenEntry entry, List<GenClass> classList)
    {
        this.entry = entry;
        this.classList = classList;
    }

    public GenEntry getEntry()
    {
        return entry;
    }

    public void setEntry(GenEntry entry)
    {
        this.entry = entry;
    }

    public List<GenClass> getClassList()
    {
        return classList;
    }

    public void setClassList(List<GenClass> classList)
    {
        this.classList = classList;
    }
}
