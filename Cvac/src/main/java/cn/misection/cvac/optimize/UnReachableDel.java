package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.Ast;

import java.util.LinkedList;

/**
 * Created by Mengxu on 2017/1/25.
 */
public class UnReachableDel implements cn.misection.cvac.ast.Visitor, Optimizable
{
    private Ast.Stm.T curStm;
    private boolean isOptimizing;

    @Override
    public void visit(Ast.Type.Boolean t) {}

    @Override
    public void visit(Ast.Type.ClassType t) {}

    @Override
    public void visit(Ast.Type.Int t) {}

    @Override
    public void visit(Ast.Dec.DecSingle d) {}

    @Override
    public void visit(Ast.Exp.Add e) {}

    @Override
    public void visit(Ast.Exp.And e) {}

    @Override
    public void visit(Ast.Exp.Call e) {}

    @Override
    public void visit(Ast.Exp.False e) {}

    @Override
    public void visit(Ast.Exp.Id e) {}

    @Override
    public void visit(Ast.Exp.LT e) {}

    @Override
    public void visit(Ast.Exp.NewObject e) {}

    @Override
    public void visit(Ast.Exp.Not e) {}

    @Override
    public void visit(Ast.Exp.Num e) {}

    @Override
    public void visit(Ast.Exp.Sub e) {}

    @Override
    public void visit(Ast.Exp.This e) {}

    @Override
    public void visit(Ast.Exp.Times e) {}

    @Override
    public void visit(Ast.Exp.True e) {}

    @Override
    public void visit(Ast.Stm.Assign s)
    {
        this.curStm = s;
    }

    @Override
    public void visit(Ast.Stm.Block s)
    {
        LinkedList<Ast.Stm.T> _stms = new LinkedList<>();
        s.stms.forEach(stm ->
        {
            this.visit(stm);
            if (curStm != null)
                if (curStm instanceof Ast.Stm.Block)
                    ((Ast.Stm.Block) curStm).stms.forEach(_stms::add);
                else _stms.add(curStm);
        });
        s.stms = _stms;
        this.curStm = s;
    }

    @Override
    public void visit(Ast.Stm.If s)
    {
        if (s.condition instanceof Ast.Exp.True)
        {
            this.isOptimizing = true;
            this.curStm = s.then_stm;
            this.visit(this.curStm);
        } else if (s.condition instanceof Ast.Exp.False)
        {
            this.isOptimizing = true;
            this.curStm = s.else_stm;
            this.visit(this.curStm);
        } else this.curStm = s;
    }

    @Override
    public void visit(Ast.Stm.Print s)
    {
        this.curStm = s;
    }

    @Override
    public void visit(Ast.Stm.While s)
    {
        if (s.condition instanceof Ast.Exp.False)
        {
            this.isOptimizing = true;
            this.curStm = null;
        }
        else if (s.condition instanceof Ast.Exp.True)
        {
            System.out.println("Warning: at line " + s.lineNum
                    + " : " + "unend-loop!");
            this.curStm = s;
        } else this.curStm = s;

    }

    @Override
    public void visit(Ast.Method.MethodSingle m)
    {
        LinkedList<Ast.Stm.T> _stms = new LinkedList<>();
        m.stms.forEach(stm ->
        {
            this.visit(stm);
            if (this.curStm != null)
                if (this.curStm instanceof Ast.Stm.Block)
                    ((Ast.Stm.Block) this.curStm).stms.forEach(_stms::add);
                else _stms.add(curStm);
        });
        m.stms = _stms;
    }

    @Override
    public void visit(Ast.Class.ClassSingle c)
    {
        c.methods.forEach(this::visit);
    }

    @Override
    public void visit(Ast.MainClass.MainClassSingle c)
    {
        this.visit(c.stm);
        c.stm = this.curStm;
    }

    @Override
    public void visit(Ast.Program.ProgramSingle p)
    {
        this.isOptimizing = false;
        this.visit(p.mainClass);
        p.classes.forEach(this::visit);
    }

    @Override
    public boolean isOptimizing()
    {
        return this.isOptimizing;
    }
}
