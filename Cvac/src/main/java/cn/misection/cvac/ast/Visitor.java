package cn.misection.cvac.ast;

import cn.misection.cvac.ast.clas.AbstractClass;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.*;
import cn.misection.cvac.ast.program.AbstractProgram;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;

/**
 * Created by Mengxu on 2017/1/7.
 */
public interface Visitor
{
    // Type
    default void visit(AbstractType t)
    {
        if (t instanceof CvaBoolean)
        {
            this.visit(((CvaBoolean) t));
        }
        else if (t instanceof CvaClassType)
        {
            this.visit(((CvaClassType) t));
        }
        else if (t instanceof CvaInt)
        {
            this.visit(((CvaInt) t));
        }
    }

    void visit(CvaBoolean t);

    void visit(CvaClassType t);

    void visit(CvaInt t);

    // Dec
    default void visit(AbstractDeclaration d)
    {
        this.visit(((CvaDeclaration) d));
    }

    void visit(CvaDeclaration d);

    // Exp
    default void visit(AbstractExpression e)
    {
        if (e instanceof CvaAddExpr)
        {
            this.visit(((CvaAddExpr) e));
        }
        else if (e instanceof CvaAndAndExpr)
        {
            this.visit(((CvaAndAndExpr) e));
        }
        else if (e instanceof CvaCallExpr)
        {
            this.visit(((CvaCallExpr) e));
        }
        else if (e instanceof CvaFalseExpr)
        {
            this.visit(((CvaFalseExpr) e));
        }
        else if (e instanceof CvaIdentifier)
        {
            this.visit(((CvaIdentifier) e));
        }
        else if (e instanceof CvaLTExpr)
        {
            this.visit(((CvaLTExpr) e));
        }
        else if (e instanceof CvaNewExpr)
        {
            this.visit(((CvaNewExpr) e));
        }
        else if (e instanceof CvaNegateExpr)
        {
            this.visit(((CvaNegateExpr) e));
        }
        else if (e instanceof CvaNumberInt)
        {
            this.visit(((CvaNumberInt) e));
        }
        else if (e instanceof CvaSubExpr)
        {
            this.visit(((CvaSubExpr) e));
        }
        else if (e instanceof CvaThisExpr)
        {
            this.visit(((CvaThisExpr) e));
        }
        else if (e instanceof CvaMuliExpr)
        {
            this.visit(((CvaMuliExpr) e));
        }
        else // if (e instanceof Ast.Exp.True)
        {
            this.visit(((CvaTrueExpr) e));
        }
    }

    void visit(CvaAddExpr e);

    void visit(CvaAndAndExpr e);

    void visit(CvaCallExpr e);

    void visit(CvaFalseExpr e);

    void visit(CvaIdentifier e);

    void visit(CvaLTExpr e);

    void visit(CvaNewExpr e);

    void visit(CvaNegateExpr e);

    void visit(CvaNumberInt e);

    void visit(CvaSubExpr e);

    void visit(CvaThisExpr e);

    void visit(CvaMuliExpr e);

    void visit(CvaTrueExpr e);

    // Stm
    default void visit(AbstractStatement s)
    {
        if (s instanceof CvaAssign)
        {
            this.visit(((CvaAssign) s));
        }
        else if (s instanceof CvaBlock)
        {
            this.visit(((CvaBlock) s));
        }
        else if (s instanceof CvaIfStatement)
        {
            this.visit(((CvaIfStatement) s));
        }
        else if (s instanceof CvaWriteOperation)
        {
            this.visit(((CvaWriteOperation) s));
        }
        else // if (s instanceof Ast.While)
        {
            this.visit(((CvaWhileStatement) s));
        }
    }

    void visit(CvaAssign s);

    void visit(CvaBlock s);

    void visit(CvaIfStatement s);

    void visit(CvaWriteOperation s);

    void visit(CvaWhileStatement s);

    // Method
    default void visit(AbstractMethod m)
    {
        this.visit(((CvaMethod) m));
    }

    void visit(CvaMethod method);

    // Class
    default void visit(AbstractClass abstractClass)
    {
        this.visit(((CvaClass) abstractClass));
    }

    void visit(CvaClass cvaClass);

    default void visit(AbstractEntry c)
    {
        this.visit(((CvaEntry) c));
    }

    void visit(CvaEntry c);

    // Program
    default void visit(AbstractProgram p)
    {
        this.visit(((CvaProgram) p));
    }

    void visit(CvaProgram p);
}
