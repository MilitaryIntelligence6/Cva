package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.Ast;

import java.util.HashSet;

/**
 * Created by Mengxu on 2017/1/27.
 */
public class DeadCodeDel implements cn.misection.cvac.ast.Visitor, Optimizable
{
    private HashSet<String> curFields;  // the fields of current class
    private HashSet<String> localVars;  // the local variables and formals in current method
    private HashSet<String> localLiveness;  // the living id in current statement
    // private boolean isAssign;   // current id is in the left of assign(true), or is being evaluated(false)
    private boolean containsCall;   // current statement contains method call?
    private boolean shouldDel;  // should delete current statement?
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
    public void visit(Ast.Exp.Add e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Exp.And e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Exp.Call e)
    {
        this.visit(e.exp);
        e.args.forEach(this::visit);
        this.containsCall = true;
    }

    @Override
    public void visit(Ast.Exp.False e) {}

    @Override
    public void visit(Ast.Exp.Id e)
    {
        if (this.localVars.contains(e.id))
            this.localLiveness.add(e.id);
    }

    @Override
    public void visit(Ast.Exp.LT e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Exp.NewObject e) {}

    @Override
    public void visit(Ast.Exp.Not e)
    {
        this.visit(e.exp);
    }

    @Override
    public void visit(Ast.Exp.Num e) {}

    @Override
    public void visit(Ast.Exp.Sub e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Exp.This e) {}

    @Override
    public void visit(Ast.Exp.Times e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Exp.True e) {}

    @Override
    public void visit(Ast.Stm.Assign s)
    {
        if (this.localLiveness.contains(s.id)
                || this.curFields.contains(s.id))
        {
            this.localLiveness.remove(s.id);
            this.visit(s.exp);
            this.shouldDel = false;
            return;
        }

        this.containsCall = false;
        this.visit(s.exp);
        if (this.containsCall)
            this.shouldDel = false;
    }

    @Override
    public void visit(Ast.Stm.Block s)
    {
        for (int i = s.stms.size() - 1; i >= 0; i--)
        {
            this.visit(s.stms.get(i));
            if (this.shouldDel)
            {
                this.isOptimizing = true;
                s.stms.remove(i);
            }
        }
        this.shouldDel = s.stms.size() == 0;
    }

    @Override
    public void visit(Ast.Stm.If s)
    {
        HashSet<String> temOriginal = new HashSet<>();
        this.localLiveness.forEach(temOriginal::add);
        this.visit(s.thenStm);
        if (this.shouldDel)
            s.thenStm = null;
        HashSet<String> _leftLiveness = this.localLiveness;

        this.localLiveness = temOriginal;
        this.visit(s.elseStm);
        if (this.shouldDel)
            s.elseStm = null;
        _leftLiveness.forEach(this.localLiveness::add);

        this.shouldDel = s.thenStm == null && s.thenStm == null;
        if (this.shouldDel)
        {
            this.localLiveness = temOriginal;
            return;
        }
        // this.isAssign = false;
        this.visit(s.condition);
    }

    @Override
    public void visit(Ast.Stm.Write s)
    {
        // this.isAssign = false;
        this.visit(s.exp);
        this.shouldDel = false;
    }

    @Override
    public void visit(Ast.Stm.While s)
    {
        HashSet<String> temOriginal = new HashSet<>();
        this.localLiveness.forEach(temOriginal::add);
        this.visit(s.body);
        if (this.shouldDel) // this statement will be deleted totally
        {
            this.localLiveness = temOriginal;
            return;         // so return is safe.
        }
        // this.isAssign = false;
        this.visit(s.condition);
    }

    @Override
    public void visit(Ast.Method.MethodSingle m)
    {
        this.localVars = new HashSet<>();
        m.formals.forEach(f -> this.localVars.add(((Ast.Dec.DecSingle) f).id));
        m.locals.forEach(l -> this.localVars.add(((Ast.Dec.DecSingle) l).id));
        this.localLiveness = new HashSet<>();

        // this.isAssign = false;
        this.visit(m.retExp);

        for (int i = m.stms.size() - 1; i >= 0; i--)
        {
            this.visit(m.stms.get(i));
            if (this.shouldDel)
            {
                this.isOptimizing = true;
                m.stms.remove(i);
            }
        }
    }

    @Override
    public void visit(Ast.Class.ClassSingle c)
    {
        this.curFields = new HashSet<>();
        c.fields.forEach(f ->
                this.curFields.add(((Ast.Dec.DecSingle) f).id));

        c.methods.forEach(this::visit);
    }

    @Override
    public void visit(Ast.MainClass.MainClassSingle c) {}

    @Override
    public void visit(Ast.Program.ProgramSingle p)
    {
        this.isOptimizing = false;
        p.classes.forEach(this::visit);
    }

    @Override
    public boolean isOptimizing()
    {
        return this.isOptimizing;
    }
}
