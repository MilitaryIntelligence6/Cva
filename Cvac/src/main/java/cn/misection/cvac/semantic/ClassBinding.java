package cn.misection.cvac.semantic;

//import cn.misection.cvac.ast.FrontAst;

import cn.misection.cvac.ast.type.AbstractType;

import java.util.Hashtable;

/**
 * Created by MI6 root 1/13.
 */
public final class ClassBinding
{
    /**
     * // null for non-existing base class
     */
    public String parent;
    public Hashtable<String, AbstractType> fields;
    public Hashtable<String, MethodType> methods;

    public ClassBinding(String parent)
    {
        this.parent = parent;
        this.fields = new Hashtable<>();
        this.methods = new Hashtable<>();
    }

    public ClassBinding(String parent,
                        Hashtable<String, AbstractType> fields,
                        Hashtable<String, MethodType> methods)
    {
        this.parent = parent;
        this.fields = fields;
        this.methods = methods;
    }

    public void putField(String literal, AbstractType type)
    {
        if (fields.get(literal) != null)
        {
            System.out.printf("duplicated class field: %s%n", literal);
            System.exit(1);
        }
        else
        {
            fields.put(literal, type);
        }
    }

    public void putMethod(String literal, MethodType type)
    {
        if (methods.get(literal) != null)
        {
            System.out.printf("duplicated class method: %s%n", literal);
            System.exit(1);
        }
        else
        {
            methods.put(literal, type);
        }
    }
}
