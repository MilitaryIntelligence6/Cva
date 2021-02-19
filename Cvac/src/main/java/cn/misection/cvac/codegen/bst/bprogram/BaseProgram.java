package cn.misection.cvac.codegen.bst.bprogram;

import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bentry.GenEntryClass;

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
    protected GenEntryClass entryClass;

    protected List<GenClass> classList;

    protected BaseProgram(GenEntryClass entryClass, List<GenClass> classList)
    {
        this.entryClass = entryClass;
        this.classList = classList;
    }

    public GenEntryClass getEntryClass()
    {
        return entryClass;
    }

    public void setEntryClass(GenEntryClass entryClass)
    {
        this.entryClass = entryClass;
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
