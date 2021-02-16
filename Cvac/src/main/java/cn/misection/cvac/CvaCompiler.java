package cn.misection.cvac;

import cn.misection.cvac.ast.program.AbstractProgram;
import cn.misection.cvac.codegen.IntermLangGenerator;
import cn.misection.cvac.codegen.TranslatorVisitor;
import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.config.Macro;
import cn.misection.cvac.constant.ConstPool;
import cn.misection.cvac.lexer.BufferedQueueHandler;
import cn.misection.cvac.lexer.IBufferedQueue;
import cn.misection.cvac.optimize.Optimizer;
import cn.misection.cvac.parser.Parser;
import cn.misection.cvac.semantic.SemanticVisitor;

import java.io.*;

/**
 * Created by MI6 root 1/4.
 */
public final class CvaCompiler
{
    private static final String ORDINARY = "-o";

    private static final String ORDINARY_0 = "-o0";

    private static final String ORDINARY_1 = "-o1";

    private static final String ORDINARY_2 = "-o2";

    private static final String ORDINARY_3 = "-o3";

    private static final String PEEK_VERSION = "-v";

    private static final String COMPILE_TO_CLASS = "-c";

    private static final String COMPILE_TO_IL = "-i";

    public static final String VERSION_INFO = "1.0.0";

    private static final int NORMAL_EXIT_STATUS = 0;

    private static final int ERROR_EXIT_STATUS = 1;

    private static final String DEBUG_FILE = "res/cvasrc/debug.cva";

    private static final String DEBUG_IL_DIR = "debug/il";

    private static final String DEBUG_CLASS_DIR = "debug/classes";


    public static void main(String[] args)
    {
        logBanner();
        String fName = null;
        if (Macro.RELEASE)
        {
            fName = release(args);
        }
        if (Macro.DEBUG)
        {
            fName = DEBUG_FILE;
        }
        IBufferedQueue fStream = readStream(fName);
        geneCode(fStream);
    }

    private static void geneCode(IBufferedQueue fStream)
    {
        Parser parser = new Parser(fStream);
        AbstractProgram program = parser.parse();

        doCheck(program);

        Optimizer optimizer = new Optimizer();
        optimizer.optimize(program);

        TranslatorVisitor translator = new TranslatorVisitor();
        translator.visit(program);

        IntermLangGenerator generator = new IntermLangGenerator();
        generator.visit(translator.getProg());

        doMkDIrs();
        // 现在是从il读到文件中而不是先创建il, il步骤在前, 需要设定一个全局;
        String ilPath = String.format("%s.il", translator.getProg().getEntry().getLiteral());
        // ascii instructions to binary file
        jasmin.Main.main(new String[] {ilPath});

        for (GenClass cla : translator.getProg().getClassList())
        {
            String filePath = String.format("%s.il", cla.getLiteral());
            jasmin.Main.main(new String[] {filePath});
        }
    }

    private static void doCheck(AbstractProgram program)
    {
        SemanticVisitor checker = new SemanticVisitor();
        checker.visit(program);
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
            System.out.printf("Hello, this is Cva compiler! \n" +
                    "Please input the file name which you want to compile\n" +
                    "cvac version %s", VERSION_INFO);
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

    private static void logBanner()
    {
        System.out.println(ConstPool.BANNER);
    }

    private static IBufferedQueue readStream(String fName)
    {
        try
        {
            IBufferedQueue fStream = new BufferedQueueHandler(new FileReader(fName));
            return fStream;
        }
        catch (IOException e)
        {
            System.out.printf("Cannot find the file: %s%n", fName);
            System.exit(ERROR_EXIT_STATUS);
        }
        // 不可达;
        return null;
    }
}
