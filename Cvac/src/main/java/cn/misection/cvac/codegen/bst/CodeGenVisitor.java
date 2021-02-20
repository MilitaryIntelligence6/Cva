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
public interface CodeGenVisitor
{
    // Type
    default void visit(BaseType t)
    {
        if (t instanceof BaseReferenceType)
        {
            this.visit(((BaseReferenceType) t));
        }
        else
        {
            this.visit(((BaseBasicType) t));
        }
    }

    void visit(BaseReferenceType t);

    void visit(BaseBasicType t);

    // Dec
    void visit(GenDeclaration d);

    // Stm
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

    void visit(ALoad s);

    void visit(AReturn s);

    void visit(AStore s);

    void visit(Goto s);

    void visit(GetField s);

    void visit(IAdd s);

    void visit(Ificmplt s);

    void visit(ILoad s);

    void visit(IMul s);

    void visit(InvokeVirtual s);

    void visit(IReturn s);

    void visit(IStore s);

    void visit(ISub s);

    void visit(LabelJ s);

    void visit(Ldc s);

    void visit(New s);

    void visit(WriteInstruction s);

    void visit(PutField s);

    void visit(GenMethod m);

    void visit(GenEntryClass c);

    void visit(GenClass c);

    void visit(GenProgram p);
}
