package cn.misection.cvac.ast;

import cn.misection.cvac.ast.Ast.*;

/**
 * Created by Mengxu on 2017/1/7.
 */
public interface Visitor
{
    // Type
    default void visit(Type.T t)
    {
        if (t instanceof Type.Boolean)
            this.visit(((Type.Boolean) t));
        else if (t instanceof Type.ClassType)
            this.visit(((Type.ClassType) t));
        else if (t instanceof Type.Int)
            this.visit(((Type.Int) t));
    }

    void visit(Type.Boolean t);

    void visit(Type.ClassType t);

    void visit(Type.Int t);

    // Dec
    default void visit(Dec.T d)
    {
        this.visit(((Dec.DecSingle) d));
    }

    void visit(Dec.DecSingle d);

    // Exp
    default void visit(Exp.T e)
    {
        if (e instanceof Exp.Add)
            this.visit(((Exp.Add) e));
        else if (e instanceof Exp.And)
            this.visit(((Exp.And) e));
        else if (e instanceof Exp.Call)
            this.visit(((Exp.Call) e));
        else if (e instanceof Exp.False)
            this.visit(((Exp.False) e));
        else if (e instanceof Exp.Id)
            this.visit(((Exp.Id) e));
        else if (e instanceof Exp.LT)
            this.visit(((Exp.LT) e));
        else if (e instanceof Exp.NewObject)
            this.visit(((Exp.NewObject) e));
        else if (e instanceof Exp.Not)
            this.visit(((Exp.Not) e));
        else if (e instanceof Exp.Num)
            this.visit(((Exp.Num) e));
        else if (e instanceof Exp.Sub)
            this.visit(((Exp.Sub) e));
        else if (e instanceof Exp.This)
            this.visit(((Exp.This) e));
        else if (e instanceof Exp.Times)
            this.visit(((Exp.Times) e));
        else // if (e instanceof Ast.Exp.True)
            this.visit(((Exp.True) e));
    }

    void visit(Exp.Add e);

    void visit(Exp.And e);

    void visit(Exp.Call e);

    void visit(Exp.False e);

    void visit(Exp.Id e);

    void visit(Exp.LT e);

    void visit(Exp.NewObject e);

    void visit(Exp.Not e);

    void visit(Exp.Num e);

    void visit(Exp.Sub e);

    void visit(Exp.This e);

    void visit(Exp.Times e);

    void visit(Exp.True e);

    // Stm
    default void visit(Stm.T s)
    {
        if (s instanceof Stm.Assign)
            this.visit(((Stm.Assign) s));
        else if (s instanceof Stm.Block)
            this.visit(((Stm.Block) s));
        else if (s instanceof Stm.If)
            this.visit(((Stm.If) s));
        else if (s instanceof Stm.Write)
            this.visit(((Stm.Write) s));
        else // if (s instanceof Ast.Stm.While)
            this.visit(((Stm.While) s));
    }

    void visit(Stm.Assign s);

    void visit(Stm.Block s);

    void visit(Stm.If s);

    void visit(Stm.Write s);

    void visit(Stm.While s);

    // Method
    default void visit(Method.T m)
    {
        this.visit(((Method.MethodSingle) m));
    }

    void visit(Method.MethodSingle m);

    // Class
    default void visit(Ast.Class.T c)
    {
        this.visit(((Ast.Class.ClassSingle) c));
    }

    void visit(Ast.Class.ClassSingle c);

    default void visit(MainClass.T c)
    {
        this.visit(((MainClass.MainClassSingle) c));
    }

    void visit(MainClass.MainClassSingle c);

    // Program
    default void visit(Program.T p)
    {
        this.visit(((Program.ProgramSingle) p));
    }

    void visit(Program.ProgramSingle p);
}
