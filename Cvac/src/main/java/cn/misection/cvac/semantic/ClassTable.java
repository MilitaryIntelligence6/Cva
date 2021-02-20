package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.type.ICvaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MI6 root 1/13.
 * @FIXME 改继承
 */
public final class ClassTable
{
    private Map<String, ClassBinding> table;

    public ClassTable()
    {
        this.table = new HashMap<>();
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

    public void putFieldToClass(String c, String literal, ICvaType type)
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

    public ICvaType getFieldType(String c, String literal)
    {
        ClassBinding cb = this.table.get(c);
        ICvaType type = cb.fields.get(literal);
        while (type == null)
        {
            if (cb.parent == null)
            {
                return type;
            }

            cb = this.table.get(cb.parent);
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
            if (cb.parent == null)
            {
                return type;
            }

            cb = this.table.get(cb.parent);
            type = cb.methods.get(literal);
        }
        return type;
    }
}
