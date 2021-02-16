package cn.misection.cvac.codegen.bst.bprogram;

import cn.misection.cvac.codegen.bst.bclas.BaseClass;
import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bentry.BaseEntry;
import cn.misection.cvac.codegen.bst.bentry.GenEntry;

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
    public GenProgram(GenEntry entry, List<GenClass> classList)
    {
        super(entry, classList);
    }
}
