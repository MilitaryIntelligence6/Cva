//package cn.misection.cvac.ast;
//
//
//import cn.misection.cvac.ast.type.CvaBoolean;
//
///**
// * Created by MI6 root 1/12.
// */
//public class AstPrintVisitor implements Visitor
//{
//    private int indentLevel = 4;
//
//    private void indent()
//    {
//        indentLevel += 4;
//    }
//
//    private void unIndent()
//    {
//        indentLevel -= 4;
//    }
//
//    private void printSpaces()
//    {
//        for (int i = indentLevel; i > 0; i--)
//            System.out.print(" ");
//    }
//
//    @Override
//    public void visit(CvaBoolean t)
//    {
//        System.out.print("boolean");
//    }
//
//    @Override
//    public void visit(ClassType t)
//    {
//        System.out.print(t.id);
//    }
//
//    @Override
//    public void visit(Int t)
//    {
//        System.out.print("int");
//    }
//
//    @Override
//    public void visit(Ast.Dec.DecSingle d)
//    {
//        this.visit(d.type);
//        System.out.print(" " + d.id);
//    }
//
//    @Override
//    public void visit(Ast.Exp.Add e)
//    {
//        this.visit(e.left);
//        System.out.print(" + ");
//        this.visit(e.right);
//    }
//
//    @Override
//    public void visit(Ast.Exp.And e)
//    {
//        this.visit(e.left);
//        System.out.print(" && ");
//        this.visit(e.right);
//    }
//
//    @Override
//    public void visit(Ast.Exp.Call e)
//    {
//        this.visit(e.exp);
//        System.out.print("." + e.id + "(");
//        for (int i = 0; i < e.args.size(); i++)
//        {
//            if (i != 0)
//                System.out.print(",");
//            this.visit(e.args.get(i));
//        }
//        System.out.print(")");
//    }
//
//    @Override
//    public void visit(Ast.Exp.False e)
//    {
//        System.out.print("false");
//    }
//
//    @Override
//    public void visit(Ast.Exp.Id e)
//    {
//        System.out.print(e.id);
//    }
//
//    @Override
//    public void visit(Ast.Exp.LT e)
//    {
//        this.visit(e.left);
//        System.out.print(" < ");
//        this.visit(e.right);
//    }
//
//    @Override
//    public void visit(Ast.Exp.NewObject e)
//    {
//        System.out.print("new " + e.id + "()");
//    }
//
//    @Override
//    public void visit(Ast.Exp.Not e)
//    {
//        if (e.exp instanceof Ast.Exp.Id)
//        {
//            System.out.print("!");
//            this.visit(e.exp);
//        } else
//        {
//            System.out.print("!(");
//            this.visit(e.exp);
//            System.out.print(")");
//        }
//    }
//
//    @Override
//    public void visit(Ast.Exp.Num e)
//    {
//        System.out.print(e.num);
//    }
//
//    @Override
//    public void visit(Ast.Exp.Sub e)
//    {
//        this.visit(e.left);
//        System.out.print(" - ");
//        this.visit(e.right);
//    }
//
//    @Override
//    public void visit(Ast.Exp.This e)
//    {
//        System.out.print("this");
//    }
//
//    @Override
//    public void visit(Ast.Exp.Times e)
//    {
//        this.visit(e.left);
//        System.out.print(" * ");
//        if (e.right instanceof Ast.Exp.Add || e.right instanceof Ast.Exp.Sub)
//        {
//            System.out.print("(");
//            this.visit(e.right);
//            System.out.print(")");
//        } else this.visit(e.right);
//
//    }
//
//    @Override
//    public void visit(Ast.Exp.True e)
//    {
//        System.out.print("true");
//    }
//
//    @Override
//    public void visit(Ast.Stm.Assign s)
//    {
//        this.printSpaces();
//        System.out.print(s.id + " = ");
//        this.visit(s.exp);
//        System.out.print(";");
//        System.out.println();
//    }
//
//    @Override
//    public void visit(Ast.Stm.Block s)
//    {
//        this.printSpaces();
//        System.out.println("{");
//        this.indent();
//        for (Ast.Stm.T stm : s.stms)
//        {
//            this.visit(stm);
//        }
//        this.unIndent();
//        this.printSpaces();
//        System.out.println("}");
//    }
//
//    @Override
//    public void visit(Ast.Stm.If s)
//    {
//        this.printSpaces();
//        System.out.print("if (");
//        this.visit(s.condition);
//        System.out.print(")");
//        System.out.println();
//        this.indent();
//        this.visit(s.then_stm);
//        this.unIndent();
//        this.printSpaces();
//        System.out.println("else");
//        this.indent();
//        this.visit(s.else_stm);
//        this.unIndent();
//    }
//
//    @Override
//    public void visit(Ast.Stm.Print s)
//    {
//        this.printSpaces();
//        System.out.print("print(");
//        this.visit(s.exp);
//        System.out.print(");");
//        System.out.println();
//    }
//
//    @Override
//    public void visit(Ast.Stm.While s)
//    {
//        this.printSpaces();
//        System.out.print("while (");
//        this.visit(s.condition);
//        System.out.print(")");
//        System.out.println();
//        this.visit(s.body);
//    }
//
//    @Override
//    public void visit(Ast.Method.MethodSingle m)
//    {
//        this.printSpaces();
//        this.visit(m.retType);
//        System.out.print(" " + m.id + "(");
//        for (int i = 0; i < m.formals.size(); i++)
//        {
//            if (i != 0)
//                System.out.print(", ");
//            this.visit(m.formals.get(i));
//        }
//        System.out.println(")");
//        this.printSpaces();
//        System.out.println("{");
//        this.indent();
//        for (Ast.Dec.T dec : m.locals)
//        {
//            this.printSpaces();
//            this.visit(dec);
//            System.out.print(";");
//            System.out.println();
//        }
//        for (Ast.Stm.T stm : m.stms)
//        {
//            this.visit(stm);
//        }
//        this.printSpaces();
//        System.out.print("return ");
//        this.visit(m.retExp);
//        System.out.print(";");
//        System.out.println();
//        this.unIndent();
//        printSpaces();
//        System.out.println("}");
//    }
//
//    @Override
//    public void visit(Ast.Class.ClassSingle c)
//    {
//        System.out.print("class " + c.id);
//        if (c.base != null)
//            System.out.print(" : " + c.base);
//        System.out.println();
//        System.out.println("{");
//        for (Ast.Dec.T dec : c.fields)
//        {
//            printSpaces();
//            this.visit(dec);
//            System.out.println(";");
//        }
//        for (Ast.Method.T method : c.methods)
//        {
//            this.visit(method);
//        }
//        System.out.println("}");
//    }
//
//    @Override
//    public void visit(Ast.MainClass.MainClassSingle c)
//    {
//        System.out.println("class " + c.id);
//        System.out.println("{");
//        System.out.println("    void main()");
//        System.out.println("    {");
//        this.indent();
//        this.visit(c.stm);
//        this.unIndent();
//        System.out.println("    }");
//        System.out.println("}");
//    }
//
//    @Override
//    public void visit(Ast.Program.ProgramSingle p)
//    {
//        this.visit(p.mainClass);
//        for (Ast.Class.T c : p.classes)
//        {
//            this.visit(c);
//        }
//    }
//}
