package cn.misection.cvac.codegen.bst;

import cn.misection.cvac.codegen.bst.bclas.TargetClass;
import cn.misection.cvac.codegen.bst.bdecl.TargetDeclaration;
import cn.misection.cvac.codegen.bst.bentry.TargetEntryClass;
import cn.misection.cvac.codegen.bst.bmethod.TargetMethod;
import cn.misection.cvac.codegen.bst.bprogram.TargetProgram;
import cn.misection.cvac.codegen.bst.btype.ITargetType;
import cn.misection.cvac.codegen.bst.btype.advance.BaseAdvanceType;
import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;
import cn.misection.cvac.codegen.bst.instruction.*;
import cn.misection.cvac.codegen.bst.btype.reference.BaseReferenceType;

/**
 * Created by MI6 root 1/17.
 */
public interface IBackendVisitor
{
    /**
     * typel
     * @param type t;
     */
    default void visit(ITargetType type)
    {
//        if (type instanceof BaseReferenceType)
//        {
//            this.visit(((BaseReferenceType) type));
//        }
//        else
//        {
//            this.visit(((BaseBasicType) type));
//        }
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
     * @param instruction linear inst;
     */
    default void visit(BaseInstruction instruction)
    {
        switch (instruction.getClass().getSimpleName())
        {
            case Operator.ALOAD:
            {
                visit((ALoad) instruction);
                break;
            }
            case Operator.ARETURN:
            {
                visit((AReturn) instruction);
                break;
            }
            case Operator.ASTORE:
            {
                visit((AStore) instruction);
                break;
            }
            case Operator.GOTO:
            {
                visit((Goto) instruction);
                break;
            }
            case Operator.GET_FIELD:
            {
                visit((GetField) instruction);
                break;
            }
            case Operator.IADD:
            {
                visit((IAdd) instruction);
                break;
            }
            case Operator.IFICMPLT:
            {
                visit((Ificmplt) instruction);
                break;
            }
            case Operator.ILOAD:
            {
                visit((ILoad) instruction);
                break;
            }
            case Operator.IMUL:
            {
                visit((IMul) instruction);
                break;
            }
            case Operator.INVOKE_VIRTUAL:
            {
                visit((InvokeVirtual) instruction);
                break;
            }
            case Operator.IRETURN:
            {
                visit((IReturn) instruction);
                break;
            }
            case Operator.ISTORE:
            {
                visit((IStore) instruction);
                break;
            }
            case Operator.ISUB:
            {
                visit((ISub) instruction);
                break;
            }
            case Operator.LABEL_J:
            {
                visit((LabelJ) instruction);
                break;
            }
            case Operator.LDC:
            {
                visit((Ldc) instruction);
                break;
            }
            case Operator.NEW:
            {
                visit((New) instruction);
                break;
            }
            case Operator.WRITE_INSTRUCTION:
            {
                visit((WriteInstruction) instruction);
                break;
            }
            case Operator.PUT_FIELD:
            {
                visit((PutField) instruction);
                break;
            }
            default:
            {
                System.err.println("unknown operator");
                break;
            }
        }
    }

    void visit(ALoad instruction);

    void visit(AReturn instruction);

    void visit(AStore instruction);

    void visit(Goto instruction);

    void visit(GetField instruction);

    void visit(IAdd instruction);

    void visit(Ificmplt instruction);

    void visit(ILoad instruction);

    void visit(IMul instruction);

    void visit(InvokeVirtual instruction);

    void visit(IReturn instruction);

    void visit(IStore instruction);

    void visit(ISub instruction);

    void visit(LabelJ instruction);

    void visit(Ldc instruction);

    void visit(New instruction);

    void visit(WriteInstruction instruction);

    void visit(PutField instruction);

    void visit(TargetMethod targetMethod);

    void visit(TargetEntryClass entryClass);

    void visit(TargetClass targetClass);

    void visit(TargetProgram targetProgram);
}
