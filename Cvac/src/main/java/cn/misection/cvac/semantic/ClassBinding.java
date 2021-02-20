package cn.misection.cvac.semantic;

//import cn.misection.cvac.ast.FrontAst;

import cn.misection.cvac.ast.type.ICvaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MI6 root 1/13.
 */
public final class ClassBinding
{
    private String parent;
    private Map<String, ICvaType> fields;
    private Map<String, MethodType> methods;

    public ClassBinding(String parent)
    {
        this.setParent(parent);
        this.setFields(new HashMap<>());
        this.setMethods(new HashMap<>());
    }

    public ClassBinding(String parent,
                        Map<String, ICvaType> fields,
                        Map<String, MethodType> methods)
    {
        this.setParent(parent);
        this.setFields(fields);
        this.setMethods(methods);
    }

    public void putField(String literal, ICvaType type)
    {
        if (getFields().get(literal) != null)
        {
            System.out.printf("duplicated class field: %s%n", literal);
            System.exit(1);
        }
        else
        {
            getFields().put(literal, type);
        }
    }

    public void putMethod(String literal, MethodType type)
    {
        if (getMethods().get(literal) != null)
        {
            System.out.printf("duplicated class method: %s%n", literal);
            System.exit(1);
        }
        else
        {
            getMethods().put(literal, type);
        }
    }

    /**
     * // null for non-existing base class
     */
    public String getParent()
    {
        return parent;
    }

    public void setParent(String parent)
    {
        this.parent = parent;
    }

    public Map<String, ICvaType> getFields()
    {
        return fields;
    }

    public void setFields(Map<String, ICvaType> fields)
    {
        this.fields = fields;
    }

    public Map<String, MethodType> getMethods()
    {
        return methods;
    }

    public void setMethods(Map<String, MethodType> methods)
    {
        this.methods = methods;
    }
}
