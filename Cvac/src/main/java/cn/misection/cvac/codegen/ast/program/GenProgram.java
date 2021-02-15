package cn.misection.cvac.codegen.ast.program;

import cn.misection.cvac.codegen.ast.clas.BaseClass;
import cn.misection.cvac.codegen.ast.entry.BaseEntry;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaProgram
 * @Description TODO
 * @CreateTime 2021年02月14日 18:14:00
 */
public class GenProgram extends BaseProgram
{
    public GenProgram(BaseEntry entry, List<BaseClass> classQueue)
    {
        super(entry, classQueue);
    }
}
