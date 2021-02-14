package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.Ast;

/**
 * Created by Mengxu on 2017/1/23.
 */
public class ConstantFolder implements cn.misection.cvac.ast.Visitor, Optimizable
{
    private Ast.Exp.T lastExp;
    private boolean isOptimizing;

    private boolean isConstant()
    {
        return lastExp != null && this.isConstant(this.lastExp);
    }

    private boolean isConstant(Ast.Exp.T exp)
    {
        return exp instanceof Ast.Exp.Num
                || exp instanceof Ast.Exp.True
                || exp instanceof Ast.Exp.False;
    }

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
        if (isConstant())
        {
            Ast.Exp.Num temLeft = (Ast.Exp.Num) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = new Ast.Exp.Num(
                        temLeft.num + ((Ast.Exp.Num) this.lastExp).num,
                        this.lastExp.lineNum);
            } else this.lastExp = new Ast.Exp.Add(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.And e)
    {
        this.visit(e.left);
        Ast.Exp.T temLeft = this.lastExp;
        this.visit(e.right);
        Ast.Exp.T temRight = this.lastExp;

        this.isOptimizing = true;
        if (temLeft instanceof Ast.Exp.False
                || temRight instanceof Ast.Exp.False)
            this.lastExp = new Ast.Exp.False(e.lineNum);
        else if (temLeft instanceof Ast.Exp.True
                && temRight instanceof Ast.Exp.True)
            this.lastExp = temLeft;
        else if (temLeft instanceof Ast.Exp.True)
            this.lastExp = temRight;
        else if (temRight instanceof Ast.Exp.True)
            this.lastExp = temLeft;
        else
        {
            this.isOptimizing = false;
            this.lastExp = e;
        }
    }

    @Override
    public void visit(Ast.Exp.Call e)
    {
        java.util.LinkedList<Ast.Exp.T> _args = new java.util.LinkedList<>();
        e.args.forEach(arg ->
        {
            this.visit(arg);
            _args.add(this.lastExp);
        });
        e.args = _args;
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.False e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.Id e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.LT e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Exp.Num temLeft = (Ast.Exp.Num) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = temLeft.num < ((Ast.Exp.Num) this.lastExp).num
                        ? new Ast.Exp.True(this.lastExp.lineNum)
                        : new Ast.Exp.False(this.lastExp.lineNum);
            }
            else this.lastExp = new Ast.Exp.LT(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.NewObject e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.Not e)
    {
        this.visit(e.exp);
        if (isConstant())
        {
            this.isOptimizing = true;
            this.lastExp = this.lastExp instanceof Ast.Exp.True
                    ? new Ast.Exp.False(this.lastExp.lineNum)
                    : new Ast.Exp.True(this.lastExp.lineNum);
        }
        else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.Num e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.Sub e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Exp.Num temLeft = (Ast.Exp.Num) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = new Ast.Exp.Num(
                        temLeft.num - ((Ast.Exp.Num) this.lastExp).num,
                        this.lastExp.lineNum);
            }
            else this.lastExp = new Ast.Exp.Sub(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.This e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.Times e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Exp.Num temLeft = (Ast.Exp.Num) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = new Ast.Exp.Num(
                        temLeft.num * ((Ast.Exp.Num) this.lastExp).num,
                        this.lastExp.lineNum);
            }
            else this.lastExp = new Ast.Exp.Times(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.True e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Stm.Assign s)
    {
        this.visit(s.exp);
        s.exp = this.lastExp;
    }

    @Override
    public void visit(Ast.Stm.Block s)
    {
        s.stms.forEach(this::visit);
    }

    @Override
    public void visit(Ast.Stm.If s)
    {
        this.visit(s.condition);
        s.condition = this.lastExp;
        this.visit(s.thenStm);
        this.visit(s.elseStm);
    }

    @Override
    public void visit(Ast.Stm.Write s)
    {
        this.visit(s.exp);
        s.exp = this.lastExp;
    }

    @Override
    public void visit(Ast.Stm.While s)
    {
        this.visit(s.condition);
        s.condition = this.lastExp;
        this.visit(s.body);
    }

    @Override
    public void visit(Ast.Method.MethodSingle m)
    {
        m.stms.forEach(this::visit);
        this.visit(m.retExp);
        m.retExp = this.lastExp;
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
