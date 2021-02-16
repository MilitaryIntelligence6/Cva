package cn.misection.cvac.ast;

import cn.misection.cvac.ast.clas.AbstractClass;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.AbstractProgram;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;

/**
 * Created by Mengxu on 2017/1/7.
 */
public interface IVisitor
{
    // Type
    default void visit(AbstractType t)
    {
        switch (t.getClass().getSimpleName())
        {
            case CvaType.CVABOOLEAN:
            {
                visit((CvaBoolean) t);
                break;
            }
            case CvaType.CVACLASSTYPE:
            {
                visit((CvaClassType) t);
                break;
            }
            case CvaType.CVAINT:
            {
                visit((CvaInt) t);
                break;
            }
            default:
            {
                System.err.println("unknown type");
                break;
            }
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
        switch (e.getClass().getSimpleName())
        {
            case CvaExpr.CVAADDEXPR:
            {
                visit((CvaAddExpr) e);
                break;
            }
            case CvaExpr.CVAANDANDEXPR:
            {
                visit((CvaAndAndExpr) e);
                break;
            }
            case CvaExpr.CVACALLEXPR:
            {
                visit((CvaCallExpr) e);
                break;
            }
            case CvaExpr.CVAFALSEEXPR:
            {
                visit((CvaFalseExpr) e);
                break;
            }
            case CvaExpr.CVAIDENTIFIER:
            {
                visit((CvaIdentifier) e);
                break;
            }
            case CvaExpr.CVALTEXPR:
            {
                visit((CvaLTExpr) e);
                break;
            }
            case CvaExpr.CVANEWEXPR:
            {
                visit((CvaNewExpr) e);
                break;
            }
            case CvaExpr.CVANEGATEEXPR:
            {
                visit((CvaNegateExpr) e);
                break;
            }
            case CvaExpr.CVANUMBERINT:
            {
                visit((CvaNumberInt) e);
                break;
            }
            case CvaExpr.CVASUBEXPR:
            {
                visit((CvaSubExpr) e);
                break;
            }
            case CvaExpr.CVATHISEXPR:
            {
                visit((CvaThisExpr) e);
                break;
            }
            case CvaExpr.CVAMULIEXPR:
            {
                visit((CvaMuliExpr) e);
                break;
            }
            case CvaExpr.CVATRUEEXPR:
            {
                visit((CvaTrueExpr) e);
                break;
            }
            default:
            {
                System.err.println("unknown expr");
                break;
            }
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
        switch (s.getClass().getSimpleName())
        {
            case CvaStatement.CVAASSIGN:
            {
                visit((CvaAssign) s);
                break;
            }
            case CvaStatement.CVABLOCK:
            {
                visit((CvaBlock) s);
                break;
            }
            case CvaStatement.CVAIFSTATEMENT:
            {
                visit((CvaIfStatement) s);
                break;
            }
            case CvaStatement.CVAWRITEOPERATION:
            {
                visit((CvaWriteOperation) s);
                break;
            }
            case CvaStatement.CVA_WHILE_STATEMENT:
            {
                visit((CvaWhileStatement) s);
                break;
            }
            default:
            {
                System.err.println("unknown statement");
                break;
            }
        }
    }

    void visit(CvaAssign s);

    void visit(CvaBlock s);

    void visit(CvaIfStatement s);

    void visit(CvaWriteOperation s);

    void visit(CvaWhileStatement s);

    // Method
    default void visit(AbstractMethod abstractMethod)
    {
        this.visit(((CvaMethod) abstractMethod));
    }

    void visit(CvaMethod cvaMethod);

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
