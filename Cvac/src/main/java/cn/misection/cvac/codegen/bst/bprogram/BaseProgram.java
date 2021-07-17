package cn.misection.cvac.codegen.bst.bprogram;

import cn.misection.cvac.codegen.bst.bclas.TargetClass;
import cn.misection.cvac.codegen.bst.bentry.TargetEntryClass;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaProgram
 * @Description TODO
 * @CreateTime 2021年02月14日 17:56:00
 */
public abstract class BaseProgram implements IProgram {
    protected TargetEntryClass entryClass;

    protected List<TargetClass> classList;

    protected BaseProgram(TargetEntryClass entryClass, List<TargetClass> classList) {
        this.entryClass = entryClass;
        this.classList = classList;
    }

    public TargetEntryClass getEntryClass() {
        return entryClass;
    }

    public void setEntryClass(TargetEntryClass entryClass) {
        this.entryClass = entryClass;
    }

    public List<TargetClass> getClassList() {
        return classList;
    }

    public void setClassList(List<TargetClass> classList) {
        this.classList = classList;
    }
}
