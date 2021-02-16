package cn.misection.cvac.codegen.bst;

import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.bentry.GenEntry;
import cn.misection.cvac.codegen.bst.bmethod.GenMethod;
import cn.misection.cvac.codegen.bst.bprogram.GenProgram;
import cn.misection.cvac.codegen.bst.bstatement.*;
import cn.misection.cvac.codegen.bst.btype.BaseType;
import cn.misection.cvac.codegen.bst.btype.GenClassType;
import cn.misection.cvac.codegen.bst.btype.GenInt;

/**
 * Created by Mengxu on 2017/1/17.
 */
public interface CodeGenVisitor
{
    // Type
    default void visit(BaseType t)
    {
        if (t instanceof GenClassType)
        {
            this.visit(((GenClassType) t));
        }
        else
        {
            this.visit(((GenInt) t));
        }
    }

    void visit(GenClassType t);

    void visit(GenInt t);

    // Dec
    void visit(GenDeclaration d);

    // Stm
    default void visit(BaseStatement s)
    {
        if (s instanceof ALoad)
        {
            this.visit(((ALoad) s));
        }
        else if (s instanceof AReturn)
        {
            this.visit(((AReturn) s));
        }
        else if (s instanceof AStore)
        {
            this.visit(((AStore) s));
        }
        else if (s instanceof Goto)
        {
            this.visit(((Goto) s));
        }
        else if (s instanceof GetField)
        {
            this.visit(((GetField) s));
        }
        else if (s instanceof IAdd)
        {
            this.visit(((IAdd) s));
        }
        else if (s instanceof IFicmplt)
        {
            this.visit(((IFicmplt) s));
        }
        else if (s instanceof ILoad)
        {
            this.visit(((ILoad) s));
        }
        else if (s instanceof IMul)
        {
            this.visit(((IMul) s));
        }
        else if (s instanceof InvokeVirtual)
        {
            this.visit(((InvokeVirtual) s));
        }
        else if (s instanceof IReturn)
        {
            this.visit(((IReturn) s));
        }
        else if (s instanceof IStore)
        {
            this.visit(((IStore) s));
        }
        else if (s instanceof ISub)
        {
            this.visit(((ISub) s));
        }
        else if (s instanceof LabelJ)
        {
            this.visit(((LabelJ) s));
        }
        else if (s instanceof Ldc)
        {
            this.visit(((Ldc) s));
        }
        else if (s instanceof New)
        {
            this.visit(((New) s));
        }
        else if (s instanceof Write)
        {
            this.visit(((Write) s));
        }
        else // if (s instanceof Ast.Stm.Putfield)
        {
            this.visit(((PutField) s));
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

    void visit(Write s);

    void visit(PutField s);

    void visit(GenMethod m);

    void visit(GenEntry c);

    void visit(GenClass c);

    void visit(GenProgram p);
}
