package cn.misection.cvac.semantic;

//import cn.misection.cvac.ast.FrontAst;

import cn.misection.cvac.ast.type.AbstractType;

import java.util.Hashtable;

/**
 * Created by Mengxu on 2017/1/13.
 */
public class ClassBinding
{
    /**
     * // null for non-existing base class
     */
    public String base;
    public Hashtable<String, AbstractType> fields;
    public Hashtable<String, MethodType> methods;

    public ClassBinding(String base)
    {
        this.base = base;
        this.fields = new Hashtable<>();
        this.methods = new Hashtable<>();
    }

    public ClassBinding(String base,
                        Hashtable<String, AbstractType> fields,
                        Hashtable<String, MethodType> methods)
    {
        this.base = base;
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
