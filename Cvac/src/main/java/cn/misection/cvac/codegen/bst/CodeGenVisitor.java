package cn.misection.cvac.codegen.bst;

/**
 * Created by Mengxu on 2017/1/17.
 */
public interface CodeGenVisitor
{
    // Type
    default void visit(CodeGenAst.Type.T t)
    {
        if (t instanceof CodeGenAst.Type.ClassType)
            this.visit(((CodeGenAst.Type.ClassType) t));
        else this.visit(((CodeGenAst.Type.GenInt) t));
    }

    void visit(CodeGenAst.Type.ClassType t);

    void visit(CodeGenAst.Type.GenInt t);

    // Dec
    void visit(CodeGenAst.Dec.GenDeclaration d);

    // Stm
    default void visit(CodeGenAst.Stm.T s)
    {
        if (s instanceof CodeGenAst.Stm.ALoad)
            this.visit(((CodeGenAst.Stm.ALoad) s));
        else if (s instanceof CodeGenAst.Stm.AReturn)
            this.visit(((CodeGenAst.Stm.AReturn) s));
        else if (s instanceof CodeGenAst.Stm.AStore)
            this.visit(((CodeGenAst.Stm.AStore) s));
        else if (s instanceof CodeGenAst.Stm.Goto)
            this.visit(((CodeGenAst.Stm.Goto) s));
        else if (s instanceof CodeGenAst.Stm.GetField)
            this.visit(((CodeGenAst.Stm.GetField) s));
        else if (s instanceof CodeGenAst.Stm.IAdd)
            this.visit(((CodeGenAst.Stm.IAdd) s));
        else if (s instanceof CodeGenAst.Stm.IFicmplt)
            this.visit(((CodeGenAst.Stm.IFicmplt) s));
        else if (s instanceof CodeGenAst.Stm.ILoad)
            this.visit(((CodeGenAst.Stm.ILoad) s));
        else if (s instanceof CodeGenAst.Stm.IMul)
            this.visit(((CodeGenAst.Stm.IMul) s));
        else if (s instanceof CodeGenAst.Stm.InvokeVirtual)
            this.visit(((CodeGenAst.Stm.InvokeVirtual) s));
        else if (s instanceof CodeGenAst.Stm.IReturn)
            this.visit(((CodeGenAst.Stm.IReturn) s));
        else if (s instanceof CodeGenAst.Stm.IStore)
            this.visit(((CodeGenAst.Stm.IStore) s));
        else if (s instanceof CodeGenAst.Stm.ISub)
            this.visit(((CodeGenAst.Stm.ISub) s));
        else if (s instanceof CodeGenAst.Stm.LabelJ)
            this.visit(((CodeGenAst.Stm.LabelJ) s));
        else if (s instanceof CodeGenAst.Stm.Ldc)
            this.visit(((CodeGenAst.Stm.Ldc) s));
        else if (s instanceof CodeGenAst.Stm.New)
            this.visit(((CodeGenAst.Stm.New) s));
        else if (s instanceof CodeGenAst.Stm.Write)
            this.visit(((CodeGenAst.Stm.Write) s));
        else // if (s instanceof Ast.Stm.Putfield)
            this.visit(((CodeGenAst.Stm.PutField) s));
    }

    void visit(CodeGenAst.Stm.ALoad s);

    void visit(CodeGenAst.Stm.AReturn s);

    void visit(CodeGenAst.Stm.AStore s);

    void visit(CodeGenAst.Stm.Goto s);

    void visit(CodeGenAst.Stm.GetField s);

    void visit(CodeGenAst.Stm.IAdd s);

    void visit(CodeGenAst.Stm.IFicmplt s);

    void visit(CodeGenAst.Stm.ILoad s);

    void visit(CodeGenAst.Stm.IMul s);

    void visit(CodeGenAst.Stm.InvokeVirtual s);

    void visit(CodeGenAst.Stm.IReturn s);

    void visit(CodeGenAst.Stm.IStore s);

    void visit(CodeGenAst.Stm.ISub s);

    void visit(CodeGenAst.Stm.LabelJ s);

    void visit(CodeGenAst.Stm.Ldc s);

    void visit(CodeGenAst.Stm.New s);

    void visit(CodeGenAst.Stm.Write s);

    void visit(CodeGenAst.Stm.PutField s);

    void visit(CodeGenAst.Method.GenMethod m);

    void visit(CodeGenAst.MainClass.GenEntry c);

    void visit(CodeGenAst.Class.GenClass c);

    void visit(CodeGenAst.Program.GenProgram p);
}
