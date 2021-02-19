package cn.misection.cvac.ast.program;

import cn.misection.cvac.ast.clas.AbstractCvaClass;
import cn.misection.cvac.ast.entry.AbstractEntryClass;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaProgram
 * @Description TODO
 * @CreateTime 2021年02月14日 17:56:00
 */
public abstract class AbstractProgram implements IProgram
{
    protected AbstractEntryClass entry;

    protected List<AbstractCvaClass> classList;

    protected AbstractProgram(AbstractEntryClass entry, List<AbstractCvaClass> classList)
    {
        this.entry = entry;
        this.classList = classList;
    }

    public AbstractEntryClass getEntry()
    {
        return entry;
    }

    public List<AbstractCvaClass> getClassList()
    {
        return classList;
    }
}
