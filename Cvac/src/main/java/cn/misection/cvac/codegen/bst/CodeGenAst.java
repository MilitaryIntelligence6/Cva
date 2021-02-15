package cn.misection.cvac.codegen.bst;

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

        public static class GenInt extends T
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
        public static class GenDeclaration
        {
            private Type.T type;
            private String literal;

            public GenDeclaration(String literal, Type.T type)
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

        public static class ALoad extends T
        {
            private int index;

            public ALoad(int index)
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

        public static class AReturn extends T
        {
        }

        public static class AStore extends T
        {
            private int index;

            public AStore(int index)
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

        public static class GetField extends T
        {
            private String fieldSpec;
            public String descriptor;

            public GetField(String fieldSpec, String descriptor)
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

        public static class IAdd extends T
        {
        }

        public static class IFicmplt extends T
        {
            private Label label;

            public IFicmplt(Label label)
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

        public static class ILoad extends T
        {
            private int index;

            public ILoad(int index)
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

        public static class IMul extends T
        {
        }

        public static class InvokeVirtual extends T
        {
            private String f;
            private String c;
            private List<Type.T> argTypeList;
            private Type.T retType;

            public InvokeVirtual(String f, String c, List<Type.T> argTypeList, Type.T retType)
            {
                this.setF(f);
                this.setC(c);
                this.setArgTypeList(argTypeList);
                this.setRetType(retType);
            }

            public String getF()
            {
                return f;
            }

            public void setF(String f)
            {
                this.f = f;
            }

            public String getC()
            {
                return c;
            }

            public void setC(String c)
            {
                this.c = c;
            }

            public List<Type.T> getArgTypeList()
            {
                return argTypeList;
            }

            public void setArgTypeList(List<Type.T> argTypeList)
            {
                this.argTypeList = argTypeList;
            }

            public Type.T getRetType()
            {
                return retType;
            }

            public void setRetType(Type.T retType)
            {
                this.retType = retType;
            }
        }

        public static class IReturn extends T
        {
        }

        public static class IStore extends T
        {
            private int index;

            public IStore(int index)
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

        public static class ISub extends T
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

        public static class PutField extends T
        {
            private String fieldSpec;
            public String descriptor;

            public PutField(String fieldSpec, String descriptor)
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
        public static class GenMethod
        {
            private Type.T retType;
            private String literal;
            private String classId;
            private List<Dec.GenDeclaration> formalList;
            private List<Dec.GenDeclaration> localList;
            private List<Stm.T> statementList;
            private int index; // number of index
            private int retExpr;

            public GenMethod(
                    String literal,
                    Type.T retType,
                    String classId,
                    List<Dec.GenDeclaration> formalList,
                    List<Dec.GenDeclaration> localList,
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

            public List<Dec.GenDeclaration> getFormalList()
            {
                return formalList;
            }

            public void setFormalList(List<Dec.GenDeclaration> formalList)
            {
                this.formalList = formalList;
            }

            public List<Dec.GenDeclaration> getLocalList()
            {
                return localList;
            }

            public void setLocalList(List<Dec.GenDeclaration> localList)
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
            private List<Dec.GenDeclaration> fieldList;
            private List<Method.GenMethod> methodList;

            public GenClass(String literal, String parent,
                            List<Dec.GenDeclaration> fieldList,
                            List<Method.GenMethod> methodList)
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

            public List<Dec.GenDeclaration> getFieldList()
            {
                return fieldList;
            }

            public void setFieldList(List<Dec.GenDeclaration> fieldList)
            {
                this.fieldList = fieldList;
            }

            public List<Method.GenMethod> getMethodList()
            {
                return methodList;
            }

            public void setMethodList(List<Method.GenMethod> methodList)
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
