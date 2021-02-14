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
        if (t instanceof Type.CvaBoolean)
        {
            this.visit(((Type.CvaBoolean) t));
        }
        else if (t instanceof Type.CvaClass)
        {
            this.visit(((Type.CvaClass) t));
        }
        else if (t instanceof Type.Int)
        {
            this.visit(((Type.Int) t));
        }
    }

    void visit(Type.CvaBoolean t);

    void visit(Type.CvaClass t);

    void visit(Type.Int t);

    // Dec
    default void visit(Decl.T d)
    {
        this.visit(((Decl.CvaDeclaration) d));
    }

    void visit(Decl.CvaDeclaration d);

    // Exp
    default void visit(Expr.T e)
    {
        if (e instanceof Expr.CvaAddExpr)
        {
            this.visit(((Expr.CvaAddExpr) e));
        }
        else if (e instanceof Expr.CvaAndAndExpr)
        {
            this.visit(((Expr.CvaAndAndExpr) e));
        }
        else if (e instanceof Expr.CvaCallExpr)
        {
            this.visit(((Expr.CvaCallExpr) e));
        }
        else if (e instanceof Expr.CvaFalseExpr)
        {
            this.visit(((Expr.CvaFalseExpr) e));
        }
        else if (e instanceof Expr.CvaIdentifier)
        {
            this.visit(((Expr.CvaIdentifier) e));
        }
        else if (e instanceof Expr.CvaLTExpr)
        {
            this.visit(((Expr.CvaLTExpr) e));
        }
        else if (e instanceof Expr.CvaNewExpr)
        {
            this.visit(((Expr.CvaNewExpr) e));
        }
        else if (e instanceof Expr.CvaNegateExpr)
        {
            this.visit(((Expr.CvaNegateExpr) e));
        }
        else if (e instanceof Expr.CvaNumberInt)
        {
            this.visit(((Expr.CvaNumberInt) e));
        }
        else if (e instanceof Expr.CvaSubExpr)
        {
            this.visit(((Expr.CvaSubExpr) e));
        }
        else if (e instanceof Expr.CvaThisExpr)
        {
            this.visit(((Expr.CvaThisExpr) e));
        }
        else if (e instanceof Expr.CvaMuliExpr)
        {
            this.visit(((Expr.CvaMuliExpr) e));
        }
        else // if (e instanceof Ast.Exp.True)
        {
            this.visit(((Expr.CvaTrueExpr) e));
        }
    }

    void visit(Expr.CvaAddExpr e);

    void visit(Expr.CvaAndAndExpr e);

    void visit(Expr.CvaCallExpr e);

    void visit(Expr.CvaFalseExpr e);

    void visit(Expr.CvaIdentifier e);

    void visit(Expr.CvaLTExpr e);

    void visit(Expr.CvaNewExpr e);

    void visit(Expr.CvaNegateExpr e);

    void visit(Expr.CvaNumberInt e);

    void visit(Expr.CvaSubExpr e);

    void visit(Expr.CvaThisExpr e);

    void visit(Expr.CvaMuliExpr e);

    void visit(Expr.CvaTrueExpr e);

    // Stm
    default void visit(Stm.T s)
    {
        if (s instanceof Stm.CvaAssign)
        {
            this.visit(((Stm.CvaAssign) s));
        }
        else if (s instanceof Stm.CvaBlock)
        {
            this.visit(((Stm.CvaBlock) s));
        }
        else if (s instanceof Stm.CvaIfStatement)
        {
            this.visit(((Stm.CvaIfStatement) s));
        }
        else if (s instanceof Stm.CvaWriteOperation)
        {
            this.visit(((Stm.CvaWriteOperation) s));
        }
        else // if (s instanceof Ast.Stm.While)
        {
            this.visit(((Stm.CvaWhileStatement) s));
        }
    }

    void visit(Stm.CvaAssign s);

    void visit(Stm.CvaBlock s);

    void visit(Stm.CvaIfStatement s);

    void visit(Stm.CvaWriteOperation s);

    void visit(Stm.CvaWhileStatement s);

    // Method
    default void visit(Method.T m)
    {
        this.visit(((Method.CvaMethod) m));
    }

    void visit(Method.CvaMethod m);

    // Class
    default void visit(Clas.T c)
    {
        this.visit(((Clas.CvaClass) c));
    }

    void visit(Clas.CvaClass c);

    default void visit(MainClass.T c)
    {
        this.visit(((MainClass.CvaEntry) c));
    }

    void visit(MainClass.CvaEntry c);

    // Program
    default void visit(Program.T p)
    {
        this.visit(((Program.CvaProgram) p));
    }

    void visit(Program.CvaProgram p);
}
