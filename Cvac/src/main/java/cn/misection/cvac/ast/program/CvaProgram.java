package cn.misection.cvac.ast.program;

import cn.misection.cvac.ast.clas.AbstractClass;
import cn.misection.cvac.ast.entry.AbstractEntry;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaProgram
 * @Description TODO
 * @CreateTime 2021年02月14日 18:14:00
 */
public class CvaProgram extends AbstractProgram
{
    public CvaProgram(AbstractEntry entry, Queue<AbstractClass> classQueue)
    {
        super(entry, classQueue);
    }
}
