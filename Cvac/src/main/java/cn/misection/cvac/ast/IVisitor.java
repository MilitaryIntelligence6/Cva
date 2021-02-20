package cn.misection.cvac.ast;

import cn.misection.cvac.ast.clas.AbstractCvaClass;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.expr.binary.*;
import cn.misection.cvac.ast.expr.unary.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.AbstractProgram;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.advance.CvaStringType;
import cn.misection.cvac.constant.CvaExprClassName;

/**
 * Created by MI6 root 1/7.
 */
public interface IVisitor
{
    /**
     * type
     * @param type t;
     */
    default void visit(ICvaType type)
    {
//        switch (type.getClass().getSimpleName())
//        {
//            case CvaTypeClassName.CVA_BOOLEAN_TYPE:
//            {
//                visit((CvaBooleanType) type);
//                break;
//            }
//            case CvaTypeClassName.CVA_CLASS_TYPE:
//            {
//                visit((CvaClassType) type);
//                break;
//            }
//            case CvaTypeClassName.CVA_INT_TYPE:
//            {
//                visit((CvaIntType) type);
//                break;
//            }
//            case CvaTypeClassName.CVA_STRING_TYPE:
//            {
//                visit((CvaStringType) type);
//                break;
//            }
//            default:
//            {
//                System.err.println("unknown type");
//                break;
//            }
//        }
        // 不是枚举;
        if (type instanceof AbstractType)
        {
            switch (((AbstractType) type).toEnum())
            {
                case CVA_STRING:
                {
                    visit((CvaStringType) type);
                    break;
                }
                case CVA_POINTER:
                {
                    // TODO;
                    break;
                }
                case CVA_ARRAY:
                {
                    // TODO;
                    break;
                }
                case CVA_CLASS:
                {
                    visit((CvaClassType) type);
                }
                default:
                {
                    break;
                }
            }
        }
        // 基本枚举类型;
        else
        {
            visit((EnumCvaType) type);
        }
    }

    void visit(EnumCvaType basicType);

    void visit(CvaStringType type);

    void visit(CvaClassType type);


    /**
     * decl
     * @param abstDecl decl;
     */
    default void visit(AbstractDeclaration abstDecl)
    {
        visit(((CvaDeclaration) abstDecl));
    }

    void visit(CvaDeclaration decl);

    /**
     * 做成map之后, 可以精简代码分散, 但是这里要用反射, 算了;
     * @param expr
     */
    default void visit(AbstractExpression expr)
    {
        // 用typeCode代替, 可以避免反射以及改名麻烦;
        switch (expr.getClass().getSimpleName())
        {
            case CvaExprClassName.CVA_ADD_EXPR:
            {
                visit((CvaAddExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_AND_AND_EXPR:
            {
                visit((CvaAndAndExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_CALL_EXPR:
            {
                visit((CvaCallExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_CONST_FALSE_EXPR:
            {
                visit((CvaConstFalseExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_IDENTIFIER_EXPR:
            {
                visit((CvaIdentifierExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_LESS_THAN_EXPR:
            {
                visit((CvaLessThanExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_NEW_EXPR:
            {
                visit((CvaNewExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_NEGATE_EXPR:
            {
                visit((CvaNegateExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_CONST_INT_EXPR:
            {
                visit((CvaConstIntExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_CONST_STRING_EXPR:
            {
                visit((CvaConstStringExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_SUB_EXPR:
            {
                visit((CvaSubExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_THIS_EXPR:
            {
                visit((CvaThisExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_MUL_EXPR:
            {
                visit((CvaMulExpr) expr);
                break;
            }
            case CvaExprClassName.CVA_CONST_TRUE_EXPR:
            {
                visit((CvaConstTrueExpr) expr);
                break;
            }
            default:
            {
                System.err.println("unknown expr");
                break;
            }
        }
    }

    void visit(CvaAddExpr expr);

    void visit(CvaAndAndExpr expr);

    void visit(CvaCallExpr expr);

    void visit(CvaConstFalseExpr expr);

    void visit(CvaIdentifierExpr expr);

    void visit(CvaLessThanExpr expr);

    void visit(CvaNewExpr expr);

    void visit(CvaNegateExpr expr);

    void visit(CvaConstIntExpr expr);

    void visit(CvaConstStringExpr expr);

    void visit(CvaSubExpr expr);

    void visit(CvaThisExpr expr);

    void visit(CvaMulExpr expr);

    void visit(CvaConstTrueExpr expr);

    // Stm
    default void visit(AbstractStatement abstStm)
    {
        switch (abstStm.getClass().getSimpleName())
        {
            case CvaStatementClassName.CVA_ASSIGN_STATEMENT:
            {
                visit((CvaAssignStatement) abstStm);
                break;
            }
            case CvaStatementClassName.CVA_BLOCK_STATEMENT:
            {
                visit((CvaBlockStatement) abstStm);
                break;
            }
            case CvaStatementClassName.CVA_IF_STATEMENT:
            {
                visit((CvaIfStatement) abstStm);
                break;
            }
            case CvaStatementClassName.CVA_WRITE_STATEMENT:
            {
                visit((CvaWriteStatement) abstStm);
                break;
            }
            case CvaStatementClassName.CVA_WHILE_STATEMENT:
            {
                visit((CvaWhileStatement) abstStm);
                break;
            }
            default:
            {
                System.err.println("unknown statement");
                break;
            }
        }
    }

    void visit(CvaAssignStatement stm);

    void visit(CvaBlockStatement stm);

    void visit(CvaIfStatement stm);

    void visit(CvaWriteStatement stm);

    void visit(CvaWhileStatement stm);

    /**
     * Method
     */
    default void visit(AbstractMethod abstMethod)
    {
        visit(((CvaMethod) abstMethod));
    }

    void visit(CvaMethod cvaMethod);

    // Class
    default void visit(AbstractCvaClass abstClass)
    {
        visit(((CvaClass) abstClass));
    }

    void visit(CvaClass cvaClass);

    default void visit(AbstractEntryClass entryClass)
    {
        visit(((CvaEntryClass) entryClass));
    }

    void visit(CvaEntryClass entryClass);

    void visit(CvaMainMethod entryMethod);

    // Program
    default void visit(AbstractProgram program)
    {
        visit(((CvaProgram) program));
    }

    void visit(CvaProgram program);
}
