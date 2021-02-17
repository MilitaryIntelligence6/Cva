package cn.misection.cvac.ast.clas;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.method.AbstractMethod;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:57:00
 */
public final class CvaClass extends AbstractClass
{
    private final String literal;

    private final String parent;

    private final List<AbstractDeclaration> fieldList;

    private final List<AbstractMethod> methodList;

    public CvaClass(String literal, String parent, List<AbstractDeclaration> fieldList, List<AbstractMethod> methodList)
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

    public List<AbstractDeclaration> getFieldList()
    {
        return fieldList;
    }

    public List<AbstractMethod> getMethodList()
    {
        return methodList;
    }
}
