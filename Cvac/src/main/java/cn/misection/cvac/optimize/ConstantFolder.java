package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.Ast;

/**
 * Created by Mengxu on 2017/1/23.
 */
public class ConstantFolder implements cn.misection.cvac.ast.Visitor, Optimizable
{
    private Ast.Expr.T lastExp;
    private boolean isOptimizing;

    private boolean isConstant()
    {
        return lastExp != null && this.isConstant(this.lastExp);
    }

    private boolean isConstant(Ast.Expr.T exp)
    {
        return exp instanceof Ast.Expr.CvaNumberInt
                || exp instanceof Ast.Expr.CvaTrueExpr
                || exp instanceof Ast.Expr.CvaFalseExpr;
    }

    @Override
    public void visit(Ast.Type.CvaBoolean t) {}

    @Override
    public void visit(Ast.Type.CvaClass t) {}

    @Override
    public void visit(Ast.Type.Int t) {}

    @Override
    public void visit(Ast.Decl.CvaDeclaration d) {}

    @Override
    public void visit(Ast.Expr.CvaAddExpr e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Expr.CvaNumberInt temLeft = (Ast.Expr.CvaNumberInt) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = new Ast.Expr.CvaNumberInt(
                        temLeft.value + ((Ast.Expr.CvaNumberInt) this.lastExp).value,
                        this.lastExp.lineNum);
            } else this.lastExp = new Ast.Expr.CvaAddExpr(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaAndAndExpr e)
    {
        this.visit(e.left);
        Ast.Expr.T temLeft = this.lastExp;
        this.visit(e.right);
        Ast.Expr.T temRight = this.lastExp;

        this.isOptimizing = true;
        if (temLeft instanceof Ast.Expr.CvaFalseExpr
                || temRight instanceof Ast.Expr.CvaFalseExpr)
            this.lastExp = new Ast.Expr.CvaFalseExpr(e.lineNum);
        else if (temLeft instanceof Ast.Expr.CvaTrueExpr
                && temRight instanceof Ast.Expr.CvaTrueExpr)
            this.lastExp = temLeft;
        else if (temLeft instanceof Ast.Expr.CvaTrueExpr)
            this.lastExp = temRight;
        else if (temRight instanceof Ast.Expr.CvaTrueExpr)
            this.lastExp = temLeft;
        else
        {
            this.isOptimizing = false;
            this.lastExp = e;
        }
    }

    @Override
    public void visit(Ast.Expr.CvaCallExpr e)
    {
        java.util.LinkedList<Ast.Expr.T> _args = new java.util.LinkedList<>();
        e.args.forEach(arg ->
        {
            this.visit(arg);
            _args.add(this.lastExp);
        });
        e.args = _args;
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaFalseExpr e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaIdentifier e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaLTExpr e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Expr.CvaNumberInt temLeft = (Ast.Expr.CvaNumberInt) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = temLeft.value < ((Ast.Expr.CvaNumberInt) this.lastExp).value
                        ? new Ast.Expr.CvaTrueExpr(this.lastExp.lineNum)
                        : new Ast.Expr.CvaFalseExpr(this.lastExp.lineNum);
            }
            else this.lastExp = new Ast.Expr.CvaLTExpr(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaNewExpr e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaNegateExpr e)
    {
        this.visit(e.expr);
        if (isConstant())
        {
            this.isOptimizing = true;
            this.lastExp = this.lastExp instanceof Ast.Expr.CvaTrueExpr
                    ? new Ast.Expr.CvaFalseExpr(this.lastExp.lineNum)
                    : new Ast.Expr.CvaTrueExpr(this.lastExp.lineNum);
        }
        else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaNumberInt e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaSubExpr e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Expr.CvaNumberInt temLeft = (Ast.Expr.CvaNumberInt) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = new Ast.Expr.CvaNumberInt(
                        temLeft.value - ((Ast.Expr.CvaNumberInt) this.lastExp).value,
                        this.lastExp.lineNum);
            }
            else this.lastExp = new Ast.Expr.CvaSubExpr(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaThisExpr e)
    {
        this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaMuliExpr e)
    {
        this.visit(e.left);
        if (isConstant())
        {
            Ast.Expr.CvaNumberInt temLeft = (Ast.Expr.CvaNumberInt) this.lastExp;
            this.visit(e.right);
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExp = new Ast.Expr.CvaNumberInt(
                        temLeft.value * ((Ast.Expr.CvaNumberInt) this.lastExp).value,
                        this.lastExp.lineNum);
            }
            else this.lastExp = new Ast.Expr.CvaMuliExpr(temLeft, this.lastExp, this.lastExp.lineNum);
        } else this.lastExp = e;
    }

    @Override
    public void visit(Ast.Expr.CvaTrueExpr e)
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
    public void visit(Ast.Method.CvaMethod m)
    {
        m.stms.forEach(this::visit);
        this.visit(m.retExp);
        m.retExp = this.lastExp;
    }

    @Override
    public void visit(Ast.Clas.CvaClass c)
    {
        c.methods.forEach(this::visit);
    }

    @Override
    public void visit(Ast.MainClass.CvaEntry c)
    {
        this.visit(c.stm);
    }

    @Override
    public void visit(Ast.Program.CvaProgram p)
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
