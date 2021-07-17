package cn.misection.cvac.codegen.bst.bprogram;

import cn.misection.cvac.codegen.bst.bclas.TargetClass;
import cn.misection.cvac.codegen.bst.bentry.TargetEntryClass;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaProgram
 * @Description TODO
 * @CreateTime 2021年02月14日 18:14:00
 */
public final class TargetProgram extends BaseProgram {
    public TargetProgram(TargetEntryClass entry, List<TargetClass> classList) {
        super(entry, classList);
    }
}
