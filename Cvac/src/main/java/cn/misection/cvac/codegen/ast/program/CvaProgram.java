package cn.misection.cvac.codegen.ast.program;

import cn.misection.cvac.codegen.ast.clas.AbstractClass;
import cn.misection.cvac.codegen.ast.entry.AbstractEntry;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaProgram
 * @Description TODO
 * @CreateTime 2021年02月14日 18:14:00
 */
public class CvaProgram extends AbstractProgram
{
    public CvaProgram(AbstractEntry entry, List<AbstractClass> classQueue)
    {
        super(entry, classQueue);
    }
}
