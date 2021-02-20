package cn.misection.cvac.codegen;

import cn.misection.cvac.codegen.bst.IBackendVisitor;
import cn.misection.cvac.codegen.bst.bclas.TargetClass;
import cn.misection.cvac.codegen.bst.bdecl.TargetDeclaration;
import cn.misection.cvac.codegen.bst.bentry.TargetEntryClass;
import cn.misection.cvac.codegen.bst.bmethod.TargetMethod;
import cn.misection.cvac.codegen.bst.bprogram.TargetProgram;
import cn.misection.cvac.codegen.bst.btype.advance.BaseAdvanceType;
import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;
import cn.misection.cvac.codegen.bst.instruction.*;
import cn.misection.cvac.codegen.bst.btype.reference.BaseReferenceType;
import cn.misection.cvac.constant.IntermLangCommon;

import java.io.*;

/**
 *
 * @author MI6 root;
 * @date 
 */
public final class IntermLangGenerator implements IBackendVisitor
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
        buffer.append(IntermLangCommon.TAB_SPACE);
    }

    private void writeln(String s)
    {
        buffer.append(s).append(IntermLangCommon.NEW_LINE_CH);
    }

    private void writeln()
    {
        buffer.append(IntermLangCommon.NEW_LINE_CH);
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
    public void visit(EnumTargetType type)
    {
        write(type.instruct());
    }

    @Override
    public void visit(BaseAdvanceType type)
    {
        write(type.instruct());
    }

    @Override
    public void visit(BaseReferenceType type)
    {
        writef("L%s;", type.literal());
    }

    @Override
    public void visit(TargetDeclaration decl)
    {
        writeln(";Error: you are accessing the dec single instance.");
    }

    @Override
    public void visit(EnumInstructor instructor)
    {
        iwriteLine(instructor.instruct());
    }

    @Override
    public void visit(ALoad instruction)
    {
        iwritefln("aload %d", instruction.getIndex());
    }

//    @Override
//    public void visit(AReturn instruction)
//    {
//        iwriteLine("areturn");
//    }

    @Override
    public void visit(AStore instruction)
    {
        iwritefln("astore %d", instruction.getIndex());
    }

    @Override
    public void visit(Goto instruction)
    {
        iwritefln("goto %s", instruction.getLabel().toString());
    }

    @Override
    public void visit(GetField instruction)
    {
        iwritefln("getfield %s %s", instruction.getFieldSpec(), instruction.getDescriptor());
    }

//    @Override
//    public void visit(IAdd instruction)
//    {
//        iwriteLine("iadd");
//    }

    @Override
    public void visit(Ificmplt instruction)
    {
        iwritefln("if_icmplt %s", instruction.getLabel().toString());
    }

    @Override
    public void visit(ILoad instruction)
    {
        iwritefln("iload %d", instruction.getIndex());
    }

//    @Override
//    public void visit(IMul instruction)
//    {
//        iwriteLine("imul");
//    }

    @Override
    public void visit(InvokeVirtual instruction)
    {
        iwritef("invokevirtual %s/%s(", instruction.getFirstFieldType(), instruction.getFuncName());
        instruction.getArgTypeList().forEach(this::visit);
        write(")");
        visit(instruction.getRetType());
        writeln();
    }

//    @Override
//    public void visit(IReturn instruction)
//    {
//        iwriteLine("ireturn");
//    }

    @Override
    public void visit(IStore instruction)
    {
        iwritefln("istore %d", instruction.getIndex());
    }

//    @Override
//    public void visit(ISub instruction)
//    {
//        iwriteLine("isub");
//    }

    @Override
    public void visit(LabelJ instruction)
    {
        writef("%s:\n", instruction.getLabel().toString());
    }

    @Override
    public void visit(Ldc instruction)
    {
        iwritefln("ldc %s", instruction.value());
    }

    @Override
    public void visit(New instruction)
    {
        iwritefln("new %s", instruction.getNewClassName());
        iwriteLine("dup");
        iwritefln("invokespecial %s/<init>()V", instruction.getNewClassName());
    }

    @Override
    public void visit(WriteInstructor instruction)
    {
        String mode = writeModeMap.get(instruction.getWriteMode());
        String type = writeTypeMap.get(instruction.getWriteType());
        iwriteLine("getstatic java/lang/System/out Ljava/io/PrintStream;");
        iwriteLine("swap");
        iwritefln("invokevirtual java/io/PrintStream/%s(%s)V", mode, type);
    }

    @Override
    public void visit(PutField instruction)
    {
        iwritefln("putfield %s %s", instruction.getFieldSpec(), instruction.getDescriptor());
    }

    @Override
    public void visit(TargetMethod targetMethod)
    {
        writef(".method public %s(", targetMethod.getName());
        targetMethod.getFormalList().forEach(f -> visit(f.getType()));
        write(")");
        visit(targetMethod.getRetType());
        writeln();
        writef(".limit stack %d\n", StackConst.MAX_STACK_DEPTH);
        writef(".limit locals %d\n", targetMethod.getIndex() + 1);

        targetMethod.getStatementList().forEach(this::visit);
        writeln(".end method");
    }

    @Override
    public void visit(TargetClass targetClass)
    {
        initWriter(String.format("%s.il", targetClass.getLiteral()));
        writef(".class public %s\n", targetClass.getLiteral());
        if (targetClass.getParent() == null)
        {
            writeln(".super java/lang/Object");
        }
        else
        {
            writef(".super %s\n", targetClass.getParent());
        }

        targetClass.getFieldList().forEach(f ->
        {
            writef(".field public %s ", f.getLiteral());
            visit(f.getType());
            writeln();
        });

        writeln(".method public <init>()V");
        iwriteLine("aload 0");
        if (targetClass.getParent() == null)
        {
            iwriteLine("invokespecial java/lang/Object/<init>()V");
        }
        else
        {
            iwritefln("invokespecial %s/<init>()V", targetClass.getParent());
        }
        iwriteLine("return");
        writeln(".end method");
        targetClass.getMethodList().forEach(this::visit);

        saveAndReinit();
    }

    @Override
    public void visit(TargetEntryClass entryClass)
    {
        initWriter(String.format("%s.il", entryClass.getName()));

        writef(".class public %s\n", entryClass.getName());
        writeln(".super java/lang/Object");
        writeln(".method public static main([Ljava/lang/String;)V");
        writef(".limit stack %d\n", StackConst.MAX_STACK_DEPTH);
        writeln(".limit locals 2");
        entryClass.getStatementList().forEach(this::visit);
        iwriteLine("return");
        writeln(".end method");

        saveAndReinit();
    }

    @Override
    public void visit(TargetProgram targetProgram)
    {
         visit(targetProgram.getEntryClass());
        targetProgram.getClassList().forEach(this::visit);
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
