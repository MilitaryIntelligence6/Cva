package cn.misection.cvac.codegen;

import cn.misection.cvac.codegen.bst.CodeGenVisitor;
import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.bentry.GenEntry;
import cn.misection.cvac.codegen.bst.bmethod.GenMethod;
import cn.misection.cvac.codegen.bst.bprogram.GenProgram;
import cn.misection.cvac.codegen.bst.instruction.*;
import cn.misection.cvac.codegen.bst.btype.basic.BaseBasicType;
import cn.misection.cvac.codegen.bst.btype.refer.BaseReferenceType;
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

    private WriteTypeMap writeTypeMap = WriteTypeMap.getInstance();

    private WriteModeMap writeModeMap = WriteModeMap.getInstance();


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
    public void visit(BaseReferenceType t)
    {
        writef("L%s;", t.literal());
    }

    @Override
    public void visit(BaseBasicType t)
    {
        write(t.instruction());
    }

    @Override
    public void visit(GenDeclaration d)
    {
        writeln(";Error: you are accessing the dec single instance.");
    }

    @Override
    public void visit(ALoad s)
    {
        iwritefln("aload %d", s.getIndex());
    }

    @Override
    public void visit(AReturn s)
    {
        iwriteLine("areturn");
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
        iwriteLine("iadd");
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
        iwriteLine("imul");
    }

    @Override
    public void visit(InvokeVirtual s)
    {
        iwritef("invokevirtual %s/%s(", s.getC(), s.getF());
        s.getArgTypeList().forEach(this::visit);
        write(")");
        visit(s.getRetType());
        writeln();
    }

    @Override
    public void visit(IReturn s)
    {
        iwriteLine("ireturn");
    }

    @Override
    public void visit(IStore s)
    {
        iwritefln("istore %d", s.getIndex());
    }

    @Override
    public void visit(ISub s)
    {
        iwriteLine("isub");
    }

    @Override
    public void visit(LabelJ s)
    {
        writef("%s:\n", s.getLabel().toString());
    }

    @Override
    public void visit(ILdc s)
    {
        iwritefln("ldc %d", s.value());
    }

    @Override
    public void visit(New s)
    {
        iwritefln("new %s", s.getClazz());
        iwriteLine("dup");
        iwritefln("invokespecial %s/<init>()V", s.getClazz());
    }

    @Override
    public void visit(WriteInstruction inst)
    {
        String mode = writeModeMap.get(inst.getWriteMode());
        String type = writeTypeMap.get(inst.getWriteType());
        iwriteLine("getstatic java/lang/System/out Ljava/io/PrintStream;");
        iwriteLine("swap");
        // TODO 封装常量字段, 同时把println变成print;
        iwritefln("invokevirtual java/io/PrintStream/%s(%s)V", mode, type);
    }

    /**
     * string 打印指令;
     * @param s
     */
//    private void printString(String s) {
//        if (s.length() == 0) {
//            return;
//        }
//
//        ProgramGenerator generator = ProgramGenerator.getInstance();
//        generator.emit(Instruction.GETSTATIC, "java/lang/System/out Ljava/io/PrintStream;");
//        generator.emit(Instruction.LDC, "\"" + s + "\"");
//        String printMethod = "java/io/PrintStream/print(Ljava/lang/String;)V";
//        generator.emit(Instruction.INVOKEVIRTUAL, printMethod);
//    }
//
//    private void printInteger(int posInList) {
//        ProgramGenerator generator = ProgramGenerator.getInstance();
//        generator.emit(Instruction.GETSTATIC, "java/lang/System/out Ljava/io/PrintStream;");
//        generator.emit(Instruction.ILOAD, "" + posInList);
//        String printMethod = "java/io/PrintStream/print(I)V";
//        generator.emit(Instruction.INVOKEVIRTUAL, printMethod);
//    }

    @Override
    public void visit(PutField s)
    {
        iwritefln("putfield %s %s", s.getFieldSpec(), s.getDescriptor());
    }

    @Override
    public void visit(GenMethod method)
    {
        writef(".method public %s(", method.getLiteral());
        method.getFormalList().forEach(f -> visit(f.getType()));
        write(")");
        visit(method.getRetType());
        writeln();
        writef(".limit stack %d\n", StackConst.MAX_STACK_DEPTH);
        writef(".limit locals %d\n", method.getIndex() + 1);

        method.getStatementList().forEach(this::visit);
        writeln(".end method");
    }

    @Override
    public void visit(GenClass genClass)
    {
        initWriter(String.format("%s.il", genClass.getLiteral()));
        writef(".class public %s\n", genClass.getLiteral());
        if (genClass.getParent() == null)
        {
            writeln(".super java/lang/Object");
        }
        else
        {
            writef(".super %s\n", genClass.getParent());
        }

        genClass.getFieldList().forEach(f ->
        {
            writef(".field public %s ", f.getLiteral());
            visit(f.getType());
            writeln();
        });

        writeln(".method public <init>()V");
        iwriteLine("aload 0");
        if (genClass.getParent() == null)
        {
            iwriteLine("invokespecial java/lang/Object/<init>()V");
        }
        else
        {
            iwritefln("invokespecial %s/<init>()V", genClass.getParent());
        }
        iwriteLine("return");
        writeln(".end method");
        genClass.getMethodList().forEach(this::visit);

        saveAndReinit();
    }

    @Override
    public void visit(GenEntry entry)
    {
        initWriter(String.format("%s.il", entry.getLiteral()));

        writef(".class public %s\n", entry.getLiteral());
        writeln(".super java/lang/Object");
        writeln(".method public static main([Ljava/lang/String;)V");
        writef(".limit stack %d\n", StackConst.MAX_STACK_DEPTH);
        writeln(".limit locals 2");
        entry.getStatementList().forEach(this::visit);
        iwriteLine("return");
        writeln(".end method");

        saveAndReinit();
    }

    @Override
    public void visit(GenProgram program)
    {
        visit(program.getEntry());
        program.getClassList().forEach(this::visit);
    }


    private void initWriter(String path)
    {
        try
        {
            // FIXME 路劲;
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path)));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        writeln("; This file is automatically generated by the compiler");
        writeln("; Do Not Modify!\n");
    }

    private void saveAndReinit()
    {
        try
        {
            writer.write(String.valueOf(buffer));
            clearBuffer();
            writer.close();
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
