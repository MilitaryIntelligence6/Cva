package cn.misection.cvac.ast;

/**
 * Created by Mengxu on 2017/1/7.
 */
public class FrontAst
{
    // Type
    public static class Type
    {
        public static abstract class T
        {}

        public static class CvaBoolean extends T
        {
            public CvaBoolean() {}

            @Override
            public String toString()
            {
                return "@boolean";
            }
        }

        public static class CvaClassType extends T
        {
            public String literal;

            public CvaClassType(String literal)
            {
                this.literal = literal;
            }

            @Override
            public String toString()
            {
                return this.literal;
            }
        }

        public static class CvaInt extends T
        {
            public CvaInt() {}

            @Override
            public String toString()
            {
                return "@int";
            }
        }

    }

    // Dec
    public static class Decl
    {
        public static abstract class T
        {
            public int lineNum;
        }

        public static class CvaDeclaration extends T
        {
            public Type.T type;
            public String literal;

            public CvaDeclaration(Type.T type, String literal, int lineNum)
            {
                this.type = type;
                this.literal = literal;
                this.lineNum = lineNum;
            }
        }
    }

    // Expression
    public static class Expr
    {
        public static abstract class T
        {
            public int lineNum;
        }

        public static class CvaAddExpr extends T
        {
            public T left, right;

            public CvaAddExpr(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        public static class CvaAndAndExpr extends T
        {
            public T left, right;

            public CvaAndAndExpr(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        /**
         * 原来call;
         */
        public static class CvaCallExpr extends T
        {
            public T exp;
            public String literal;
            public java.util.LinkedList<T> args;
            public String type; // type of first field "exp"
            public java.util.LinkedList<Type.T> at; // arg's type
            public Type.T rt;

            public CvaCallExpr(T exp, String literal, java.util.LinkedList<T> args, int lineNum)
            {
                this.exp = exp;
                this.literal = literal;
                this.args = args;
                this.type = null;
                this.lineNum = lineNum;
            }
        }

        public static class CvaFalseExpr extends T
        {
            public CvaFalseExpr(int lineNum)
            {
                this.lineNum = lineNum;
            }
        }

        public static class CvaIdentifier extends T
        {
            public String literal; // name of the id
            public Type.T type; // type of the id
            public boolean isField; // whether or not a field

            public CvaIdentifier(String literal, int lineNum)
            {
                this.literal = literal;
                this.lineNum = lineNum;
            }

            public CvaIdentifier(String literal, Type.T type, boolean isField, int lineNum)
            {
                this.literal = literal;
                this.type = type;
                this.isField = isField;
                this.lineNum = lineNum;
            }
        }

        public static class CvaLTExpr extends T
        {
            public T left, right;

            public CvaLTExpr(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        public static class CvaNewExpr extends T
        {
            public String literal;

            public CvaNewExpr(String literal, int lineNum)
            {
                this.literal = literal;
                this.lineNum = lineNum;
            }
        }

        public static class CvaNegateExpr extends T
        {
            public T expr;

            public CvaNegateExpr(T expr, int lineNum)
            {
                this.expr = expr;
                this.lineNum = lineNum;
            }
        }

        public static class CvaNumberInt extends T
        {
            public int value;

            public CvaNumberInt(int value, int lineNum)
            {
                this.value = value;
                this.lineNum = lineNum;
            }
        }

        public static class CvaSubExpr extends T
        {
            public T left, right;

            public CvaSubExpr(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        public static class CvaThisExpr extends T
        {
            public CvaThisExpr(int lineNum)
            {
                this.lineNum = lineNum;
            }
        }

        public static class CvaMuliExpr extends T
        {
            public T left, right;

            public CvaMuliExpr(T left, T right, int lineNum)
            {
                this.left = left;
                this.right = right;
                this.lineNum = lineNum;
            }
        }

        public static class CvaTrueExpr extends T
        {
            public CvaTrueExpr(int lineNum)
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

        public static class CvaAssign extends T
        {
            public String id;
            public Expr.T exp;
            public Type.T type; // type of the id

            public CvaAssign(String id, Expr.T exp, int lineNum)
            {
                this.id = id;
                this.exp = exp;
                this.type = null;
                this.lineNum = lineNum;
            }
        }

        public static class CvaBlock extends T
        {
            public java.util.LinkedList<T> stms;

            public CvaBlock(java.util.LinkedList<T> stms, int lineNum)
            {
                this.stms = stms;
                this.lineNum = lineNum;
            }
        }

        public static class CvaIfStatement extends T
        {
            public Expr.T condition;
            public T thenStm, elseStm;

            public CvaIfStatement(Expr.T condition, T thenStm, T elseStm, int lineNum)
            {
                this.condition = condition;
                this.thenStm = thenStm;
                this.elseStm = elseStm;
                this.lineNum = lineNum;
            }
        }

        public static class CvaWriteOperation extends T
        {
            public Expr.T exp;

            public CvaWriteOperation(Expr.T exp, int lineNum)
            {
                this.exp = exp;
                this.lineNum = lineNum;
            }
        }

        public static class CvaWhileStatement extends T
        {
            public Expr.T condition;
            public T body;

            public CvaWhileStatement(Expr.T condition, T body, int lineNum)
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

        public static class CvaMethod extends T
        {
            public Type.T retType;
            public String literal;
            public java.util.LinkedList<Decl.T> formals;
            public java.util.LinkedList<Decl.T> locals;
            public java.util.LinkedList<Stm.T> stms;
            public Expr.T retExp;

            public CvaMethod(Type.T retType, String literal,
                             java.util.LinkedList<Decl.T> formals,
                             java.util.LinkedList<Decl.T> locals,
                             java.util.LinkedList<Stm.T> stms,
                             Expr.T retExp)
            {
                this.retType = retType;
                this.literal = literal;
                this.formals = formals;
                this.locals = locals;
                this.stms = stms;
                this.retExp = retExp;
            }
        }
    }

    public static class Clas
    {
        public static abstract class T
        {}

        public static class CvaClass extends T
        {
            public String literal;
            public String parent; // null for no-base
            public java.util.LinkedList<Decl.T> fields;
            public java.util.LinkedList<Method.T> methods;

            public CvaClass(String literal, String parent,
                            java.util.LinkedList<Decl.T> fields,
                            java.util.LinkedList<Method.T> methods)
            {
                this.literal = literal;
                this.parent = parent;
                this.fields = fields;
                this.methods = methods;
            }
        }
    }

    public static class MainClass
    {
        public static abstract class T
        {}

        public static class CvaEntry extends T
        {
            public String id;
            public Stm.T stm;

            public CvaEntry(String id, Stm.T stm)
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

        public static class CvaProgram extends T
        {
            public MainClass.T mainClass;
            public java.util.LinkedList<Clas.T> classes;

            public CvaProgram(MainClass.T mainClass,
                              java.util.LinkedList<Clas.T> classes)
            {
                this.mainClass = mainClass;
                this.classes = classes;
            }
        }
    }
}
