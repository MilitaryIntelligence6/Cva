package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.Ast;

import java.util.HashMap;

/**
 * Created by Mengxu on 2017/1/28.
 */
public class ConstantAndCopyPropagation implements cn.misection.cvac.ast.Visitor, Optimizable
{
    private HashMap<String, Ast.Exp.T> conorcopy; // constant or copy in current method
    private Ast.Exp.T curExp;
    private boolean canChange;
    private boolean inWhile; // if in while body, the left of assign should be delete from conorcopy
    private boolean isOptimizing;

    private boolean isEqual(Ast.Exp.T fir, Ast.Exp.T sec)
    {
        return (fir instanceof Ast.Exp.CvaNumberInt
                && sec instanceof Ast.Exp.CvaNumberInt
                && ((Ast.Exp.CvaNumberInt) fir).value == ((Ast.Exp.CvaNumberInt) sec).value)
                || (fir instanceof Ast.Exp.Identifier
                && sec instanceof Ast.Exp.Identifier
                && ((Ast.Exp.Identifier) fir).literal.equals(((Ast.Exp.Identifier) sec).literal));

    }

    private HashMap<String, Ast.Exp.T> intersection(
            HashMap<String, Ast.Exp.T> first,
            HashMap<String, Ast.Exp.T> second)
    {
        HashMap<String, Ast.Exp.T> result = new HashMap<>();
        first.forEach((k, v) ->
        {
            if (second.containsKey(k) && isEqual(v, second.get(k)))
                result.put(k, v);
        });
        return result;
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
        if (this.canChange)
            e.left = this.curExp;
        this.visit(e.right);
        if (this.canChange)
            e.right = this.curExp;
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Exp.And e)
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
    public void visit(Ast.Exp.Function e)
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
    public void visit(Ast.Exp.False e)
    {
        this.curExp = e;
        this.canChange = true;
    }

    @Override
    public void visit(Ast.Exp.Identifier e)
    {
        if (this.conorcopy.containsKey(e.literal))
        {
            this.isOptimizing = true;
            this.canChange = true;
            this.curExp = this.conorcopy.get(e.literal);
        } else this.canChange = false;
    }

    @Override
    public void visit(Ast.Exp.LT e)
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
    public void visit(Ast.Exp.NewObject e)
    {
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Exp.CvaNegateExpr e)
    {
        this.visit(e.expr);
        if (this.canChange)
            e.expr = this.curExp;
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Exp.CvaNumberInt e)
    {
        this.curExp = e;
        this.canChange = true;
    }

    @Override
    public void visit(Ast.Exp.CvaSubExpr e)
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
    public void visit(Ast.Exp.CvaThisExpr e)
    {
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Exp.CvaMuliExpr e)
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
    public void visit(Ast.Exp.CvaTrueExpr e)
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

        if (s.exp instanceof Ast.Exp.Identifier || s.exp instanceof Ast.Exp.CvaNumberInt)
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

        HashMap<String, Ast.Exp.T> _original = new HashMap<>();
        this.conorcopy.forEach(_original::put);
        this.visit(s.thenStm);

        HashMap<String, Ast.Exp.T> _left = this.conorcopy;
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
    public void visit(Ast.Method.MethodSingle m)
    {
        this.conorcopy = new HashMap<>();
        m.stms.forEach(this::visit);
        this.visit(m.retExp);
        if (this.canChange)
            m.retExp = this.curExp;
    }

    @Override
    public void visit(Ast.Class.ClassSingle c)
    {
        c.methods.forEach(m ->
        {
            this.canChange = false;
            this.visit(m);
        });
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
        return isOptimizing;
    }
}
