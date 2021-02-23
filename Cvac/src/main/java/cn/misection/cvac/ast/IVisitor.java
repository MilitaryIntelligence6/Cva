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
        // 不是枚举;
        if (type instanceof AbstractType)
        {
            switch ((type.toEnum()))
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
     * @param expr e;
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
            case CvaExprClassName.CVA_LESS_OR_MORE_THAN_EXPR:
            {
                visit((CvaLessOrMoreThanExpr) expr);
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
                // 将所有的都用绑定的enum判定;
                switch (expr.toEnum())
                {
                    case ADD:
                    case SUB:
                    case MUL:
                    case DIV:
                    case REM:
                    case BIT_AND:
                    case BIT_OR:
                    case BIT_XOR:
                    case LEFT_SHIFT:
                    case RIGHT_SHIFT:
                    case UNSIGNED_RIGHT_SHIFT:
                    {
                        visit((CvaOperandOperatorExpr) expr);
                        break;
                    }
                    default:
                    {
                        System.err.println("unknown expr");
                        break;
                    }
                }
            }
        }
    }

    void visit(CvaAddExpr expr);

    void visit(CvaAndAndExpr expr);

    void visit(CvaCallExpr expr);

    void visit(CvaConstFalseExpr expr);

    void visit(CvaIdentifierExpr expr);

    void visit(CvaLessOrMoreThanExpr expr);

    void visit(CvaNewExpr expr);

    void visit(CvaNegateExpr expr);

    void visit(CvaConstIntExpr expr);

    void visit(CvaConstStringExpr expr);

    void visit(CvaSubExpr expr);

    void visit(CvaThisExpr expr);

    void visit(CvaMulExpr expr);

    void visit(CvaConstTrueExpr expr);

    void visit(CvaOperandOperatorExpr expr);

    /**
     * stm;
     * @param abstStm stm;
     */
    default void visit(AbstractStatement abstStm)
    {
        switch (abstStm.toEnum())
        {
            case ASSIGN:
            {
                visit((CvaAssignStatement) abstStm);
                break;
            }
            case BLOCK:
            {
                visit((CvaBlockStatement) abstStm);
                break;
            }
            case IF:
            {
                visit((CvaIfStatement) abstStm);
                break;
            }
            case WRITE:
            {
                visit((CvaWriteStatement) abstStm);
                break;
            }
            case WHILE_FOR:
            {
                visit((CvaWhileForStatement) abstStm);
                break;
            }
            case INCREMENT:
            {
                visit((CvaIncreStatement) abstStm);
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

    void visit(CvaWhileForStatement stm);

    void visit(CvaIncreStatement stm);

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
