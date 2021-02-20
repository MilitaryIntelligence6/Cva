package cn.misection.cvac.codegen.bst;

import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.bentry.GenEntryClass;
import cn.misection.cvac.codegen.bst.bmethod.GenMethod;
import cn.misection.cvac.codegen.bst.bprogram.GenProgram;
import cn.misection.cvac.codegen.bst.instruction.*;
import cn.misection.cvac.codegen.bst.btype.BaseType;
import cn.misection.cvac.codegen.bst.btype.basic.BaseBasicType;
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
    default void visit(BaseType type)
    {
        if (type instanceof BaseReferenceType)
        {
            this.visit(((BaseReferenceType) type));
        }
        else
        {
            this.visit(((BaseBasicType) type));
        }
    }

    void visit(BaseReferenceType type);

    void visit(BaseBasicType type);

    /**
     * declrartion;
     * @param decl declaration;
     */
    void visit(GenDeclaration decl);

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

    void visit(GenMethod genMethod);

    void visit(GenEntryClass entryClass);

    void visit(GenClass genClass);

    void visit(GenProgram genProgram);
}
