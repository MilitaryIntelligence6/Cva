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
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Expr.CvaAndAndExpr e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Expr.CvaCallExpr e)
    {
        this.visit(e.exp);
        e.args.forEach(this::visit);
        this.containsCall = true;
    }

    @Override
    public void visit(Ast.Expr.CvaFalseExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaIdentifier e)
    {
        if (this.localVars.contains(e.literal))
            this.localLiveness.add(e.literal);
    }

    @Override
    public void visit(Ast.Expr.CvaLTExpr e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Expr.CvaNewExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaNegateExpr e)
    {
        this.visit(e.expr);
    }

    @Override
    public void visit(Ast.Expr.CvaNumberInt e) {}

    @Override
    public void visit(Ast.Expr.CvaSubExpr e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Expr.CvaThisExpr e) {}

    @Override
    public void visit(Ast.Expr.CvaMuliExpr e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Expr.CvaTrueExpr e) {}

    @Override
    public void visit(Ast.Stm.CvaAssign s)
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
    public void visit(Ast.Stm.CvaBlock s)
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
    public void visit(Ast.Stm.CvaIfStatement s)
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
    public void visit(Ast.Stm.CvaWriteOperation s)
    {
        // this.isAssign = false;
        this.visit(s.exp);
        this.shouldDel = false;
    }

    @Override
    public void visit(Ast.Stm.CvaWhileStatement s)
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
    public void visit(Ast.Method.CvaMethod m)
    {
        this.localVars = new HashSet<>();
        m.formals.forEach(f -> this.localVars.add(((Ast.Decl.CvaDeclaration) f).literal));
        m.locals.forEach(l -> this.localVars.add(((Ast.Decl.CvaDeclaration) l).literal));
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
    public void visit(Ast.Clas.CvaClass c)
    {
        this.curFields = new HashSet<>();
        c.fields.forEach(f ->
                this.curFields.add(((Ast.Decl.CvaDeclaration) f).literal));

        c.methods.forEach(this::visit);
    }

    @Override
    public void visit(Ast.MainClass.CvaEntry c) {}

    @Override
    public void visit(Ast.Program.CvaProgram p)
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
