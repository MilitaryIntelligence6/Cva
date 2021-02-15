package cn.misection.cvac.codegen.ast;

import java.util.List;

/**
 * Created by Mengxu on 2017/1/17.
 * @TODO 改这里;
 */
public class CodeGenAst
{
    public static class Type
    {
        public static abstract class T {}

        public static class ClassType extends T
        {
            private String literal;

            public ClassType(String literal)
            {
                this.setLiteral(literal);
            }

            @Override
            public String toString()
            {
                return this.getLiteral();
            }

            public String getLiteral()
            {
                return literal;
            }

            public void setLiteral(String literal)
            {
                this.literal = literal;
            }
        }

        public static class Int extends T
        {
            @Override
            public String toString()
            {
                return "@int";
            }
        }
    }

    public static class Dec
    {
        public static class DecSingle
        {
            private Type.T type;
            private String literal;

            public DecSingle(Type.T type, String literal)
            {
                this.setType(type);
                this.setLiteral(literal);
            }

            public Type.T getType()
            {
                return type;
            }

            public void setType(Type.T type)
            {
                this.type = type;
            }

            public String getLiteral()
            {
                return literal;
            }

            public void setLiteral(String literal)
            {
                this.literal = literal;
            }
        }
    }

    public static class Stm
    {
        public static abstract class T {}

        public static class Aload extends T
        {
            private int index;

            public Aload(int index)
            {
                this.setIndex(index);
            }

            public int getIndex()
            {
                return index;
            }

            public void setIndex(int index)
            {
                this.index = index;
            }
        }

        public static class Areturn extends T {}

        public static class Astore extends T
        {
            private int index;

            public Astore(int index)
            {
                this.setIndex(index);
            }

            public int getIndex()
            {
                return index;
            }

            public void setIndex(int index)
            {
                this.index = index;
            }
        }

        public static class Goto extends T
        {
            private Label label;

            public Goto(Label label)
            {
                this.setLabel(label);
            }

            public Label getLabel()
            {
                return label;
            }

            public void setLabel(Label label)
            {
                this.label = label;
            }
        }

        public static class Getfield extends T
        {
            private String fieldSpec;
            public String descriptor;

            public Getfield(String fieldSpec, String descriptor)
            {
                this.setFieldSpec(fieldSpec);
                this.descriptor = descriptor;
            }

            public String getFieldSpec()
            {
                return fieldSpec;
            }

            public void setFieldSpec(String fieldSpec)
            {
                this.fieldSpec = fieldSpec;
            }
        }

        public static class Iadd extends T {}

        public static class Ificmplt extends T
        {
            private Label label;

            public Ificmplt(Label label)
            {
                this.setLabel(label);
            }

            public Label getLabel()
            {
                return label;
            }

            public void setLabel(Label label)
            {
                this.label = label;
            }
        }

        public static class Iload extends T
        {
            private int index;

            public Iload(int index)
            {
                this.setIndex(index);
            }

            public int getIndex()
            {
                return index;
            }

            public void setIndex(int index)
            {
                this.index = index;
            }
        }

        public static class Imul extends T {}

        public static class Invokevirtual extends T
        {
            public String f;
            public String c;
            public List<Type.T> at;
            public Type.T rt;

            public Invokevirtual(String f, String c, List<Type.T> at, Type.T rt)
            {
                this.f = f;
                this.c = c;
                this.at = at;
                this.rt = rt;
            }
        }

        public static class Ireturn extends T {}

        public static class Istore extends T
        {
            private int index;

            public Istore(int index)
            {
                this.setIndex(index);
            }

            public int getIndex()
            {
                return index;
            }

            public void setIndex(int index)
            {
                this.index = index;
            }
        }

        public static class Isub extends T {}

        public static class LabelJ extends T
        {
            private Label label;

            public LabelJ(Label label)
            {
                this.setLabel(label);
            }

            public Label getLabel()
            {
                return label;
            }

            public void setLabel(Label label)
            {
                this.label = label;
            }
        }

        public static class Ldc extends T
        {
            private int integ;

            public Ldc(int integ)
            {
                this.setInteg(integ);
            }

            public int getInteg()
            {
                return integ;
            }

            public void setInteg(int integ)
            {
                this.integ = integ;
            }
        }

        public static class New extends T
        {
            private String clas;

            public New(String clas)
            {
                this.setClas(clas);
            }

            public String getClas()
            {
                return clas;
            }

            public void setClas(String clas)
            {
                this.clas = clas;
            }
        }

        public static class Write extends T {}

        public static class Putfield extends T
        {
            private String fieldSpec;
            public String descriptor;

            public Putfield(String fieldSpec, String descriptor)
            {
                this.setFieldSpec(fieldSpec);
                this.descriptor = descriptor;
            }

            public String getFieldSpec()
            {
                return fieldSpec;
            }

            public void setFieldSpec(String fieldSpec)
            {
                this.fieldSpec = fieldSpec;
            }
        }
    }

    public static class Method
    {
        public static class MethodSingle
        {
            private Type.T retType;
            private String id;
            private String classId;
            private List<Dec.DecSingle> formals;
            private List<Dec.DecSingle> locals;
            private List<Stm.T> stms;
            private int index; // number of index
            private int retExp;

            public MethodSingle(Type.T retType, String id, String classId,
                                List<Dec.DecSingle> formals,
                                List<Dec.DecSingle> locals,
                                List<Stm.T> stms, int retExp, int index)
            {
                this.setRetType(retType);
                this.setId(id);
                this.setClassId(classId);
                this.setFormals(formals);
                this.setLocals(locals);
                this.setStms(stms);
                this.setRetExp(retExp);
                this.setIndex(index);
            }

            public Type.T getRetType()
            {
                return retType;
            }

            public void setRetType(Type.T retType)
            {
                this.retType = retType;
            }

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getClassId()
            {
                return classId;
            }

            public void setClassId(String classId)
            {
                this.classId = classId;
            }

            public List<Dec.DecSingle> getFormals()
            {
                return formals;
            }

            public void setFormals(List<Dec.DecSingle> formals)
            {
                this.formals = formals;
            }

            public List<Dec.DecSingle> getLocals()
            {
                return locals;
            }

            public void setLocals(List<Dec.DecSingle> locals)
            {
                this.locals = locals;
            }

            public List<Stm.T> getStms()
            {
                return stms;
            }

            public void setStms(List<Stm.T> stms)
            {
                this.stms = stms;
            }

            public int getIndex()
            {
                return index;
            }

            public void setIndex(int index)
            {
                this.index = index;
            }

            public int getRetExp()
            {
                return retExp;
            }

            public void setRetExp(int retExp)
            {
                this.retExp = retExp;
            }
        }
    }

    public static class Class
    {
        public static class ClassSingle
        {
            private String literal;
            private String base;
            private List<Dec.DecSingle> fields;
            private List<Method.MethodSingle> methods;

            public ClassSingle(String literal, String base,
                               List<Dec.DecSingle> fields,
                               List<Method.MethodSingle> methods)
            {
                this.setLiteral(literal);
                this.setBase(base);
                this.setFields(fields);
                this.setMethods(methods);
            }

            public String getLiteral()
            {
                return literal;
            }

            public void setLiteral(String literal)
            {
                this.literal = literal;
            }

            public String getBase()
            {
                return base;
            }

            public void setBase(String base)
            {
                this.base = base;
            }

            public List<Dec.DecSingle> getFields()
            {
                return fields;
            }

            public void setFields(List<Dec.DecSingle> fields)
            {
                this.fields = fields;
            }

            public List<Method.MethodSingle> getMethods()
            {
                return methods;
            }

            public void setMethods(List<Method.MethodSingle> methods)
            {
                this.methods = methods;
            }
        }
    }

    public static class MainClass
    {
        public static class MainClassSingle
        {
            private String literal;
            private List<Stm.T> stms;

            public MainClassSingle(String literal,
                                   List<Stm.T> stms)
            {
                this.setLiteral(literal);
                this.setStms(stms);
            }

            public String getLiteral()
            {
                return literal;
            }

            public void setLiteral(String literal)
            {
                this.literal = literal;
            }

            public List<Stm.T> getStms()
            {
                return stms;
            }

            public void setStms(List<Stm.T> stms)
            {
                this.stms = stms;
            }
        }
    }

    public static class Program
    {
        public static class ProgramSingle
        {
            private MainClass.MainClassSingle mainClass;
            private List<Class.ClassSingle> classes;

            public ProgramSingle(MainClass.MainClassSingle mainClass,
                                 List<Class.ClassSingle> classes)
            {
                this.setMainClass(mainClass);
                this.setClasses(classes);
            }

            public MainClass.MainClassSingle getMainClass()
            {
                return mainClass;
            }

            public void setMainClass(MainClass.MainClassSingle mainClass)
            {
                this.mainClass = mainClass;
            }

            public List<Class.ClassSingle> getClasses()
            {
                return classes;
            }

            public void setClasses(List<Class.ClassSingle> classes)
            {
                this.classes = classes;
            }
        }
    }
}
