package cn.misection.cvac.codegen;

import cn.misection.cvac.ast.Ast;
import cn.misection.cvac.codegen.ast.Ast.*;
import cn.misection.cvac.codegen.ast.Label;

import java.util.Hashtable;
import java.util.LinkedList;

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
    private LinkedList<Stm.T> stms;
    private Method.MethodSingle method;
    private cn.misection.cvac.codegen.ast.Ast.Class.ClassSingle classs;
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

    public void visit(Ast.Type.CvaBoolean t)
    {
        this.type = new Type.Int();
    }

    @Override
    public void visit(Ast.Type.CvaClass t)
    {
        this.type = new Type.ClassType(t.literal);
    }

    @Override
    public void visit(Ast.Type.Int t)
    {
        this.type = new Type.Int();
    }

    @Override
    public void visit(Ast.Decl.CvaDeclaration d)
    {
        this.visit(d.type);
        this.dec = new Dec.DecSingle(this.type, d.literal);
        if (this.indexTable != null) // if it is field
        this.indexTable.put(d.literal, index++);
    }

    @Override
    public void visit(Ast.Expr.CvaAddExpr e)
    {
        this.visit(e.left);
        this.visit(e.right);
        emit(new Stm.Iadd());
    }

    @Override
    public void visit(Ast.Expr.CvaAndAndExpr e)
    {
        Label f = new Label();
        Label r = new Label();
        this.visit(e.left);
        emit(new Stm.Ldc(1));
        emit(new Stm.Ificmplt(f));
        this.visit(e.right);
        emit(new Stm.Ldc(1));
        emit(new Stm.Ificmplt(f));
        emit(new Stm.Ldc(1));
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(f));
        emit(new Stm.Ldc(0));
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(Ast.Expr.CvaCallExpr e)
    {
        this.visit(e.exp);
        e.args.forEach(this::visit);
        this.visit(e.rt);
        Type.T rt = this.type;
        LinkedList<Type.T> at = new LinkedList<>();
        e.at.forEach(a ->
        {
            this.visit(a);
            at.add(this.type);
        });
        emit(new Stm.Invokevirtual(e.literal, e.type, at, rt));
    }

    @Override
    public void visit(Ast.Expr.CvaFalseExpr e)
    {
        emit(new Stm.Ldc(0));
    }

    @Override
    public void visit(Ast.Expr.CvaIdentifier e)
    {
        if (e.isField)
        {
            emit(new Stm.Aload(0));
            Ast.Type.T type = e.type;
            emit(new Stm.Getfield(this.classId + '/' + e.literal,
                    type instanceof Ast.Type.CvaClass ?
                            ("L" + ((Ast.Type.CvaClass) type).literal + ";")
                            : "I"));
        } else
        {
            int index = this.indexTable.get(e.literal);
            if (e.type instanceof Ast.Type.CvaClass)
                emit(new Stm.Aload(index));
            else emit(new Stm.Iload(index));
        }
    }

    @Override
    public void visit(Ast.Expr.CvaLTExpr e)
    {
        Label t = new Label();
        Label r = new Label();
        this.visit(e.left);
        this.visit(e.right);
        emit(new Stm.Ificmplt(t));
        emit(new Stm.Ldc(0));
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(t));
        emit(new Stm.Ldc(1));
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(Ast.Expr.CvaNewExpr e)
    {
        emit(new Stm.New(e.literal));
    }

    @Override
    public void visit(Ast.Expr.CvaNegateExpr e)
    {
        Label f = new Label();
        Label r = new Label();
        this.visit(e.expr);
        emit(new Stm.Ldc(1));
        emit(new Stm.Ificmplt(f));
        emit(new Stm.Ldc(1));
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(f));
        emit(new Stm.Ldc(0));
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(Ast.Expr.CvaNumberInt e)
    {
        emit(new Stm.Ldc(e.value));
    }

    @Override
    public void visit(Ast.Expr.CvaSubExpr e)
    {
        this.visit(e.left);
        this.visit(e.right);
        emit(new Stm.Isub());
    }

    @Override
    public void visit(Ast.Expr.CvaThisExpr e)
    {
        emit(new Stm.Aload(0));
    }

    @Override
    public void visit(Ast.Expr.CvaMuliExpr e)
    {
        this.visit(e.left);
        this.visit(e.right);
        emit(new Stm.Imul());
    }

    @Override
    public void visit(Ast.Expr.CvaTrueExpr e)
    {
        emit(new Stm.Ldc(1));
    }

    @Override
    public void visit(Ast.Stm.CvaAssign s)
    {
        try
        {
            int index = this.indexTable.get(s.id);
            this.visit(s.exp);
            if (s.type instanceof Ast.Type.CvaClass)
                emit(new Stm.Astore(index));
            else emit(new Stm.Istore(index));
        } catch (NullPointerException e)
        {
            emit(new Stm.Aload(0));
            this.visit(s.exp);
            emit(new Stm.Putfield(this.classId + '/' + s.id,
                    s.type instanceof Ast.Type.CvaClass ?
                            ("L" + ((Ast.Type.CvaClass) s.type).literal + ";")
                            : "I"));
        }
    }

    @Override
    public void visit(Ast.Stm.CvaBlock s)
    {
        s.stms.forEach(this::visit);
    }

    @Override
    public void visit(Ast.Stm.CvaIfStatement s)
    {
        Label l = new Label();
        Label r = new Label();
        this.visit(s.condition);
        emit(new Stm.Ldc(1));
        emit(new Stm.Ificmplt(l));
        this.visit(s.thenStm);
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(l));
        this.visit(s.elseStm);
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(Ast.Stm.CvaWriteOperation s)
    {
        this.visit(s.exp);
        emit(new Stm.Write());
    }

    @Override
    public void visit(Ast.Stm.CvaWhileStatement s)
    {
        Label con = new Label();
        Label end = new Label();
        emit(new Stm.LabelJ(con));
        this.visit(s.condition);
        emit(new Stm.Ldc(1));
        emit(new Stm.Ificmplt(end));
        this.visit(s.body);
        emit(new Stm.Goto(con));
        emit(new Stm.LabelJ(end));
    }

    @Override
    public void visit(Ast.Method.CvaMethod m)
    {
        this.index = 1;
        this.indexTable = new Hashtable<>();
        this.visit(m.retType);
        Type.T _retType = this.type;

        LinkedList<Dec.DecSingle> _formals = new LinkedList<>();
        m.formals.forEach(f ->
        {
            this.visit(f);
            _formals.add(this.dec);
        });

        LinkedList<Dec.DecSingle> _locals = new LinkedList<>();
        m.locals.forEach(l ->
        {
            this.visit(l);
            _locals.add(this.dec);
        });
        this.stms = new LinkedList<>();
        m.stms.forEach(this::visit);

        this.visit(m.retExp);

        if (m.retType instanceof Ast.Type.CvaClass)
            emit(new Stm.Areturn());
        else emit(new Stm.Ireturn());

        this.method = new Method.MethodSingle(_retType, m.literal, this.classId,
                _formals, _locals, this.stms, 0, this.index);
    }

    @Override
    public void visit(Ast.Clas.CvaClass c)
    {
        this.classId = c.literal;
        LinkedList<Dec.DecSingle> _fields = new LinkedList<>();
        c.fields.forEach(f ->
        {
            this.visit(f);
            _fields.add(this.dec);
        });
        LinkedList<Method.MethodSingle> _methods = new LinkedList<>();
        c.methods.forEach(m ->
        {
            this.visit(m);
            _methods.add(this.method);
        });
        this.classs = new cn.misection.cvac.codegen.ast.Ast.Class.ClassSingle(
                c.literal, c.parent, _fields, _methods);
    }

    @Override
    public void visit(Ast.MainClass.CvaEntry c)
    {
        this.visit(c.stm);
        this.mainClass = new MainClass.MainClassSingle(c.id, this.stms);
        this.stms = new LinkedList<>();
    }

    @Override
    public void visit(Ast.Program.CvaProgram p)
    {
        this.visit(p.mainClass);
        LinkedList<cn.misection.cvac.codegen.ast.Ast.Class.ClassSingle> _class =
                new LinkedList<>();
        p.classes.forEach(c ->
        {
            this.visit(c);
            _class.add(this.classs);
        });
        this.prog = new Program.ProgramSingle(this.mainClass, _class);
    }
}
