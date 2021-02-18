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
import cn.misection.cvac.constant.WriteILPool;

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

    /**
     * write tab space;
     */
    private void writeTabSpace()
    {
        buffer.append(WriteILPool.TAB_SPACE);
    }

    private void writeln(String s)
    {
        buffer.append(s).append(WriteILPool.NEW_LINE_CH);
    }

    private void writeln()
    {
        buffer.append(WriteILPool.NEW_LINE_CH);
    }

    private void writef(String format, Object... args)
    {
        write(String.format(format, args));
    }

    /**
     * iwrite 是write instruction 的意思;
     * 避免和writefln混淆;
     * @param s
     */
    private void iwriteLine(String s)
    {
        writeTabSpace();
        write(s);
        writeln();
    }
    
    private void iwritef(String format, Object... args)
    {
        writeTabSpace();
        write(String.format(format, args));
    }

    private void iwritefln(String format, Object... args)
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
        iwritefln("aload %d", s.getIndex());
    }

    @Override
    public void visit(AReturn s)
    {
        this.iwriteLine("areturn");
    }

    @Override
    public void visit(AStore s)
    {
        iwritefln("astore %d", s.getIndex());
    }

    @Override
    public void visit(Goto s)
    {
        iwritefln("goto %s", s.getLabel().toString());
    }

    @Override
    public void visit(GetField s)
    {
        iwritefln("getfield %s %s", s.getFieldSpec(), s.getDescriptor());
    }

    @Override
    public void visit(IAdd s)
    {
        this.iwriteLine("iadd");
    }

    @Override
    public void visit(IFicmplt s)
    {
        iwritefln("if_icmplt %s", s.getLabel().toString());
    }

    @Override
    public void visit(ILoad s)
    {
        iwritefln("iload %d", s.getIndex());
    }

    @Override
    public void visit(IMul s)
    {
        this.iwriteLine("imul");
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
        this.iwriteLine("ireturn");
    }

    @Override
    public void visit(IStore s)
    {
        iwritefln("istore %d", s.getIndex());
    }

    @Override
    public void visit(ISub s)
    {
        this.iwriteLine("isub");
    }

    @Override
    public void visit(LabelJ s)
    {
        writef("%s:\n", s.getLabel().toString());
    }

    @Override
    public void visit(Ldc s)
    {
        iwritefln("ldc %d", s.getInteg());
    }

    @Override
    public void visit(New s)
    {
        iwritefln("new %s", s.getClazz());
        this.iwriteLine("dup");
        iwritefln("invokespecial %s/<init>()V", s.getClazz());
    }

    @Override
    public void visit(WriteInstr s)
    {
        this.iwriteLine("getstatic java/lang/System/out Ljava/io/PrintStream;");
        this.iwriteLine("swap");
        // TODO 封装常量字段, 同时把println变成print;
        this.iwriteLine("invokevirtual java/io/PrintStream/println(I)V");
    }

    @Override
    public void visit(PutField s)
    {
        iwritefln("putfield %s %s", s.getFieldSpec(), s.getDescriptor());
    }

    @Override
    public void visit(GenMethod method)
    {
        writef(".method public %s(", method.getLiteral());
        method.getFormalList().forEach(f -> this.visit(f.getType()));
        write(")");
        visit(method.getRetType());
        writeln();
        writef(".limit stack %d\n", StackConst.MAX_STACK_DEPTH);
        writef(".limit locals %d\n", method.getIndex() + 1);

        method.getStatementList().forEach(this::visit);
        writeln(".end method");
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
        this.iwriteLine("aload 0");
        if (c.getParent() == null)
        {
            this.iwriteLine("invokespecial java/lang/Object/<init>()V");
        }
        else
        {
            iwritefln("invokespecial %s/<init>()V", c.getParent());
        }
        this.iwriteLine("return");
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
        this.iwriteLine("return");
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
