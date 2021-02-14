package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.Ast;

import java.util.HashMap;

/**
 * Created by Mengxu on 2017/1/28.
 */
public class ConstantAndCopyPropagation implements cn.misection.cvac.ast.Visitor, Optimizable
{
    private HashMap<String, Ast.Expr.T> conorcopy; // constant or copy in current method
    private Ast.Expr.T curExp;
    private boolean canChange;
    private boolean inWhile; // if in while body, the left of assign should be delete from conorcopy
    private boolean isOptimizing;

    private boolean isEqual(Ast.Expr.T fir, Ast.Expr.T sec)
    {
        return (fir instanceof Ast.Expr.CvaNumberInt
                && sec instanceof Ast.Expr.CvaNumberInt
                && ((Ast.Expr.CvaNumberInt) fir).value == ((Ast.Expr.CvaNumberInt) sec).value)
                || (fir instanceof Ast.Expr.CvaIdentifier
                && sec instanceof Ast.Expr.CvaIdentifier
                && ((Ast.Expr.CvaIdentifier) fir).literal.equals(((Ast.Expr.CvaIdentifier) sec).literal));

    }

    private HashMap<String, Ast.Expr.T> intersection(
            HashMap<String, Ast.Expr.T> first,
            HashMap<String, Ast.Expr.T> second)
    {
        HashMap<String, Ast.Expr.T> result = new HashMap<>();
        first.forEach((k, v) ->
        {
            if (second.containsKey(k) && isEqual(v, second.get(k)))
                result.put(k, v);
        });
        return result;
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
        if (this.canChange)
            e.left = this.curExp;
        this.visit(e.right);
        if (this.canChange)
            e.right = this.curExp;
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaAndAndExpr e)
    {
        this.visit(e.left);
        if (this.canChange)
            e.left = this.curExp;
        this.visit(e.right);
        if (this.canChange)
            e.right = this.curExp;
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaCallExpr e)
    {
        this.visit(e.exp);
        for (int i = 0; i < e.args.size(); i++)
        {
            this.visit(e.args.get(i));
            if (this.canChange)
                e.args.set(i, this.curExp);
        }
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaFalseExpr e)
    {
        this.curExp = e;
        this.canChange = true;
    }

    @Override
    public void visit(Ast.Expr.CvaIdentifier e)
    {
        if (this.conorcopy.containsKey(e.literal))
        {
            this.isOptimizing = true;
            this.canChange = true;
            this.curExp = this.conorcopy.get(e.literal);
        } else this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaLTExpr e)
    {
        this.visit(e.left);
        if (this.canChange)
            e.left = this.curExp;
        this.visit(e.right);
        if (this.canChange)
            e.right = this.curExp;
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaNewExpr e)
    {
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaNegateExpr e)
    {
        this.visit(e.expr);
        if (this.canChange)
            e.expr = this.curExp;
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaNumberInt e)
    {
        this.curExp = e;
        this.canChange = true;
    }

    @Override
    public void visit(Ast.Expr.CvaSubExpr e)
    {
        this.visit(e.left);
        if (this.canChange)
            e.left = this.curExp;
        this.visit(e.right);
        if (this.canChange)
            e.right = this.curExp;
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaThisExpr e)
    {
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaMuliExpr e)
    {
        this.visit(e.left);
        if (this.canChange)
            e.left = this.curExp;
        this.visit(e.right);
        if (this.canChange)
            e.right = this.curExp;
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Expr.CvaTrueExpr e)
    {
        this.curExp = e;
        this.canChange = true;
    }

    @Override
    public void visit(Ast.Stm.CvaAssign s)
    {
        if (this.inWhile)
        {
            if (this.conorcopy.containsKey(s.id))
                this.conorcopy.remove(s.id);
            return;
        }

        if (s.exp instanceof Ast.Expr.CvaIdentifier || s.exp instanceof Ast.Expr.CvaNumberInt)
            this.conorcopy.put(s.id, s.exp);
        else
        {
            this.visit(s.exp);
            if (this.canChange) s.exp = this.curExp;
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
        if (this.inWhile) return;

        this.visit(s.condition);
        if (this.canChange)
            s.condition = this.curExp;

        HashMap<String, Ast.Expr.T> _original = new HashMap<>();
        this.conorcopy.forEach(_original::put);
        this.visit(s.thenStm);

        HashMap<String, Ast.Expr.T> _left = this.conorcopy;
        this.conorcopy = _original;
        this.visit(s.elseStm);

        this.conorcopy = intersection(_left, this.conorcopy);
    }


    @Override
    public void visit(Ast.Stm.CvaWriteOperation s)
    {
        if (this.inWhile) return;

        this.visit(s.exp);
        if (this.canChange)
            s.exp = curExp;
    }

    @Override
    public void visit(Ast.Stm.CvaWhileStatement s)
    {
        // TODO: it is wrong when in multi-layer-loop
        // delete the var which be changed
        this.inWhile = true;
        this.visit(s.body);
        this.inWhile = false;

        this.visit(s.condition);
        this.visit(s.body);
    }

    @Override
    public void visit(Ast.Method.CvaMethod m)
    {
        this.conorcopy = new HashMap<>();
        m.stms.forEach(this::visit);
        this.visit(m.retExp);
        if (this.canChange)
            m.retExp = this.curExp;
    }

    @Override
    public void visit(Ast.Clas.CvaClass c)
    {
        c.methods.forEach(m ->
        {
            this.canChange = false;
            this.visit(m);
        });
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
        return isOptimizing;
    }
}
