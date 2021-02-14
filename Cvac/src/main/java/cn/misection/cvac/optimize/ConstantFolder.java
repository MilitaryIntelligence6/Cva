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
        return exp instanceof Ast.Exp.CvaNumberInt
                || exp instanceof Ast.Exp.CvaTrueExpr
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
            Ast.Exp.CvaNumberInt temLeft = (Ast.Exp.CvaNumberInt) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = new Ast.Exp.CvaNumberInt(
                        temLeft.value + ((Ast.Exp.CvaNumberInt) this.lastExp).value,
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
        else if (temLeft instanceof Ast.Exp.CvaTrueExpr
                && temRight instanceof Ast.Exp.CvaTrueExpr)
            this.lastExp = temLeft;
        else if (temLeft instanceof Ast.Exp.CvaTrueExpr)
            this.lastExp = temRight;
        else if (temRight instanceof Ast.Exp.CvaTrueExpr)
            this.lastExp = temLeft;
        else
        {
            this.isOptimizing = false;
            this.lastExp = e;
        }
    }

    @Override
    public void visit(Ast.Exp.Function e)
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
    public void visit(Ast.Exp.Identifier e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.LT e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Exp.CvaNumberInt temLeft = (Ast.Exp.CvaNumberInt) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = temLeft.value < ((Ast.Exp.CvaNumberInt) this.lastExp).value
                        ? new Ast.Exp.CvaTrueExpr(this.lastExp.lineNum)
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
    public void visit(Ast.Exp.CvaNegateExpr e)
    {
        this.visit(e.expr);
        if (isConstant())
        {
            this.isOptimizing = true;
            this.lastExp = this.lastExp instanceof Ast.Exp.CvaTrueExpr
                    ? new Ast.Exp.False(this.lastExp.lineNum)
                    : new Ast.Exp.CvaTrueExpr(this.lastExp.lineNum);
        }
        else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.CvaNumberInt e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.CvaSubExpr e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Exp.CvaNumberInt temLeft = (Ast.Exp.CvaNumberInt) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = new Ast.Exp.CvaNumberInt(
                        temLeft.value - ((Ast.Exp.CvaNumberInt) this.lastExp).value,
                        this.lastExp.lineNum);
            }
            else this.lastExp = new Ast.Exp.CvaSubExpr(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.CvaThisExpr e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.CvaMuliExpr e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Exp.CvaNumberInt temLeft = (Ast.Exp.CvaNumberInt) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = new Ast.Exp.CvaNumberInt(
                        temLeft.value * ((Ast.Exp.CvaNumberInt) this.lastExp).value,
                        this.lastExp.lineNum);
            }
            else this.lastExp = new Ast.Exp.CvaMuliExpr(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Exp.CvaTrueExpr e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Stm.CvaAssign s)
    {
        this.visit(s.exp);
        s.exp = this.lastExp;
    }

    @Override
    public void visit(Ast.Stm.CvaBlock s)
    {
        s.stms.forEach(this::visit);
    }

    @Override
    public void visit(Ast.Stm.CvaIfStatement s)
    {
        this.visit(s.condition);
        s.condition = this.lastExp;
        this.visit(s.thenStm);
        this.visit(s.elseStm);
    }

    @Override
    public void visit(Ast.Stm.CvaWriteOperation s)
    {
        this.visit(s.exp);
        s.exp = this.lastExp;
    }

    @Override
    public void visit(Ast.Stm.CvaWhileStatement s)
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
