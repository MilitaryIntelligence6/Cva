package cn.misection.cvac.codegen;

import cn.misection.cvac.codegen.bst.CodeGenVisitor;
import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.bentry.GenEntry;
import cn.misection.cvac.codegen.bst.bmethod.GenMethod;
import cn.misection.cvac.codegen.bst.bprogram.GenProgram;
import cn.misection.cvac.codegen.bst.binstruct.*;
import cn.misection.cvac.codegen.bst.btype.GenClassType;
import cn.misection.cvac.codegen.bst.btype.GenInt;
import cn.misection.cvac.constant.CharPool;

import java.io.*;

/**
 *
 * @author MI6 root;
 * @date 
 */
public final class IntermLangGenerator implements CodeGenVisitor
{
    private BufferedWriter writer;

    private final StringBuffer buffer = new StringBuffer();


    private void write(String s)
    {
        buffer.append(s);
    }

    private void writeln(String s)
    {
        buffer.append(s).append(CharPool.NEW_LINE_CH);
    }

    private void writeln()
    {
        buffer.append(CharPool.NEW_LINE_CH);
    }

    private void writef(String format, Object... args)
    {
        write(String.format(format, args));
    }

    /**
     * iwrite 是write instruction 的意思;
     * @param s
     */
    private void iwriteln(String s)
    {
        write(String.format("    %s\n", s));
    }
    
    private void iwritef(String format, Object... args)
    {
        write("    ");
        write(String.format(format, args));
    }

    /**
     * 为了避免和writln混淆;
     * @param format
     * @param args
     */
    private void iwritefline(String format, Object... args)
    {
        iwritef(format, args);
        writeln();
    }

    @Override
    public void visit(GenClassType t)
    {
        writef("L%s;", t.getLiteral());
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
        iwritefline("aload %d", s.getIndex());
    }

    @Override
    public void visit(AReturn s)
    {
        this.iwriteln("areturn");
    }

    @Override
    public void visit(AStore s)
    {
        iwritefline("astore %d", s.getIndex());
    }

    @Override
    public void visit(Goto s)
    {
        iwritefline("goto %s", s.getLabel().toString());
    }

    @Override
    public void visit(GetField s)
    {
        iwritefline("getfield %s %s", s.getFieldSpec(), s.getDescriptor());
    }

    @Override
    public void visit(IAdd s)
    {
        this.iwriteln("iadd");
    }

    @Override
    public void visit(IFicmplt s)
    {
        iwritefline("if_icmplt %s", s.getLabel().toString());
    }

    @Override
    public void visit(ILoad s)
    {
        iwritefline("iload %d", s.getIndex());
    }

    @Override
    public void visit(IMul s)
    {
        this.iwriteln("imul");
    }

    @Override
    public void visit(InvokeVirtual s)
    {
        writef("    invokevirtual %s/%s(", s.getC(), s.getF());
        s.getArgTypeList().forEach(this::visit);
        this.write(")");
        this.visit(s.getRetType());
        writeln();
    }

    @Override
    public void visit(IReturn s)
    {
        this.iwriteln("ireturn");
    }

    @Override
    public void visit(IStore s)
    {
        iwritefline("istore %d", s.getIndex());
    }

    @Override
    public void visit(ISub s)
    {
        this.iwriteln("isub");
    }

    @Override
    public void visit(LabelJ s)
    {
        writef("%s:\n", s.getLabel().toString());
    }

    @Override
    public void visit(Ldc s)
    {
        iwritefline("ldc %d", s.getInteg());
    }

    @Override
    public void visit(New s)
    {
        iwritefline("new %s", s.getClazz());
        this.iwriteln("dup");
        iwritefline("invokespecial %s/<init>()V", s.getClazz());
    }

    @Override
    public void visit(WriteInstr s)
    {
        this.iwriteln("getstatic java/lang/System/out Ljava/io/PrintStream;");
        this.iwriteln("swap");
        // TODO 封装常量字段, 同时把println变成print;
        this.iwriteln("invokevirtual java/io/PrintStream/println(I)V");
    }

    @Override
    public void visit(PutField s)
    {
        iwritefline("putfield %s %s", s.getFieldSpec(), s.getDescriptor());
    }

    @Override
    public void visit(GenMethod m)
    {
        writef(".method public %s(", m.getLiteral());
        m.getFormalList().forEach(f -> this.visit(f.getType()));
        this.write(")");
        this.visit(m.getRetType());
        writeln();
        this.writeln(".limit stack 4096");
        writef(".limit locals %d\n", m.getIndex() + 1);

        m.getStatementList().forEach(this::visit);
        this.writeln(".end method");
    }

    @Override
    public void visit(GenClass c)
    {
        initWriter(String.format("%s.il", c.getLiteral()));
        writef(".class public %s\n", c.getLiteral());
        if (c.getParent() == null)
        {
            this.writeln(".super java/lang/Object");
        }
        else
        {
            writef(".super %s\n", c.getParent());
        }

        c.getFieldList().forEach(f ->
        {
            writef(".field public %s ", f.getLiteral());
            this.visit(f.getType());
            writeln();
        });

        this.writeln(".method public <init>()V");
        this.iwriteln("aload 0");
        if (c.getParent() == null)
        {
            this.iwriteln("invokespecial java/lang/Object/<init>()V");
        }
        else
        {
            iwritefline("invokespecial %s/<init>()V", c.getParent());
        }
        this.iwriteln("return");
        this.writeln(".end method");
        c.getMethodList().forEach(this::visit);

        writeAndDestroy();
    }

    @Override
    public void visit(GenEntry entry)
    {
        initWriter(String.format("%s.il", entry.getLiteral()));

        writef(".class public %s\n", entry.getLiteral());
        this.writeln(".super java/lang/Object");
        this.writeln(".method public static main([Ljava/lang/String;)V");
        this.writef(".limit stack %d\n", StackConst.MAX_STACK_DEPTH);
        this.writeln(".limit locals 2");
        entry.getStatementList().forEach(this::visit);
        this.iwriteln("return");
        this.writeln(".end method");

        writeAndDestroy();
    }

    @Override
    public void visit(GenProgram program)
    {
        this.visit(program.getEntry());
        program.getClassList().forEach(this::visit);
    }


    private void initWriter(String path)
    {
        try
        {
            // FIXME 路劲;
            this.writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path)));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        this.writeln("; This file is automatically generated by the compiler");
        this.writeln("; Do Not Modify!\n");
    }

    private void writeAndDestroy()
    {
        try
        {
            this.writer.write(String.valueOf(buffer));
            clearBuffer();
            this.writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void clearBuffer()
    {
        buffer.delete(0, buffer.length());
    }
}
