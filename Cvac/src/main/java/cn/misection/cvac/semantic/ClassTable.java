package cn.misection.cvac.semantic;

//import cn.misection.cvac.ast.FrontAst;

import cn.misection.cvac.ast.type.AbstractType;

import java.util.Hashtable;

/**
 * Created by MI6 root 1/13.
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
            System.out.printf("duplicated class: %s%n", c);
            System.exit(1);
        }
        else
        {
            this.table.put(c, cb);
        }
    }

    public void putFieldToClass(String c, String literal, AbstractType type)
    {
        this.table.get(c).putField(literal, type);
    }

    public void putMethodToClass(String c, String literal, MethodType type)
    {
        this.table.get(c).putMethod(literal, type);
    }

    public ClassBinding getClassBinding(String c)
    {
        return this.table.get(c);
    }

    public AbstractType getFieldType(String c, String literal)
    {
        ClassBinding cb = this.table.get(c);
        AbstractType type = cb.fields.get(literal);
        while (type == null)
        {
            if (cb.base == null)
            {
                return type;
            }

            cb = this.table.get(cb.base);
            type = cb.fields.get(literal);
        }
        return type;
    }

    public MethodType getMethodType(String c, String literal)
    {
        ClassBinding cb = this.table.get(c);
        MethodType type = cb.methods.get(literal);
        while (type == null)
        {
            if (cb.base == null)
            {
                return type;
            }

            cb = this.table.get(cb.base);
            type = cb.methods.get(literal);
        }
        return type;
    }
}
