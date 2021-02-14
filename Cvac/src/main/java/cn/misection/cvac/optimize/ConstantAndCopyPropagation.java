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
        return (fir instanceof Ast.Exp.Num
                && sec instanceof Ast.Exp.Num
                && ((Ast.Exp.Num) fir).num == ((Ast.Exp.Num) sec).num)
                || (fir instanceof Ast.Exp.Id
                && sec instanceof Ast.Exp.Id
                && ((Ast.Exp.Id) fir).id.equals(((Ast.Exp.Id) sec).id));

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
    public void visit(Ast.Exp.Call e)
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
    public void visit(Ast.Exp.Id e)
    {
        if (this.conorcopy.containsKey(e.id))
        {
            this.isOptimizing = true;
            this.canChange = true;
            this.curExp = this.conorcopy.get(e.id);
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
    public void visit(Ast.Exp.Not e)
    {
        this.visit(e.exp);
        if (this.canChange)
            e.exp = this.curExp;
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Exp.Num e)
    {
        this.curExp = e;
        this.canChange = true;
    }

    @Override
    public void visit(Ast.Exp.Sub e)
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
    public void visit(Ast.Exp.This e)
    {
        this.canChange = false;
    }

    @Override
    public void visit(Ast.Exp.Times e)
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
    public void visit(Ast.Exp.True e)
    {
        this.curExp = e;
        this.canChange = true;
    }

    @Override
    public void visit(Ast.Stm.Assign s)
    {
        if (this.inWhile)
        {
            if (this.conorcopy.containsKey(s.id))
                this.conorcopy.remove(s.id);
            return;
        }

        if (s.exp instanceof Ast.Exp.Id || s.exp instanceof Ast.Exp.Num)
            this.conorcopy.put(s.id, s.exp);
        else
        {
            this.visit(s.exp);
            if (this.canChange) s.exp = this.curExp;
        }
    }

    @Override
    public void visit(Ast.Stm.Block s)
    {
        s.stms.forEach(this::visit);
    }

    @Override
    public void visit(Ast.Stm.If s)
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
    public void visit(Ast.Stm.Write s)
    {
        if (this.inWhile) return;

        this.visit(s.exp);
        if (this.canChange)
            s.exp = curExp;
    }

    @Override
    public void visit(Ast.Stm.While s)
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
