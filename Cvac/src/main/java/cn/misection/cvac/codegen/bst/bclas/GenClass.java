package cn.misection.cvac.codegen.bst.bclas;

import cn.misection.cvac.codegen.bst.bdecl.BaseDeclaration;
import cn.misection.cvac.codegen.bst.bmethod.BaseMethod;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:57:00
 */
public class GenClass extends BaseClass
{
    private String literal;

    private String parent;

    private List<BaseDeclaration> fieldList;

    private List<BaseMethod> methodList;

    public GenClass(String literal, String parent, List<BaseDeclaration> fieldList, List<BaseMethod> methodList)
    {
        this.literal = literal;
        this.parent = parent;
        this.fieldList = fieldList;
        this.methodList = methodList;
    }

    public String getLiteral()
    {
        return literal;
    }

    public String getParent()
    {
        return parent;
    }

    public List<BaseDeclaration> getFieldList()
    {
        return fieldList;
    }

    public List<BaseMethod> getMethodList()
    {
        return methodList;
    }
}
