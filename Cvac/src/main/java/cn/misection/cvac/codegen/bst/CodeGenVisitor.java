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
        else this.visit(((CodeGenAst.Type.Int) t));
    }

    void visit(CodeGenAst.Type.ClassType t);

    void visit(CodeGenAst.Type.Int t);

    // Dec
    void visit(CodeGenAst.Dec.DecSingle d);

    // Stm
    default void visit(CodeGenAst.Stm.T s)
    {
        if (s instanceof CodeGenAst.Stm.Aload)
            this.visit(((CodeGenAst.Stm.Aload) s));
        else if (s instanceof CodeGenAst.Stm.Areturn)
            this.visit(((CodeGenAst.Stm.Areturn) s));
        else if (s instanceof CodeGenAst.Stm.Astore)
            this.visit(((CodeGenAst.Stm.Astore) s));
        else if (s instanceof CodeGenAst.Stm.Goto)
            this.visit(((CodeGenAst.Stm.Goto) s));
        else if (s instanceof CodeGenAst.Stm.Getfield)
            this.visit(((CodeGenAst.Stm.Getfield) s));
        else if (s instanceof CodeGenAst.Stm.Iadd)
            this.visit(((CodeGenAst.Stm.Iadd) s));
        else if (s instanceof CodeGenAst.Stm.Ificmplt)
            this.visit(((CodeGenAst.Stm.Ificmplt) s));
        else if (s instanceof CodeGenAst.Stm.Iload)
            this.visit(((CodeGenAst.Stm.Iload) s));
        else if (s instanceof CodeGenAst.Stm.Imul)
            this.visit(((CodeGenAst.Stm.Imul) s));
        else if (s instanceof CodeGenAst.Stm.InvokeVirtual)
            this.visit(((CodeGenAst.Stm.InvokeVirtual) s));
        else if (s instanceof CodeGenAst.Stm.Ireturn)
            this.visit(((CodeGenAst.Stm.Ireturn) s));
        else if (s instanceof CodeGenAst.Stm.Istore)
            this.visit(((CodeGenAst.Stm.Istore) s));
        else if (s instanceof CodeGenAst.Stm.Isub)
            this.visit(((CodeGenAst.Stm.Isub) s));
        else if (s instanceof CodeGenAst.Stm.LabelJ)
            this.visit(((CodeGenAst.Stm.LabelJ) s));
        else if (s instanceof CodeGenAst.Stm.Ldc)
            this.visit(((CodeGenAst.Stm.Ldc) s));
        else if (s instanceof CodeGenAst.Stm.New)
            this.visit(((CodeGenAst.Stm.New) s));
        else if (s instanceof CodeGenAst.Stm.Write)
            this.visit(((CodeGenAst.Stm.Write) s));
        else // if (s instanceof Ast.Stm.Putfield)
            this.visit(((CodeGenAst.Stm.Putfield) s));
    }

    void visit(CodeGenAst.Stm.Aload s);

    void visit(CodeGenAst.Stm.Areturn s);

    void visit(CodeGenAst.Stm.Astore s);

    void visit(CodeGenAst.Stm.Goto s);

    void visit(CodeGenAst.Stm.Getfield s);

    void visit(CodeGenAst.Stm.Iadd s);

    void visit(CodeGenAst.Stm.Ificmplt s);

    void visit(CodeGenAst.Stm.Iload s);

    void visit(CodeGenAst.Stm.Imul s);

    void visit(CodeGenAst.Stm.InvokeVirtual s);

    void visit(CodeGenAst.Stm.Ireturn s);

    void visit(CodeGenAst.Stm.Istore s);

    void visit(CodeGenAst.Stm.Isub s);

    void visit(CodeGenAst.Stm.LabelJ s);

    void visit(CodeGenAst.Stm.Ldc s);

    void visit(CodeGenAst.Stm.New s);

    void visit(CodeGenAst.Stm.Write s);

    void visit(CodeGenAst.Stm.Putfield s);

    void visit(CodeGenAst.Method.MethodSingle m);

    void visit(CodeGenAst.MainClass.GenEntry c);

    void visit(CodeGenAst.Class.GenClass c);

    void visit(CodeGenAst.Program.GenProgram p);
}
