package cn.misection.cvac.ast.clas;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.method.AbstractMethod;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 17:54:00
 */
public abstract class AbstractCvaClass implements ICvaClass
{
    protected final String name;

    protected final String parent;

    protected final List<AbstractDeclaration> fieldList;

    protected final List<AbstractMethod> methodList;

    protected AbstractCvaClass()
    {
        this.name = null;
        this.parent = null;
        this.fieldList = null;
        this.methodList = null;
    }

    public AbstractCvaClass(String name,
                            String parent,
                            List<AbstractDeclaration> fieldList,
                            List<AbstractMethod> methodList)
    {
        this.name = name;
        this.parent = parent;
        this.fieldList = fieldList;
        this.methodList = methodList;
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String parent()
    {
        return parent;
    }

    @Override
    public List<AbstractDeclaration> getFieldList()
    {
        return fieldList;
    }

    @Override
    public List<AbstractMethod> getMethodList()
    {
        return methodList;
    }
}
