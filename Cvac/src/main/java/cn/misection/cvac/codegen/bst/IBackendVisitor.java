package cn.misection.cvac.codegen.bst;

import cn.misection.cvac.codegen.bst.bclas.TargetClass;
import cn.misection.cvac.codegen.bst.bdecl.TargetDeclaration;
import cn.misection.cvac.codegen.bst.bentry.TargetEntryClass;
import cn.misection.cvac.codegen.bst.bmethod.TargetMethod;
import cn.misection.cvac.codegen.bst.bprogram.TargetProgram;
import cn.misection.cvac.codegen.bst.btype.ITargetType;
import cn.misection.cvac.codegen.bst.btype.advance.BaseAdvanceType;
import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;
import cn.misection.cvac.codegen.bst.instructor.*;
import cn.misection.cvac.codegen.bst.btype.reference.BaseReferenceType;

/**
 * Created by MI6 root 1/17.
 */
@SuppressWarnings("rawtypes")
public interface IBackendVisitor
{
    /**
     * typel
     * @param type t;
     */
    default void visit(ITargetType type)
    {
        EnumTargetType typeEnum = type.toEnum();
        if (EnumTargetType.isBasicType(typeEnum))
        {
            visit((EnumTargetType) type);
        }
        else if (EnumTargetType.isAdvanceType(typeEnum))
        {
            visit((BaseAdvanceType) type);
        }
        else
        {
            visit((BaseReferenceType) type);
        }
    }

    void visit(EnumTargetType type);

    void visit(BaseAdvanceType type);

    void visit(BaseReferenceType type);

    /**
     * declrartion;
     * @param decl declaration;
     */
    void visit(TargetDeclaration decl);

    /**
     * statements
     * @param instructor linear inst;
     */
    default void visit(IInstructor instructor)
    {
        switch (instructor.getClass().getSimpleName())
        {
            case Operator.ARETURN:
            case Operator.IADD:
            case Operator.IMUL:
            case Operator.IRETURN:
            case Operator.ISUB:
            case "EnumInstructor":
            {
                visit((EnumInstructor) instructor);
                break;
            }
            case Operator.ALOAD:
            {
                visit((ALoad) instructor);
                break;
            }
            case Operator.ASTORE:
            {
                visit((AStore) instructor);
                break;
            }
            case Operator.GOTO:
            {
                visit((Goto) instructor);
                break;
            }
            case Operator.GET_FIELD:
            {
                visit((GetField) instructor);
                break;
            }
            case Operator.IF_ICMP_LT:
            {
                visit((IfICmpLt) instructor);
                break;
            }
            case Operator.ILOAD:
            {
                visit((ILoad) instructor);
                break;
            }
            case Operator.INVOKE_VIRTUAL:
            {
                visit((InvokeVirtual) instructor);
                break;
            }
            case Operator.ISTORE:
            {
                visit((IStore) instructor);
                break;
            }
            case Operator.LABEL_J:
            {
                visit((LabelJ) instructor);
                break;
            }
            case Operator.LDC:
            {
                visit((Ldc) instructor);
                break;
            }
            case Operator.NEW:
            {
                visit((New) instructor);
                break;
            }
            case Operator.WRITE_INSTRUCTOR:
            {
                visit((WriteInstructor) instructor);
                break;
            }
            case Operator.PUT_FIELD:
            {
                visit((PutField) instructor);
                break;
            }
            case "IInc":
            {
                visit((IInc) instructor);
                break;
            }
            default:
            {
                System.err.println("unknown operator");
                break;
            }
        }
    }

    void visit(EnumInstructor instructor);

    void visit(ALoad instructor);

    void visit(AStore instructor);

    void visit(Goto instructor);

    void visit(GetField instructor);

    void visit(IfICmpLt instructor);

    void visit(ILoad instructor);

    void visit(InvokeVirtual instructor);

    void visit(IStore instructor);

    void visit(LabelJ instructor);

    void visit(Ldc instructor);

    void visit(New instructor);

    void visit(WriteInstructor instructor);

    void visit(PutField instructor);

    void visit(IInc instructor);

    void visit(TargetMethod targetMethod);

    void visit(TargetEntryClass entryClass);

    void visit(TargetClass targetClass);

    void visit(TargetProgram targetProgram);
}
