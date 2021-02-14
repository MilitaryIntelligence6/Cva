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
 * @CreateTime 2021年02月14日 17:56:00
 */
public abstract class AbstractProgram implements IProgram
{
    protected AbstractEntry entry;

    protected Queue<AbstractClass> classQueue;

    protected AbstractProgram(AbstractEntry entry, Queue<AbstractClass> classQueue)
    {
        this.entry = entry;
        this.classQueue = classQueue;
    }

    public AbstractEntry getEntry()
    {
        return entry;
    }

    public Queue<AbstractClass> getClassQueue()
    {
        return classQueue;
    }
}
