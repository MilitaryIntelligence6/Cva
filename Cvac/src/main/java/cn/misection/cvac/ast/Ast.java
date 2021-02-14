package cn.misection.cvac.ast;

/**
 * Created by Mengxu on 2017/1/7.
 */
public class Ast
{
    // Type
    public static class Type
    {
        public static abstract class T
        {}

        public static class Boolean extends T
        {
            public Boolean() {}

            @Override
            public String toString()
            {
                return "@boolean";
            }
        }

        public static class ClassType extends T
        {
            public String id;

            public ClassType(String id)
            {
                this.id = id;
            }

            @Override
            public String toString()
            {
                return this.id;
            }
        }

        public static class Int extends T
        {
            public Int() {}

            @Override
            public String toString()
            {
                return "@int";
            }
        }

    }

    // Dec
    public static class Dec
    {
        public static abstract class T
        {
            public int lineNum;
        }

        public static class DecSingle extends T
        {
            public Type.T type;
            public String id;

            public DecSingle(Type.T type, String id, int lineNum)
            {
                this.type = type;
                this.id = id;
                this.lineNum = lineNum;
            }
        }
    }

    // Expression
    public static class Exp
    {
        public static abstract class T
        {
            public int lineNum;
        }

        public static class Add extends T
        {
            public T left, right;

            public Add(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        public static class And extends T
        {
            public T left, right;

            public And(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        public static class Call extends T
        {
            public T exp;
            public String id;
            public java.util.LinkedList<T> args;
            public String type; // type of first field "exp"
            public java.util.LinkedList<Type.T> at; // arg's type
            public Type.T rt;

            public Call(T exp, String id, java.util.LinkedList<T> args, int lineNum)
            {
                this.exp = exp;
                this.id = id;
                this.args = args;
                this.type = null;
                this.lineNum = lineNum;
            }
        }

        public static class False extends T
        {
            public False(int lineNum)
            {
                this.lineNum = lineNum;
            }
        }

        public static class Id extends T
        {
            public String id; // name of the id
            public Type.T type; // type of the id
            public boolean isField; // whether or not a field

            public Id(String id, int lineNum)
            {
                this.id = id;
                this.lineNum = lineNum;
            }

            public Id(String id, Type.T type, boolean isField, int lineNum)
            {
                this.id = id;
                this.type = type;
                this.isField = isField;
                this.lineNum = lineNum;
            }
        }

        public static class LT extends T
        {
            public T left, right;

            public LT(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        public static class NewObject extends T
        {
            public String id;

            public NewObject(String id, int lineNum)
            {
                this.id = id;
                this.lineNum = lineNum;
            }
        }

        public static class Not extends T
        {
            public T exp;

            public Not(T exp, int lineNum)
            {
                this.exp = exp;
                this.lineNum = lineNum;
            }
        }

        public static class Num extends T
        {
            public int num;

            public Num(int num, int lineNum)
            {
                this.num = num;
                this.lineNum = lineNum;
            }
        }

        public static class Sub extends T
        {
            public T left, right;

            public Sub(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        public static class This extends T
        {
            public This(int lineNum)
            {
                this.lineNum = lineNum;
            }
        }

        public static class Times extends T
        {
            public T left, right;

            public Times(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        public static class True extends T
        {
            public True(int lineNum)
            {
                this.lineNum = lineNum;
            }
        }
    }

    // Statement
    public static class Stm
    {
        public static abstract class T
        {
            public int lineNum;
        }

        public static class Assign extends T
        {
            public String id;
            public Exp.T exp;
            public Type.T type; // type of the id

            public Assign(String id, Exp.T exp, int lineNum)
            {
                this.id = id;
                this.exp = exp;
                this.type = null;
                this.lineNum = lineNum;
            }
        }

        public static class Block extends T
        {
            public java.util.LinkedList<T> stms;

            public Block(java.util.LinkedList<T> stms, int lineNum)
            {
                this.stms = stms;
                this.lineNum = lineNum;
            }
        }

        public static class If extends T
        {
            public Exp.T condition;
            public T thenStm, elseStm;

            public If(Exp.T condition, T thenStm, T elseStm, int lineNum)
            {
                this.condition = condition;
                this.thenStm = thenStm;
                this.elseStm = elseStm;
                this.lineNum = lineNum;
            }
        }

        public static class Write extends T
        {
            public Exp.T exp;

            public Write(Exp.T exp, int lineNum)
            {
                this.exp = exp;
                this.lineNum = lineNum;
            }
        }

        public static class While extends T
        {
            public Exp.T condition;
            public T body;

            public While(Exp.T condition, T body, int lineNum)
            {
                this.condition = condition;
                this.body = body;
                this.lineNum = lineNum;
            }
        }
    }

    public static class Method
    {
        public static abstract class T
        {}

        public static class MethodSingle extends T
        {
            public Type.T retType;
            public String id;
            public java.util.LinkedList<Dec.T> formals;
            public java.util.LinkedList<Dec.T> locals;
            public java.util.LinkedList<Stm.T> stms;
            public Exp.T retExp;

            public MethodSingle(Type.T retType, String id,
                                java.util.LinkedList<Dec.T> formals,
                                java.util.LinkedList<Dec.T> locals,
                                java.util.LinkedList<Stm.T> stms,
                                Exp.T retExp)
            {
                this.retType = retType;
                this.id = id;
                this.formals = formals;
                this.locals = locals;
                this.stms = stms;
                this.retExp = retExp;
            }
        }
    }

    public static class Class
    {
        public static abstract class T
        {}

        public static class ClassSingle extends T
        {
            public String id;
            public String base; // null for no-base
            public java.util.LinkedList<Dec.T> fields;
            public java.util.LinkedList<Method.T> methods;

            public ClassSingle(String id, String base,
                               java.util.LinkedList<Dec.T> fields,
                               java.util.LinkedList<Method.T> methods)
            {
                this.id = id;
                this.base = base;
                this.fields = fields;
                this.methods = methods;
            }
        }
    }

    public static class MainClass
    {
        public static abstract class T
        {}

        public static class MainClassSingle extends T
        {
            public String id;
            public Stm.T stm;

            public MainClassSingle(String id, Stm.T stm)
            {
                this.id = id;
                this.stm = stm;
            }
        }
    }

    public static class Program
    {
        public static abstract class T
        {}

        public static class ProgramSingle extends T
        {
            public MainClass.T mainClass;
            public java.util.LinkedList<Class.T> classes;

            public ProgramSingle(MainClass.T mainClass,
                                 java.util.LinkedList<Class.T> classes)
            {
                this.mainClass = mainClass;
                this.classes = classes;
            }
        }
    }
}
