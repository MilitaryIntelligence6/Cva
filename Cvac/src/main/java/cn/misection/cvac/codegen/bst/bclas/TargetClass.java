package cn.misection.cvac.codegen.bst.bclas;

import cn.misection.cvac.codegen.bst.bdecl.TargetDeclaration;
import cn.misection.cvac.codegen.bst.bmethod.TargetMethod;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:57:00
 */
public final class TargetClass extends BaseClass {
    private String className;

    private String parent;

    private List<TargetDeclaration> fieldList;

    private List<TargetMethod> methodList;

    public TargetClass(String className, String parent, List<TargetDeclaration> fieldList,
                       List<TargetMethod> methodList) {
        this.className = className;
        this.parent = parent;
        this.fieldList = fieldList;
        this.methodList = methodList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<TargetDeclaration> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<TargetDeclaration> fieldList) {
        this.fieldList = fieldList;
    }

    public List<TargetMethod> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<TargetMethod> methodList) {
        this.methodList = methodList;
    }
}
