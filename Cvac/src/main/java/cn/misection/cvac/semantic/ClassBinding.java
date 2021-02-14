package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.Ast;

import java.util.Hashtable;

/**
 * Created by Mengxu on 2017/1/13.
 */
public class ClassBinding
{
    public String base; // null for non-existing base class
    public Hashtable<String, Ast.Type.T> fields;
    public Hashtable<String, MethodType> methods;

    public ClassBinding(String base)
    {
        this.base = base;
        this.fields = new Hashtable<>();
        this.methods = new Hashtable<>();
    }

    public ClassBinding(String base,
                        Hashtable<String, Ast.Type.T> fields,
                        Hashtable<String, MethodType> methods)
    {
        this.base = base;
        this.fields = fields;
        this.methods = methods;
    }

    public void putField(String id, Ast.Type.T type)
    {
        if (fields.get(id) != null)
        {
            System.out.println("duplicated class field: " + id);
            System.exit(1);
        } else fields.put(id, type);
    }

    public void putMethod(String id, MethodType type)
    {
        if (methods.get(id) != null)
        {
            System.out.println("duplicated class method: " + id);
            System.exit(1);
        } else methods.put(id, type);
    }
}
