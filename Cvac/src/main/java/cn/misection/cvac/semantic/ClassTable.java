package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.Ast;

import java.util.Hashtable;

/**
 * Created by Mengxu on 2017/1/13.
 */
public class ClassTable
{
    private Hashtable<String, ClassBinding> table;

    public ClassTable()
    {
        this.table = new Hashtable<>();
    }

    public void putClassBinding(String c, ClassBinding cb)
    {
        if (this.table.get(c) != null)
        {
            System.out.println("duplicated class: " + c);
            System.exit(1);
        } else this.table.put(c, cb);
    }

    public void putFieldToClass(String c, String id, Ast.Type.T type)
    {
        this.table.get(c).putField(id, type);
    }

    public void putMethodToClass(String c, String id, MethodType type)
    {
        this.table.get(c).putMethod(id, type);
    }

    public ClassBinding getClassBinding(String c)
    {
        return this.table.get(c);
    }

    public Ast.Type.T getFieldType(String c, String id)
    {
        ClassBinding cb = this.table.get(c);
        Ast.Type.T type = cb.fields.get(id);
        while (type == null)
        {
            if (cb.base == null)
                return type;

            cb = this.table.get(cb.base);
            type = cb.fields.get(id);
        }
        return type;
    }

    public MethodType getMethodType(String c, String id)
    {
        ClassBinding cb = this.table.get(c);
        MethodType type = cb.methods.get(id);
        while (type == null)
        {
            if (cb.base == null)
                return type;

            cb = this.table.get(cb.base);
            type = cb.methods.get(id);
        }
        return type;
    }
}
