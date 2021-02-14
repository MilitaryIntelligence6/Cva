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
        else if (e instanceof Exp.Function)
            this.visit(((Exp.Function) e));
        else if (e instanceof Exp.False)
            this.visit(((Exp.False) e));
        else if (e instanceof Exp.Identifier)
            this.visit(((Exp.Identifier) e));
        else if (e instanceof Exp.LT)
            this.visit(((Exp.LT) e));
        else if (e instanceof Exp.NewObject)
            this.visit(((Exp.NewObject) e));
        else if (e instanceof Exp.CvaNegateExpr)
            this.visit(((Exp.CvaNegateExpr) e));
        else if (e instanceof Exp.CvaNumberInt)
            this.visit(((Exp.CvaNumberInt) e));
        else if (e instanceof Exp.CvaSubExpr)
            this.visit(((Exp.CvaSubExpr) e));
        else if (e instanceof Exp.CvaThisExpr)
            this.visit(((Exp.CvaThisExpr) e));
        else if (e instanceof Exp.CvaMuliExpr)
            this.visit(((Exp.CvaMuliExpr) e));
        else // if (e instanceof Ast.Exp.True)
            this.visit(((Exp.CvaTrueExpr) e));
    }

    void visit(Exp.Add e);

    void visit(Exp.And e);

    void visit(Exp.Function e);

    void visit(Exp.False e);

    void visit(Exp.Identifier e);

    void visit(Exp.LT e);

    void visit(Exp.NewObject e);

    void visit(Exp.CvaNegateExpr e);

    void visit(Exp.CvaNumberInt e);

    void visit(Exp.CvaSubExpr e);

    void visit(Exp.CvaThisExpr e);

    void visit(Exp.CvaMuliExpr e);

    void visit(Exp.CvaTrueExpr e);

    // Stm
    default void visit(Stm.T s)
    {
        if (s instanceof Stm.CvaAssign)
            this.visit(((Stm.CvaAssign) s));
        else if (s instanceof Stm.CvaBlock)
            this.visit(((Stm.CvaBlock) s));
        else if (s instanceof Stm.CvaIfStatement)
            this.visit(((Stm.CvaIfStatement) s));
        else if (s instanceof Stm.CvaWriteOperation)
            this.visit(((Stm.CvaWriteOperation) s));
        else // if (s instanceof Ast.Stm.While)
            this.visit(((Stm.CvaWhileStatement) s));
    }

    void visit(Stm.CvaAssign s);

    void visit(Stm.CvaBlock s);

    void visit(Stm.CvaIfStatement s);

    void visit(Stm.CvaWriteOperation s);

    void visit(Stm.CvaWhileStatement s);

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
