package cn.misection.cvac.ast;

import cn.misection.cvac.ast.clas.AbstractCvaClass;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.decl.nullptr.CvaNullDecl;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.expr.nonterminal.binary.*;
import cn.misection.cvac.ast.expr.terminator.*;
import cn.misection.cvac.ast.expr.nonterminal.unary.*;
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
                case STRING:
                {
                    visit((CvaStringType) type);
                    break;
                }
                case POINTER:
                {
                    // TODO;
                    break;
                }
                case ARRAY:
                {
                    // TODO;
                    break;
                }
                case CLASS:
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
        if (!(abstDecl instanceof CvaNullDecl))
        {
            visit(((CvaDeclaration) abstDecl));
        }
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
            case CvaExprClassName.CVA_THIS_EXPR:
            {
                visit((CvaThisExpr) expr);
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
                    case BINARY_OPERAND_OP:
                    {
                        visit((CvaOperandOperatorExpr) expr);
                        break;
                    }
                    case INCREMENT:
                    case DECREMENT:
                    {
                        visit((CvaIncDecExpr) expr);
                        break;
                    }
                    case NULL_POINTER:
                    {
                        // 直接忽略, 转都不转;
                        break;
                    }
                    case CONST_NULL:
                    {
                        // TODO;
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

    void visit(CvaAndAndExpr expr);

    void visit(CvaCallExpr expr);

    void visit(CvaConstFalseExpr expr);

    void visit(CvaIdentifierExpr expr);

    void visit(CvaLessOrMoreThanExpr expr);

    void visit(CvaNewExpr expr);

    void visit(CvaNegateExpr expr);

    void visit(CvaConstIntExpr expr);

    void visit(CvaConstStringExpr expr);

    void visit(CvaThisExpr expr);

    void visit(CvaConstTrueExpr expr);

    void visit(CvaOperandOperatorExpr expr);

    void visit(CvaIncDecExpr expr);

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
                switch (abstStm.toEnum())
                {
                    case NULL_POINTER:
                    {
                        // 直接忽略, 转都不用转;
                        break;
                    }
                    case EXPR_STATEMENT:
                    {
                        // decl statement 不需要用, 其只是辅助数据结构, 用完就扔;
                        visit((CvaExprStatement) abstStm);
                        break;
                    }
                    default:
                    {
                        System.err.println("unknown statement");
                        break;
                    }
                }
            }
        }
    }

    void visit(CvaAssignStatement stm);

    void visit(CvaBlockStatement stm);

    void visit(CvaIfStatement stm);

    void visit(CvaWriteStatement stm);

    void visit(CvaWhileForStatement stm);

    void visit(CvaIncreStatement stm);

    void visit(CvaExprStatement stm);

    /**
     * Method
     */
    default void visit(AbstractMethod abstMethod)
    {
        visit(((CvaMethod) abstMethod));
    }

    void visit(CvaMethod cvaMethod);

    /**
     * class;
     * @param abstClass class;
     */
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
