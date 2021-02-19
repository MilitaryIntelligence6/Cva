package cn.misection.cvac.ast.program;

import cn.misection.cvac.ast.clas.AbstractCvaClass;
import cn.misection.cvac.ast.entry.AbstractEntryClass;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaProgram
 * @Description TODO
 * @CreateTime 2021年02月14日 18:14:00
 */
public final class CvaProgram extends AbstractProgram
{
    public CvaProgram(AbstractEntryClass entry, List<AbstractCvaClass> classQueue)
    {
        super(entry, classQueue);
    }
}
