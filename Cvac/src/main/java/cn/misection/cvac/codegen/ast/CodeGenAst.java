package cn.misection.cvac.codegen.ast;

import java.util.List;

/**
 * Created by Mengxu on 2017/1/17.
 *
 * @TODO 改这里;
 */
public class CodeGenAst
{
    public static class Type
    {
        public static abstract class T
        {
        }

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
        public static abstract class T
        {
        }

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

        public static class Areturn extends T
        {
        }

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

        public static class Iadd extends T
        {
        }

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

        public static class Imul extends T
        {
        }

        public static class InvokeVirtual extends T
        {
            public String f;
            public String c;
            public List<Type.T> at;
            public Type.T rt;

            public InvokeVirtual(String f, String c, List<Type.T> at, Type.T rt)
            {
                this.f = f;
                this.c = c;
                this.at = at;
                this.rt = rt;
            }
        }

        public static class Ireturn extends T
        {
        }

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

        public static class Isub extends T
        {
        }

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

        public static class Write extends T
        {
        }

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
            private String literal;
            private String classId;
            private List<Dec.DecSingle> formalList;
            private List<Dec.DecSingle> localList;
            private List<Stm.T> statementList;
            private int index; // number of index
            private int retExpr;

            public MethodSingle(
                    String literal,
                    Type.T retType,
                    String classId,
                    List<Dec.DecSingle> formalList,
                    List<Dec.DecSingle> localList,
                    List<Stm.T> statementList,
                    int retExpr,
                    int index
            )
            {
                this.setRetType(retType);
                this.setLiteral(literal);
                this.setClassId(classId);
                this.setFormalList(formalList);
                this.setLocalList(localList);
                this.setStatementList(statementList);
                this.setRetExpr(retExpr);
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

            public String getLiteral()
            {
                return literal;
            }

            public void setLiteral(String literal)
            {
                this.literal = literal;
            }

            public String getClassId()
            {
                return classId;
            }

            public void setClassId(String classId)
            {
                this.classId = classId;
            }

            public List<Dec.DecSingle> getFormalList()
            {
                return formalList;
            }

            public void setFormalList(List<Dec.DecSingle> formalList)
            {
                this.formalList = formalList;
            }

            public List<Dec.DecSingle> getLocalList()
            {
                return localList;
            }

            public void setLocalList(List<Dec.DecSingle> localList)
            {
                this.localList = localList;
            }

            public List<Stm.T> getStatementList()
            {
                return statementList;
            }

            public void setStatementList(List<Stm.T> statementList)
            {
                this.statementList = statementList;
            }

            public int getIndex()
            {
                return index;
            }

            public void setIndex(int index)
            {
                this.index = index;
            }

            public int getRetExpr()
            {
                return retExpr;
            }

            public void setRetExpr(int retExpr)
            {
                this.retExpr = retExpr;
            }
        }
    }

    public static class Class
    {
        public static class GenClass
        {
            private String literal;
            private String parent;
            private List<Dec.DecSingle> fieldList;
            private List<Method.MethodSingle> methodList;

            public GenClass(String literal, String parent,
                            List<Dec.DecSingle> fieldList,
                            List<Method.MethodSingle> methodList)
            {
                this.setLiteral(literal);
                this.setParent(parent);
                this.setFieldList(fieldList);
                this.setMethodList(methodList);
            }

            public String getLiteral()
            {
                return literal;
            }

            public void setLiteral(String literal)
            {
                this.literal = literal;
            }

            public String getParent()
            {
                return parent;
            }

            public void setParent(String parent)
            {
                this.parent = parent;
            }

            public List<Dec.DecSingle> getFieldList()
            {
                return fieldList;
            }

            public void setFieldList(List<Dec.DecSingle> fieldList)
            {
                this.fieldList = fieldList;
            }

            public List<Method.MethodSingle> getMethodList()
            {
                return methodList;
            }

            public void setMethodList(List<Method.MethodSingle> methodList)
            {
                this.methodList = methodList;
            }
        }
    }

    public static class MainClass
    {
        public static class GenEntry
        {
            private String literal;
            private List<Stm.T> statementList;

            public GenEntry(String literal,
                            List<Stm.T> statementList)
            {
                this.setLiteral(literal);
                this.setStatementList(statementList);
            }

            public String getLiteral()
            {
                return literal;
            }

            public void setLiteral(String literal)
            {
                this.literal = literal;
            }

            public List<Stm.T> getStatementList()
            {
                return statementList;
            }

            public void setStatementList(List<Stm.T> statementList)
            {
                this.statementList = statementList;
            }
        }
    }

    public static class Program
    {
        public static class GenProgram
        {
            private MainClass.GenEntry entry;
            private List<Class.GenClass> classList;

            public GenProgram(MainClass.GenEntry entry,
                              List<Class.GenClass> classList)
            {
                this.setEntry(entry);
                this.setClassList(classList);
            }

            public MainClass.GenEntry getEntry()
            {
                return entry;
            }

            public void setEntry(MainClass.GenEntry entry)
            {
                this.entry = entry;
            }

            public List<Class.GenClass> getClassList()
            {
                return classList;
            }

            public void setClassList(List<Class.GenClass> classList)
            {
                this.classList = classList;
            }
        }
    }
}
