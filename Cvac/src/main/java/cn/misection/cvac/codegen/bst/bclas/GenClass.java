package cn.misection.cvac.codegen.bst.bclas;

import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.bmethod.GenMethod;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:57:00
 */
public final class GenClass extends BaseClass
{
    private String literal;

    private String parent;

    private List<GenDeclaration> fieldList;

    private List<GenMethod> methodList;

    public GenClass(String literal, String parent, List<GenDeclaration> fieldList, List<GenMethod> methodList)
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

    public void setLiteral(String literal)
    {
        this.literal = literal;
    }

    public String getParent()
    {
        return parent;
    }

    public void setParent(String parent)
    {
        this.parent = parent;
    }

    public List<GenDeclaration> getFieldList()
    {
        return fieldList;
    }

    public void setFieldList(List<GenDeclaration> fieldList)
    {
        this.fieldList = fieldList;
    }

    public List<GenMethod> getMethodList()
    {
        return methodList;
    }

    public void setMethodList(List<GenMethod> methodList)
    {
        this.methodList = methodList;
    }
}
