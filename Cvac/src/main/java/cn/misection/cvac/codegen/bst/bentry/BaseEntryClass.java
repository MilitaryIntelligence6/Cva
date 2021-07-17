package cn.misection.cvac.codegen.bst.bentry;

import cn.misection.cvac.codegen.bst.instructor.IInstructor;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMain
 * @Description main 方法入口;
 * @CreateTime 2021年02月14日 17:54:00
 */
public abstract class BaseEntryClass implements IEntryClass {
    protected String name;

    protected List<IInstructor> statementList;

    protected BaseEntryClass(String name, List<IInstructor> statementList) {
        this.name = name;
        this.statementList = statementList;
    }

    public String getName() {
        return name;
    }

    public List<IInstructor> getStatementList() {
        return statementList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatementList(List<IInstructor> statementList) {
        this.statementList = statementList;
    }
}
