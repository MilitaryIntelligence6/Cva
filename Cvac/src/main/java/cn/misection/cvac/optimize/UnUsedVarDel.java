package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.Ast;

import java.util.Hashtable;

/**
 * Created by Mengxu on 2017/1/24.
 */
public class UnUsedVarDel implements cn.misection.cvac.ast.Visitor, Optimizable
{
    private Hashtable<String, Ast.Dec.DecSingle> unUsedLocals;
    private Hashtable<String, Ast.Dec.DecSingle> unUsedArgs;
    private boolean isOptimizing;
    public boolean givesWarning;

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
    public void visit(Ast.Exp.Function e)
    {
        this.visit(e.exp);
        e.args.forEach(this::visit);
    }

    @Override
    public void visit(Ast.Exp.False e) {}

    @Override
    public void visit(Ast.Exp.Identifier e)
    {
        if (this.unUsedLocals.containsKey(e.literal))
            this.unUsedLocals.remove(e.literal);
        else if (this.unUsedArgs.containsKey(e.literal))
            this.unUsedArgs.remove(e.literal);
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
    public void visit(Ast.Exp.CvaNegateExpr e)
    {
        this.visit(e.expr);
    }

    @Override
    public void visit(Ast.Exp.CvaNumberInt e) {}

    @Override
    public void visit(Ast.Exp.CvaSubExpr e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Exp.CvaThisExpr e) {}

    @Override
    public void visit(Ast.Exp.CvaMuliExpr e)
    {
        this.visit(e.left);
        this.visit(e.right);
    }

    @Override
    public void visit(Ast.Exp.CvaTrueExpr e) {}

    @Override
    public void visit(Ast.Stm.CvaAssign s)
    {
        this.visit(new Ast.Exp.Identifier(s.id, s.lineNum));
        this.visit(s.exp);
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
        this.visit(s.thenStm);
        this.visit(s.elseStm);
    }

    @Override
    public void visit(Ast.Stm.CvaWriteOperation s)
    {
        this.visit(s.exp);
    }

    @Override
    public void visit(Ast.Stm.CvaWhileStatement s)
    {
        this.visit(s.condition);
        this.visit(s.body);
    }

    @Override
    public void visit(Ast.Method.MethodSingle m)
    {
        this.unUsedLocals = new Hashtable<>();
        m.locals.forEach(local ->
        {
            Ast.Dec.DecSingle l = (Ast.Dec.DecSingle) local;
            this.unUsedLocals.put(l.id, l);
        });

        this.unUsedArgs = new Hashtable<>();
        m.formals.forEach(formal ->
        {
            Ast.Dec.DecSingle f = (Ast.Dec.DecSingle) formal;
            this.unUsedArgs.put(f.id, f);
        });

        m.stms.forEach(this::visit);
        this.visit(m.retExp);

        this.isOptimizing = this.unUsedArgs.size() > 0
                || this.unUsedLocals.size() > 0;
        this.unUsedArgs.forEach((uak, uao) ->
        {
            if (givesWarning)
                System.out.println("Warning: at line " + uao.lineNum + " : "
                        + "the argument \"" + uak + "\" of method \""
                        + m.id + "\" you have never used.");
        });

        this.unUsedLocals.forEach((ulk, ulo) ->
        {
            if (givesWarning)
                System.out.println("Warning: at line " + ulo.lineNum + " : "
                        + "the local variable \"" + ulk + "\" you have never used."
                        + " Now we delete it.");
            m.locals.remove(ulo);
        });
    }

    @Override
    public void visit(Ast.Class.ClassSingle c)
    {
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
