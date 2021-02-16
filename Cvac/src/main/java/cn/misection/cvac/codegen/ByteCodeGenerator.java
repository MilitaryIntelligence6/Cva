package cn.misection.cvac.codegen;

//import cn.misection.cvac.codegen.bst.CodeGenAst;

import cn.misection.cvac.codegen.bst.CodeGenVisitor;
import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.bentry.GenEntry;
import cn.misection.cvac.codegen.bst.bmethod.GenMethod;
import cn.misection.cvac.codegen.bst.bprogram.GenProgram;
import cn.misection.cvac.codegen.bst.bstatement.*;
import cn.misection.cvac.codegen.bst.btype.GenClassType;
import cn.misection.cvac.codegen.bst.btype.GenInt;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Mengxu on 2017/1/18.
 */
public class ByteCodeGenerator implements CodeGenVisitor
{
    private BufferedWriter writer;

    private void write(String s)
    {
        try
        {
            this.writer.write(s);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void writeln(String s)
    {
        write(String.format("%s\n", s));
    }

    private void writef(String format, Object... args)
    {
        write(String.format(format, args));
    }

    private void iwriteln(String s)
    {
        write(String.format("    %s\n", s));
    }

    @Override
    public void visit(GenClassType t)
    {
        this.write(String.format("L%s;", t.getLiteral()));
    }

    @Override
    public void visit(GenInt t)
    {
        this.write("I");
    }

    @Override
    public void visit(GenDeclaration d)
    {
        this.writeln(";Error: you are accessing the dec single instance.");
    }

    @Override
    public void visit(ALoad s)
    {
        this.iwriteln(String.format("aload %d", s.getIndex()));
    }

    @Override
    public void visit(AReturn s)
    {
        this.iwriteln("areturn");
    }

    @Override
    public void visit(AStore s)
    {
        this.iwriteln(String.format("astore %d", s.getIndex()));
    }

    @Override
    public void visit(Goto s)
    {
        this.iwriteln(String.format("goto %s", s.getLabel().toString()));
    }

    @Override
    public void visit(GetField s)
    {
        this.iwriteln(String.format("getfield %s %s", s.getFieldSpec(), s.getDescriptor()));
    }

    @Override
    public void visit(IAdd s)
    {
        this.iwriteln("iadd");
    }

    @Override
    public void visit(IFicmplt s)
    {
        this.iwriteln(String.format("if_icmplt %s", s.getLabel().toString()));
    }

    @Override
    public void visit(ILoad s)
    {
        this.iwriteln(String.format("iload %d", s.getIndex()));
    }

    @Override
    public void visit(IMul s)
    {
        this.iwriteln("imul");
    }

    @Override
    public void visit(InvokeVirtual s)
    {
        this.write(String.format("    invokevirtual %s/%s(", s.getC(), s.getF()));
        s.getArgTypeList().forEach(this::visit);
        this.write(")");
        this.visit(s.getRetType());
        this.writeln("");
    }

    @Override
    public void visit(IReturn s)
    {
        this.iwriteln("ireturn");
    }

    @Override
    public void visit(IStore s)
    {
        this.iwriteln(String.format("istore %d", s.getIndex()));
    }

    @Override
    public void visit(ISub s)
    {
        this.iwriteln("isub");
    }

    @Override
    public void visit(LabelJ s)
    {
        this.writeln(String.format("%s:", s.getLabel().toString()));
    }

    @Override
    public void visit(Ldc s)
    {
        this.iwriteln(String.format("ldc %d", s.getInteg()));
    }

    @Override
    public void visit(New s)
    {
        this.iwriteln(String.format("new %s", s.getClazz()));
        this.iwriteln("dup");
        this.iwriteln(String.format("invokespecial %s/<init>()V", s.getClazz()));
    }

    @Override
    public void visit(Write s)
    {
        this.iwriteln("getstatic java/lang/System/out Ljava/io/PrintStream;");
        this.iwriteln("swap");
        // TODO 封装常量字段, 同时把println变成print;
        this.iwriteln("invokevirtual java/io/PrintStream/println(I)V");
    }

    @Override
    public void visit(PutField s)
    {
        this.iwriteln(String.format("putfield %s %s", s.getFieldSpec(), s.getDescriptor()));
    }

    @Override
    public void visit(GenMethod m)
    {
        this.write(String.format(".method public %s(", m.getLiteral()));
        m.getFormalList().forEach(f -> this.visit(f.getType()));
        this.write(")");
        this.visit(m.getRetType());
        this.writeln("");
        this.writeln(".limit stack 4096");
        this.writeln(String.format(".limit locals %d", m.getIndex() + 1));

        m.getStatementList().forEach(this::visit);
        this.writeln(".end method");
    }

    @Override
    public void visit(GenClass c)
    {
        try
        {
            // FIXME 路劲;
            this.writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
                    new java.io.FileOutputStream(String.format("%s.il", c.getLiteral()))));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        this.writeln("; This file is automatically generated by the compiler");
        this.writeln("; Do Not Modify!\n");

        this.writeln(String.format(".class public %s", c.getLiteral()));
        if (c.getParent() == null)
        {
            this.writeln(".super java/lang/Object");
        }
        else
        {
            this.writeln(String.format(".super %s", c.getParent()));
        }

        c.getFieldList().forEach(f ->
        {
            this.write(String.format(".field public %s ", f.getLiteral()));
            this.visit(f.getType());
            this.writeln("");
        });

        this.writeln(".method public <init>()V");
        this.iwriteln("aload 0");
        if (c.getParent() == null)
        {
            this.iwriteln("invokespecial java/lang/Object/<init>()V");
        }
        else
        {
            this.iwriteln(String.format("invokespecial %s/<init>()V", c.getParent()));
        }
        this.iwriteln("return");
        this.writeln(".end method");
        c.getMethodList().forEach(this::visit);

        try
        {
            this.writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void visit(GenEntry c)
    {
        try
        {
            this.writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
                    new java.io.FileOutputStream(String.format("%s.il", c.getLiteral()))));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        this.writeln("; This file is automatically generated by the compiler");
        this.writeln("; Do Not Modify!\n");

        this.writeln(String.format(".class public %s", c.getLiteral()));
        this.writeln(".super java/lang/Object");
        this.writeln(".method public static main([Ljava/lang/String;)V");
        this.writeln(".limit stack 4096");
        this.writeln(".limit locals 2");
        c.getStatementList().forEach(this::visit);
        this.iwriteln("return");
        this.writeln(".end method");

        try
        {
            this.writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void visit(GenProgram p)
    {
        this.visit(p.getEntry());
        p.getClassList().forEach(this::visit);
    }
}
