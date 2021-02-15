package cn.misection.cvac;

import cn.misection.cvac.ast.FrontAst;
import cn.misection.cvac.ast.program.AbstractProgram;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.codegen.ByteCodeGenerator;
import cn.misection.cvac.codegen.TranslatorVisitor;
import cn.misection.cvac.codegen.ast.CodeGenAst;
import cn.misection.cvac.config.Macro;
import cn.misection.cvac.lexer.BufferedQueueHandler;
import cn.misection.cvac.lexer.IBufferedQueue;
//import cn.misection.cvac.optimize.Optimizer;
import cn.misection.cvac.parser.Parser;
import cn.misection.cvac.semantic.SemanticVisitor;

import java.io.*;

/**
 * Created by Mengxu on 2017/1/4.
 */
public class CvaCompiler
{
    private static final String ORDINARY = "-o";

    private static final String ORDINARY_0 = "-o0";

    private static final String ORDINARY_1 = "-o1";

    private static final String ORDINARY_2 = "-o2";

    private static final String ORDINARY_3 = "-o3";

    private static final String PEEK_VERSION = "-v";

    private static final String COMPILE_TO_CLASS = "-c";

    private static final String COMPILE_TO_IL = "-i";

    private static final int NORMAL_EXIT_STATUS = 0;

    private static final int ERROR_EXIT_STATUS = 1;


    public static final String CURRENT_SHELL_PATH = System.getProperty("user.dir");

    private static final String DEBUG_FILE = "res/cvasrc/debug.cva";

//    private static final String DEBUG_IL_DIR = String.format("%s/debug/il", CURRENT_SHELL_PATH);
    private static final String DEBUG_IL_DIR = "debug/il";

//    private static final String DEBUG_CLASS_DIR = String.format("%s/debug/classes", CURRENT_SHELL_PATH);
    private static final String DEBUG_CLASS_DIR = "debug/classes";


    public static void main(String[] args)
    {
        String fName = null;
        if (Macro.RELEASE)
        {
            fName = release(args);
        }
        if (Macro.DEBUG)
        {
            fName = DEBUG_FILE;
        }
        IBufferedQueue fStream = null;
        try
        {
            fStream = new BufferedQueueHandler(new FileReader(fName));
        }
        catch (IOException e)
        {
            System.out.printf("Cannot find the file: %s%n", fName);
            System.exit(ERROR_EXIT_STATUS);
        }
        geneCode(fStream);
    }

    private static void geneCode(IBufferedQueue fStream)
    {
        Parser parser = new Parser(fStream);
        AbstractProgram program = parser.parse();

        doCheck(program);

//        Optimizer optimizer = new Optimizer();
//        optimizer.optimize(prog);

        TranslatorVisitor translator = new TranslatorVisitor();
        translator.visit(program);

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(translator.prog);

        doMkDIrs();

        // 现在是从il读到文件中而不是先创建il, il步骤在前, 需要设定一个全局;
        String ilPath = String.format("%s.il", translator.prog.mainClass.getLiteral());
//        String ilPath = String.format("%s/%s.il", DEBUG_IL_DIR, translator.prog.mainClass.id);
//        mkFile(ilPath);
        // ascii instructions to binary file
        jasmin.Main.main(new String[] {ilPath});

        for (CodeGenAst.Class.ClassSingle cla : translator.prog.classes)
        {
            String filePath = String.format("%s.il", cla.getLiteral());
//            String filePath = String.format("%s/%s.il", DEBUG_IL_DIR, cla.id);
//            mkFile(filePath);
            jasmin.Main.main(new String[] {filePath});
        }
    }

    private static void doCheck(AbstractProgram prog)
    {
        SemanticVisitor checker = new SemanticVisitor();
        checker.visit(prog);

        // if the program is correct, we generate code for it
        if (!checker.isOK())
        {
            System.err.println("ERROE: check failed");
            System.exit(ERROR_EXIT_STATUS);
        }
    }

    private static String release(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Hello, this is a simple compiler!");
            System.out.println("Please input the file name which you want to compile");
            System.exit(NORMAL_EXIT_STATUS);
        }
        return args[0];
    }

    private static boolean mkDirs(String dirPath)
    {
        File dir = new File(dirPath);
        if (dir.exists())
        {
            if (dir.isFile())
            {
                System.err.println("ERROR: 当前路径下存在同名文件, 请清除后再编译!");
                System.exit(ERROR_EXIT_STATUS);
            }
            return true;
        }
        else
        {
            return dir.mkdirs();
        }
    }

    private static void doMkDIrs()
    {
        if (!mkDirs(DEBUG_CLASS_DIR))
        {
            System.err.printf("mkdir %s failed!\n", DEBUG_CLASS_DIR);
        }
        if (!mkDirs(DEBUG_IL_DIR))
        {
            System.err.printf("mkdir %s failed\n", DEBUG_IL_DIR);
        }
    }

    private static void mkFile(String filePath)
    {
        File ilFile = new File(filePath);
        if (!ilFile.exists())
        {
            try
            {
                if (!ilFile.createNewFile())
                {
                    System.err.printf("创建文件%s失败!\n", ilFile);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
