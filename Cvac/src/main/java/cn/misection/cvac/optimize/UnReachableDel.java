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
    public void visit(Ast.Type.CvaBoolean t) {}

    @Override
    public void visit(Ast.Type.CvaClass t) {}

    @Override
    public void visit(Ast.Type.Int t) {}

    @Override
    public void visit(Ast.Decl.CvaDeclaration d) {}

    @Override
    public void visit(Ast.Expr.CvaAddExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaAndAndExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaCallExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaFalseExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaIdentifier e) {}

    @Override
    public void visit(Ast.Expr.CvaLTExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaNewExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaNegateExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaNumberInt e) {}

    @Override
    public void visit(Ast.Expr.CvaSubExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaThisExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaMuliExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaTrueExpr e) {}

    @Override
    public void visit(Ast.Stm.CvaAssign s)
    {
        this.curStm = s;
    }

    @Override
    public void visit(Ast.Stm.CvaBlock s)
    {
        LinkedList<Ast.Stm.T> _stms = new LinkedList<>();
        s.stms.forEach(stm ->
        {
            this.visit(stm);
            if (curStm != null)
                if (curStm instanceof Ast.Stm.CvaBlock)
                    ((Ast.Stm.CvaBlock) curStm).stms.forEach(_stms::add);
                else _stms.add(curStm);
        });
        s.stms = _stms;
        this.curStm = s;
    }

    @Override
    public void visit(Ast.Stm.CvaIfStatement s)
    {
        if (s.condition instanceof Ast.Expr.CvaTrueExpr)
        {
            this.isOptimizing = true;
            this.curStm = s.thenStm;
            this.visit(this.curStm);
        } else if (s.condition instanceof Ast.Expr.CvaFalseExpr)
        {
            this.isOptimizing = true;
            this.curStm = s.elseStm;
            this.visit(this.curStm);
        } else this.curStm = s;
    }

    @Override
    public void visit(Ast.Stm.CvaWriteOperation s)
    {
        this.curStm = s;
    }

    @Override
    public void visit(Ast.Stm.CvaWhileStatement s)
    {
        if (s.condition instanceof Ast.Expr.CvaFalseExpr)
        {
            this.isOptimizing = true;
            this.curStm = null;
        }
        else if (s.condition instanceof Ast.Expr.CvaTrueExpr)
        {
            System.out.println("Warning: at line " + s.lineNum
                    + " : " + "unend-loop!");
            this.curStm = s;
        } else this.curStm = s;

    }

    @Override
    public void visit(Ast.Method.CvaMethod m)
    {
        LinkedList<Ast.Stm.T> _stms = new LinkedList<>();
        m.stms.forEach(stm ->
        {
            this.visit(stm);
            if (this.curStm != null)
                if (this.curStm instanceof Ast.Stm.CvaBlock)
                    ((Ast.Stm.CvaBlock) this.curStm).stms.forEach(_stms::add);
                else _stms.add(curStm);
        });
        m.stms = _stms;
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
        c.stm = this.curStm;
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
