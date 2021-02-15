package cn.misection.cvac.codegen;

import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;

import cn.misection.cvac.codegen.ast.CodeGenAst;
import cn.misection.cvac.codegen.ast.CodeGenAst.*;
import cn.misection.cvac.codegen.ast.Label;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mengxu on 2017/1/17.
 */
public class TranslatorVisitor implements cn.misection.cvac.ast.Visitor
{
    private String classId;
    private int index;

    private Hashtable<String, Integer> indexTable;
    private Type.T type;
    private Dec.DecSingle dec;
    private List<Stm.T> stms;
    private Method.MethodSingle method;
    private CodeGenAst.Class.ClassSingle classs;
    private MainClass.MainClassSingle mainClass;
    public Program.ProgramSingle prog;

    public TranslatorVisitor()
    {
        this.classId = null;
        this.indexTable = null;
        this.type = null;
        this.dec = null;
        this.stms = new LinkedList<>();
        this.method = null;
        this.classId = null;
        this.mainClass = null;
        this.classs = null;
        this.prog = null;
    }

    private void emit(Stm.T s)
    {
        this.stms.add(s);
    }

    @Override
    public void visit(CvaBoolean t)
    {
        this.type = new Type.Int();
    }

    @Override
    public void visit(CvaClassType t)
    {
        this.type = new Type.ClassType(t.getLiteral());
    }

    @Override
    public void visit(CvaInt t)
    {
        this.type = new Type.Int();
    }

    @Override
    public void visit(CvaDeclaration d)
    {
        this.visit(d.getType());
        this.dec = new Dec.DecSingle(this.type, d.getLiteral());
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
        emit(new Stm.Iadd());
    }

    @Override
    public void visit(CvaAndAndExpr e)
    {
        Label f = new Label();
        Label r = new Label();
        this.visit(e.getLeft());
        emit(new Stm.Ldc(1));
        emit(new Stm.Ificmplt(f));
        this.visit(e.getRight());
        emit(new Stm.Ldc(1));
        emit(new Stm.Ificmplt(f));
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
        Type.T rt = this.type;
        List<Type.T> at = new LinkedList<>();
        e.getArgTypeList().forEach(a ->
        {
            this.visit(a);
            at.add(this.type);
        });
        emit(new Stm.Invokevirtual(e.getLiteral(), e.getType(), at, rt));
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
            emit(new Stm.Aload(0));
            AbstractType type = e.getType();
            emit(new Stm.Getfield(this.classId + '/' + e.getLiteral(),
                    type instanceof CvaClassType ?
                            ("L" + ((CvaClassType) type).getLiteral() + ";")
                            : "I"));
        }
        else
        {
            int index = this.indexTable.get(e.getLiteral());
            if (e.getType() instanceof CvaClassType)
            {
                emit(new Stm.Aload(index));
            }
            else
            {
                emit(new Stm.Iload(index));
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
        emit(new Stm.Ificmplt(t));
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
        emit(new Stm.Ificmplt(f));
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
        emit(new Stm.Isub());
    }

    @Override
    public void visit(CvaThisExpr e)
    {
        emit(new Stm.Aload(0));
    }

    @Override
    public void visit(CvaMuliExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
        emit(new Stm.Imul());
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
                emit(new Stm.Astore(index));
            }
            else
            {
                emit(new Stm.Istore(index));
            }
        }
        catch (NullPointerException e)
        {
            emit(new Stm.Aload(0));
            this.visit(s.getExpr());
            emit(new Stm.Putfield(String.format("%s/%s", this.classId, s.getLiteral()),
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
        emit(new Stm.Ificmplt(l));
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
        emit(new Stm.Ificmplt(end));
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
        Type.T theRetType = this.type;

        List<Dec.DecSingle> formalList = new LinkedList<>();
        cvaMethod.getFormalList().forEach(f ->
        {
            this.visit(f);
            formalList.add(this.dec);
        });

        List<Dec.DecSingle> localList = new LinkedList<>();
        cvaMethod.getLocalList().forEach(l ->
        {
            this.visit(l);
            localList.add(this.dec);
        });
        this.stms = new LinkedList<>();
        cvaMethod.getStatementList().forEach(this::visit);

        this.visit(cvaMethod.getRetExpr());

        if (cvaMethod.getRetType() instanceof CvaClassType)
        {
            emit(new Stm.Areturn());
        }
        else
        {
            emit(new Stm.Ireturn());
        }

        this.method = new Method.MethodSingle(theRetType, cvaMethod.getLiteral(), this.classId,
                formalList, localList, this.stms, 0, this.index);
    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        this.classId = cvaClass.getLiteral();
        List<Dec.DecSingle> fieldList = new LinkedList<>();
        cvaClass.getFieldList().forEach(f ->
        {
            this.visit(f);
            fieldList.add(this.dec);
        });
        List<Method.MethodSingle> methodList = new LinkedList<>();
        cvaClass.getMethodList().forEach(m ->
        {
            this.visit(m);
            methodList.add(this.method);
        });
        this.classs = new CodeGenAst.Class.ClassSingle(
                cvaClass.getLiteral(), cvaClass.getParent(), fieldList, methodList);
    }

    @Override
    public void visit(CvaEntry c)
    {
        this.visit(c.getStatement());
        this.mainClass = new MainClass.MainClassSingle(c.getLiteral(), this.stms);
        this.stms = new LinkedList<>();
    }

    @Override
    public void visit(CvaProgram p)
    {
        this.visit(p.getEntry());
        List<CodeGenAst.Class.ClassSingle> classList = new LinkedList<>();
        p.getClassList().forEach(c ->
        {
            this.visit(c);
            classList.add(this.classs);
        });
        this.prog = new Program.ProgramSingle(this.mainClass, classList);
    }
}
