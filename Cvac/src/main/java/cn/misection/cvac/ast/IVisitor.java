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
 * Created by MI6 root 1/7.
 */
public interface IVisitor
{
    // Type
    default void visit(AbstractType t)
    {
        switch (t.getClass().getSimpleName())
        {
            case CvaType.CVA_BOOLEAN:
            {
                visit((CvaBoolean) t);
                break;
            }
            case CvaType.CVA_CLASS_TYPE:
            {
                visit((CvaClassType) t);
                break;
            }
            case CvaType.CVA_INT:
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

    /**
     * 做成map之后, 可以精简代码分散, 但是这里要用反射, 算了;
     * @param e
     */
    default void visit(AbstractExpression e)
    {
        switch (e.getClass().getSimpleName())
        {
            case CvaExpr.CVA_ADD_EXPR:
            {
                visit((CvaAddExpr) e);
                break;
            }
            case CvaExpr.CVA_AND_AND_EXPR:
            {
                visit((CvaAndAndExpr) e);
                break;
            }
            case CvaExpr.CVA_CALL_EXPR:
            {
                visit((CvaCallExpr) e);
                break;
            }
            case CvaExpr.CVA_FALSE_EXPR:
            {
                visit((CvaFalseExpr) e);
                break;
            }
            case CvaExpr.CVA_IDENTIFIER:
            {
                visit((CvaIdentifier) e);
                break;
            }
            case CvaExpr.CVA_LESS_THAN_EXPR:
            {
                visit((CvaLessThanExpr) e);
                break;
            }
            case CvaExpr.CVA_NEW_EXPR:
            {
                visit((CvaNewExpr) e);
                break;
            }
            case CvaExpr.CVA_NEGATE_EXPR:
            {
                visit((CvaNegateExpr) e);
                break;
            }
            case CvaExpr.CVA_NUMBER_INT_EXPR:
            {
                visit((CvaNumberIntExpr) e);
                break;
            }
            case CvaExpr.CVA_SUB_EXPR:
            {
                visit((CvaSubExpr) e);
                break;
            }
            case CvaExpr.CVA_THIS_EXPR:
            {
                visit((CvaThisExpr) e);
                break;
            }
            case CvaExpr.CVA_MULI_EXPR:
            {
                visit((CvaMuliExpr) e);
                break;
            }
            case CvaExpr.CVA_TRUE_EXPR:
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

    void visit(CvaLessThanExpr e);

    void visit(CvaNewExpr e);

    void visit(CvaNegateExpr e);

    void visit(CvaNumberIntExpr e);

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
