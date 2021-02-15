package cn.misection.cvac.codegen;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;

import cn.misection.cvac.codegen.bst.CodeGenAst;
import cn.misection.cvac.codegen.bst.CodeGenAst.*;
import cn.misection.cvac.codegen.bst.Label;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mengxu on 2017/1/17.
 */
public class TranslatorVisitor implements IVisitor
{
    private String classId;
    private int index;

    private Hashtable<String, Integer> indexTable;
    private Type.T type;
    private Dec.GenDeclaration dec;
    private List<Stm.T> stms;
    private Method.GenMethod method;
    private CodeGenAst.Class.GenClass classs;
    private MainClass.GenEntry mainClass;
    private Program.GenProgram prog;

    public TranslatorVisitor()
    {
        this.setClassId(null);
        this.indexTable = null;
        this.setType(null);
        this.setDec(null);
        this.setStms(new LinkedList<>());
        this.setMethod(null);
        this.setClassId(null);
        this.setMainClass(null);
        this.setClasss(null);
        this.setProg(null);
    }

    private void emit(Stm.T s)
    {
        this.getStms().add(s);
    }

    @Override
    public void visit(CvaBoolean t)
    {
        this.setType(new Type.GenInt());
    }

    @Override
    public void visit(CvaClassType t)
    {
        this.setType(new Type.ClassType(t.getLiteral()));
    }

    @Override
    public void visit(CvaInt t)
    {
        this.setType(new Type.GenInt());
    }

    @Override
    public void visit(CvaDeclaration d)
    {
        this.visit(d.getType());
        this.setDec(new Dec.GenDeclaration(
                d.getLiteral(),
                this.getType()));
        if (this.indexTable != null) // if it is field
        {
            this.indexTable.put(d.getLiteral(), index++);
        }
    }

    @Override
    public void visit(CvaAddExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
        emit(new Stm.IAdd());
    }

    @Override
    public void visit(CvaAndAndExpr e)
    {
        Label f = new Label();
        Label r = new Label();
        this.visit(e.getLeft());
        emit(new Stm.Ldc(1));
        emit(new Stm.IFicmplt(f));
        this.visit(e.getRight());
        emit(new Stm.Ldc(1));
        emit(new Stm.IFicmplt(f));
        emit(new Stm.Ldc(1));
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(f));
        emit(new Stm.Ldc(0));
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(CvaCallExpr e)
    {
        this.visit(e.getExpr());
        e.getArgs().forEach(this::visit);
        this.visit(e.getRetType());
        Type.T rt = this.getType();
        List<Type.T> at = new LinkedList<>();
        e.getArgTypeList().forEach(a ->
        {
            this.visit(a);
            at.add(this.getType());
        });
        emit(new Stm.InvokeVirtual(e.getLiteral(), e.getType(), at, rt));
    }

    @Override
    public void visit(CvaFalseExpr e)
    {
        emit(new Stm.Ldc(0));
    }

    @Override
    public void visit(CvaIdentifier e)
    {
        if (e.isField())
        {
            emit(new Stm.ALoad(0));
            AbstractType type = e.getType();
            emit(new Stm.GetField(String.format("%s/%s", this.getClassId(), e.getLiteral()),
                    type instanceof CvaClassType ?
                            (String.format("L%s;", ((CvaClassType) type).getLiteral()))
                            : "I"));
        }
        else
        {
            int index = this.indexTable.get(e.getLiteral());
            if (e.getType() instanceof CvaClassType)
            {
                emit(new Stm.ALoad(index));
            }
            else
            {
                emit(new Stm.ILoad(index));
            }
        }
    }

    @Override
    public void visit(CvaLTExpr e)
    {
        Label t = new Label();
        Label r = new Label();
        this.visit(e.getLeft());
        this.visit(e.getRight());
        emit(new Stm.IFicmplt(t));
        emit(new Stm.Ldc(0));
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(t));
        emit(new Stm.Ldc(1));
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(CvaNewExpr e)
    {
        emit(new Stm.New(e.getLiteral()));
    }

    @Override
    public void visit(CvaNegateExpr e)
    {
        Label f = new Label();
        Label r = new Label();
        this.visit(e.getExpr());
        emit(new Stm.Ldc(1));
        emit(new Stm.IFicmplt(f));
        emit(new Stm.Ldc(1));
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(f));
        emit(new Stm.Ldc(0));
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(CvaNumberInt e)
    {
        emit(new Stm.Ldc(e.getValue()));
    }

    @Override
    public void visit(CvaSubExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
        emit(new Stm.ISub());
    }

    @Override
    public void visit(CvaThisExpr e)
    {
        emit(new Stm.ALoad(0));
    }

    @Override
    public void visit(CvaMuliExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
        emit(new Stm.IMul());
    }

    @Override
    public void visit(CvaTrueExpr e)
    {
        emit(new Stm.Ldc(1));
    }

    @Override
    public void visit(CvaAssign s)
    {
        try
        {
            int index = this.indexTable.get(s.getLiteral());
            this.visit(s.getExpr());
            if (s.getType() instanceof CvaClassType)
            {
                emit(new Stm.AStore(index));
            }
            else
            {
                emit(new Stm.IStore(index));
            }
        }
        catch (NullPointerException e)
        {
            emit(new Stm.ALoad(0));
            this.visit(s.getExpr());
            emit(new Stm.PutField(String.format("%s/%s", this.getClassId(), s.getLiteral()),
                    s.getType() instanceof CvaClassType ?
                            (String.format("L%s;", ((CvaClassType) s.getType()).getLiteral()))
                            : "I"));
        }
    }

    @Override
    public void visit(CvaBlock s)
    {
        s.getStatementList().forEach(this::visit);
    }

    @Override
    public void visit(CvaIfStatement s)
    {
        Label l = new Label();
        Label r = new Label();
        this.visit(s.getCondition());
        emit(new Stm.Ldc(1));
        emit(new Stm.IFicmplt(l));
        this.visit(s.getThenStatement());
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(l));
        this.visit(s.getElseStatement());
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(CvaWriteOperation s)
    {
        this.visit(s.getExpr());
        emit(new Stm.Write());
    }

    @Override
    public void visit(CvaWhileStatement s)
    {
        Label con = new Label();
        Label end = new Label();
        emit(new Stm.LabelJ(con));
        this.visit(s.getCondition());
        emit(new Stm.Ldc(1));
        emit(new Stm.IFicmplt(end));
        this.visit(s.getBody());
        emit(new Stm.Goto(con));
        emit(new Stm.LabelJ(end));
    }

    @Override
    public void visit(CvaMethod cvaMethod)
    {
        this.index = 1;
        this.indexTable = new Hashtable<>();
        this.visit(cvaMethod.getRetType());
        Type.T theRetType = this.getType();

        List<Dec.GenDeclaration> formalList = new LinkedList<>();
        cvaMethod.getFormalList().forEach(f ->
        {
            this.visit(f);
            formalList.add(this.getDec());
        });

        List<Dec.GenDeclaration> localList = new LinkedList<>();
        cvaMethod.getLocalList().forEach(l ->
        {
            this.visit(l);
            localList.add(this.getDec());
        });
        this.setStms(new LinkedList<>());
        cvaMethod.getStatementList().forEach(this::visit);

        this.visit(cvaMethod.getRetExpr());

        if (cvaMethod.getRetType() instanceof CvaClassType)
        {
            emit(new Stm.AReturn());
        }
        else
        {
            emit(new Stm.IReturn());
        }

        this.setMethod(new Method.GenMethod(
                cvaMethod.getLiteral(),
                theRetType,
                this.getClassId(),
                formalList,
                localList,
                this.getStms(),
                0,
                this.index));
    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        this.setClassId(cvaClass.getLiteral());
        List<Dec.GenDeclaration> fieldList = new LinkedList<>();
        cvaClass.getFieldList().forEach(f ->
        {
            this.visit(f);
            fieldList.add(this.getDec());
        });
        List<Method.GenMethod> methodList = new LinkedList<>();
        cvaClass.getMethodList().forEach(m ->
        {
            this.visit(m);
            methodList.add(this.getMethod());
        });
        this.setClasss(new CodeGenAst.Class.GenClass(
                cvaClass.getLiteral(), cvaClass.getParent(), fieldList, methodList));
    }

    @Override
    public void visit(CvaEntry c)
    {
        this.visit(c.getStatement());
        this.setMainClass(new MainClass.GenEntry(c.getLiteral(), this.getStms()));
        this.setStms(new LinkedList<>());
    }

    @Override
    public void visit(CvaProgram p)
    {
        this.visit(p.getEntry());
        List<CodeGenAst.Class.GenClass> classList = new LinkedList<>();
        p.getClassList().forEach(c ->
        {
            this.visit(c);
            classList.add(this.getClasss());
        });
        this.setProg(new Program.GenProgram(this.getMainClass(), classList));
    }

    public String getClassId()
    {
        return classId;
    }

    public void setClassId(String classId)
    {
        this.classId = classId;
    }

    public Type.T getType()
    {
        return type;
    }

    public void setType(Type.T type)
    {
        this.type = type;
    }

    public Dec.GenDeclaration getDec()
    {
        return dec;
    }

    public void setDec(Dec.GenDeclaration dec)
    {
        this.dec = dec;
    }

    public List<Stm.T> getStms()
    {
        return stms;
    }

    public void setStms(List<Stm.T> stms)
    {
        this.stms = stms;
    }

    public Method.GenMethod getMethod()
    {
        return method;
    }

    public void setMethod(Method.GenMethod method)
    {
        this.method = method;
    }

    public CodeGenAst.Class.GenClass getClasss()
    {
        return classs;
    }

    public void setClasss(CodeGenAst.Class.GenClass classs)
    {
        this.classs = classs;
    }

    public MainClass.GenEntry getMainClass()
    {
        return mainClass;
    }

    public void setMainClass(MainClass.GenEntry mainClass)
    {
        this.mainClass = mainClass;
    }

    public Program.GenProgram getProg()
    {
        return prog;
    }

    public void setProg(Program.GenProgram prog)
    {
        this.prog = prog;
    }
}
