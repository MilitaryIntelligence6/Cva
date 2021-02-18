package cn.misection.cvac.codegen.bst;

import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.bentry.GenEntry;
import cn.misection.cvac.codegen.bst.bmethod.GenMethod;
import cn.misection.cvac.codegen.bst.bprogram.GenProgram;
import cn.misection.cvac.codegen.bst.instruction.*;
import cn.misection.cvac.codegen.bst.btype.BaseType;
import cn.misection.cvac.codegen.bst.btype.basic.BaseBasicType;
import cn.misection.cvac.codegen.bst.btype.refer.BaseReferenceType;

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
    default void visit(BaseInstruction s)
    {
        switch (s.getClass().getSimpleName())
        {
            case Operator.ALOAD:
            {
                visit((ALoad) s);
                break;
            }
            case Operator.ARETURN:
            {
                visit((AReturn) s);
                break;
            }
            case Operator.ASTORE:
            {
                visit((AStore) s);
                break;
            }
            case Operator.GOTO:
            {
                visit((Goto) s);
                break;
            }
            case Operator.GETFIELD:
            {
                visit((GetField) s);
                break;
            }
            case Operator.IADD:
            {
                visit((IAdd) s);
                break;
            }
            case Operator.IFICMPLT:
            {
                visit((IFicmplt) s);
                break;
            }
            case Operator.ILOAD:
            {
                visit((ILoad) s);
                break;
            }
            case Operator.IMUL:
            {
                visit((IMul) s);
                break;
            }
            case Operator.INVOKEVIRTUAL:
            {
                visit((InvokeVirtual) s);
                break;
            }
            case Operator.IRETURN:
            {
                visit((IReturn) s);
                break;
            }
            case Operator.ISTORE:
            {
                visit((IStore) s);
                break;
            }
            case Operator.ISUB:
            {
                visit((ISub) s);
                break;
            }
            case Operator.LABEL_J:
            {
                visit((LabelJ) s);
                break;
            }
            case Operator.LDC:
            {
                visit((Ldc) s);
                break;
            }
            case Operator.NEW:
            {
                visit((New) s);
                break;
            }
            case Operator.WRITE_INT:
            {
                visit((WriteInt) s);
                break;
            }
            case Operator.PUT_FIELD:
            {
                visit((PutField) s);
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

    void visit(IFicmplt s);

    void visit(ILoad s);

    void visit(IMul s);

    void visit(InvokeVirtual s);

    void visit(IReturn s);

    void visit(IStore s);

    void visit(ISub s);

    void visit(LabelJ s);

    void visit(Ldc s);

    void visit(New s);

    void visit(WriteInt s);

    void visit(PutField s);

    void visit(GenMethod m);

    void visit(GenEntry c);

    void visit(GenClass c);

    void visit(GenProgram p);
}
