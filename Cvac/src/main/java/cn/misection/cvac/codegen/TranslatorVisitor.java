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

    public void visit(Ast.Type.Boolean t)
    {
        this.type = new Type.Int();
    }

    @Override
    public void visit(Ast.Type.ClassType t)
    {
        this.type = new Type.ClassType(t.id);
    }

    @Override
    public void visit(Ast.Type.Int t)
    {
        this.type = new Type.Int();
    }

    @Override
    public void visit(Ast.Dec.DecSingle d)
    {
        this.visit(d.type);
        this.dec = new Dec.DecSingle(this.type, d.id);
        if (this.indexTable != null) // if it is field
        this.indexTable.put(d.id, index++);
    }

    @Override
    public void visit(Ast.Exp.Add e)
    {
        this.visit(e.left);
        this.visit(e.right);
        emit(new Stm.Iadd());
    }

    @Override
    public void visit(Ast.Exp.And e)
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
    public void visit(Ast.Exp.Call e)
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
        emit(new Stm.Invokevirtual(e.id, e.type, at, rt));
    }

    @Override
    public void visit(Ast.Exp.False e)
    {
        emit(new Stm.Ldc(0));
    }

    @Override
    public void visit(Ast.Exp.Id e)
    {
        if (e.isField)
        {
            emit(new Stm.Aload(0));
            Ast.Type.T type = e.type;
            emit(new Stm.Getfield(this.classId + '/' + e.id,
                    type instanceof Ast.Type.ClassType ?
                            ("L" + ((Ast.Type.ClassType) type).id + ";")
                            : "I"));
        } else
        {
            int index = this.indexTable.get(e.id);
            if (e.type instanceof Ast.Type.ClassType)
                emit(new Stm.Aload(index));
            else emit(new Stm.Iload(index));
        }
    }

    @Override
    public void visit(Ast.Exp.LT e)
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
    public void visit(Ast.Exp.NewObject e)
    {
        emit(new Stm.New(e.id));
    }

    @Override
    public void visit(Ast.Exp.Not e)
    {
        Label f = new Label();
        Label r = new Label();
        this.visit(e.exp);
        emit(new Stm.Ldc(1));
        emit(new Stm.Ificmplt(f));
        emit(new Stm.Ldc(1));
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(f));
        emit(new Stm.Ldc(0));
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(Ast.Exp.Num e)
    {
        emit(new Stm.Ldc(e.num));
    }

    @Override
    public void visit(Ast.Exp.Sub e)
    {
        this.visit(e.left);
        this.visit(e.right);
        emit(new Stm.Isub());
    }

    @Override
    public void visit(Ast.Exp.This e)
    {
        emit(new Stm.Aload(0));
    }

    @Override
    public void visit(Ast.Exp.Times e)
    {
        this.visit(e.left);
        this.visit(e.right);
        emit(new Stm.Imul());
    }

    @Override
    public void visit(Ast.Exp.True e)
    {
        emit(new Stm.Ldc(1));
    }

    @Override
    public void visit(Ast.Stm.Assign s)
    {
        try
        {
            int index = this.indexTable.get(s.id);
            this.visit(s.exp);
            if (s.type instanceof Ast.Type.ClassType)
                emit(new Stm.Astore(index));
            else emit(new Stm.Istore(index));
        } catch (NullPointerException e)
        {
            emit(new Stm.Aload(0));
            this.visit(s.exp);
            emit(new Stm.Putfield(this.classId + '/' + s.id,
                    s.type instanceof Ast.Type.ClassType ?
                            ("L" + ((Ast.Type.ClassType) s.type).id + ";")
                            : "I"));
        }
    }

    @Override
    public void visit(Ast.Stm.Block s)
    {
        s.stms.forEach(this::visit);
    }

    @Override
    public void visit(Ast.Stm.If s)
    {
        Label l = new Label();
        Label r = new Label();
        this.visit(s.condition);
        emit(new Stm.Ldc(1));
        emit(new Stm.Ificmplt(l));
        this.visit(s.then_stm);
        emit(new Stm.Goto(r));
        emit(new Stm.LabelJ(l));
        this.visit(s.else_stm);
        emit(new Stm.LabelJ(r));
    }

    @Override
    public void visit(Ast.Stm.Print s)
    {
        this.visit(s.exp);
        emit(new Stm.Print());
    }

    @Override
    public void visit(Ast.Stm.While s)
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
    public void visit(Ast.Method.MethodSingle m)
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

        if (m.retType instanceof Ast.Type.ClassType)
            emit(new Stm.Areturn());
        else emit(new Stm.Ireturn());

        this.method = new Method.MethodSingle(_retType, m.id, this.classId,
                _formals, _locals, this.stms, 0, this.index);
    }

    @Override
    public void visit(Ast.Class.ClassSingle c)
    {
        this.classId = c.id;
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
                c.id, c.base, _fields, _methods);
    }

    @Override
    public void visit(Ast.MainClass.MainClassSingle c)
    {
        this.visit(c.stm);
        this.mainClass = new MainClass.MainClassSingle(c.id, this.stms);
        this.stms = new LinkedList<>();
    }

    @Override
    public void visit(Ast.Program.ProgramSingle p)
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
