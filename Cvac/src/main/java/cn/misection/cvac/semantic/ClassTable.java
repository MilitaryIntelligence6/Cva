package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.type.ICvaType;

import java.util.HashMap;

/**
 * @author MI6 root
 * @FIXME 改继承
 */
public final class ClassTable extends HashMap<String, ClassBinding>
{
    public ClassTable()
    {
        super();
    }

    public void putClassBinding(String className, ClassBinding bind)
    {
        if (this.get(className) != null)
        {
            System.out.printf("duplicated class: %s%n", className);
            System.exit(1);
        }
        else
        {
            this.put(className, bind);
        }
    }

    public void putFieldToClass(String className, String literal, ICvaType type)
    {
        this.get(className).putField(literal, type);
    }

    public void putMethodToClass(String className, String literal, MethodType type)
    {
        this.get(className).putMethod(literal, type);
    }

    public ClassBinding getClassBinding(String className)
    {
        return this.get(className);
    }

    public ICvaType getFieldType(String className, String literal)
    {
        ClassBinding bind = this.get(className);
        ICvaType type = bind.getFields().get(literal);
        while (type == null)
        {
            if (bind.getParent() == null)
            {
                return type;
            }

            bind = this.get(bind.getParent());
            type = bind.getFields().get(literal);
        }
        return type;
    }

    public MethodType getMethodType(String className, String literal)
    {
        ClassBinding bind = this.get(className);
        MethodType type = bind.getMethods().get(literal);
        while (type == null)
        {
            if (bind.getParent() == null)
            {
                return type;
            }
            bind = this.get(bind.getParent());
            type = bind.getMethods().get(literal);
        }
        return type;
    }
}
