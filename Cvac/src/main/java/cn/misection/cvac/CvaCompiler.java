package cn.misection.cvac;

import cn.misection.cvac.ast.program.AbstractProgram;
import cn.misection.cvac.codegen.IntermLangGenerator;
import cn.misection.cvac.codegen.TranslatorVisitor;
import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.config.DebugMacro;
import cn.misection.cvac.config.Macro;
import cn.misection.cvac.config.VersionMacro;
import cn.misection.cvac.constant.UserIntfConst;
import cn.misection.cvac.io.BufferedHandler;
import cn.misection.cvac.io.IBufferedQueue;
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

    /**
     * 用户选择;
     */
    private static boolean optimizeFlag = true;

    public static void main(String[] args)
    {
        logBanner();
        String fName = null;
        if (Macro.RELEASE_WINDOWS)
        {
            fName = release(args);
        }
        if (Macro.DEBUG)
        {
            fName = DebugMacro.DEBUG_FILE;
        }
        IBufferedQueue fStream = readStream(fName);
        AbstractProgram program = grammarAnalysis(fStream);
        geneCode(program);
    }

    private static void geneCode(AbstractProgram program)
    {
        if (optimizeFlag)
        {
            optimize(program);
        }
        System.out.println("start intermediate code gene");
        TranslatorVisitor translator = new TranslatorVisitor();
        translator.visit(program);

        IntermLangGenerator generator = new IntermLangGenerator();
        generator.visit(translator.getGenProgram());
        System.out.println("finish intermediate code gene\n");

        doMkDIrs();

        System.out.println("start gene .class file\n");

        // 现在是从il读到文件中而不是先创建il, il步骤在前, 需要设定一个全局;
        String ilPath = String.format("%s.il", translator.getGenProgram().getEntry().getName());
        // ascii instructions to binary file
        jasmin.Main.main(new String[] {ilPath});

        for (GenClass cla : translator.getGenProgram().getClassList())
        {
            String filePath = String.format("%s.il", cla.getLiteral());
            jasmin.Main.main(new String[] {filePath});
        }
        System.out.println("\nwell down!\n");
    }

    private static AbstractProgram grammarAnalysis(IBufferedQueue fStream)
    {
        System.out.println("\nstart grammar analysis");
        Parser parser = new Parser(fStream);
        AbstractProgram program = parser.parse();
        doCheck(program);
        System.out.println("finish grammar analysis\n");
        return program;
    }

    private static void optimize(AbstractProgram program)
    {
        System.out.println("start optimize");
        Optimizer optimizer = new Optimizer();
        optimizer.optimize(program);
        System.out.println("finish optimize\n");
    }

    private static void doCheck(AbstractProgram program)
    {
        SemanticVisitor checker = new SemanticVisitor();
        checker.visit(program);
        // if the program is correct, we generate code for it
        if (!checker.isOK())
        {
            System.err.println("ERROE: check failed");
            System.exit(1);
        }
    }

    private static String release(String[] args)
    {
        if (args.length == 0)
        {
            System.out.printf("\nHello, welcome to  Cva compiler! \n" +
                    "Please input the file name which you want to compile\n" +
                    "Cvac version %s\n", VersionMacro.VERSION);
            System.exit(0);
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
                System.exit(1);
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
        if (!mkDirs(DebugMacro.DEBUG_CLASS_DIR))
        {
            System.err.printf("mkdir %s failed!\n", DebugMacro.DEBUG_CLASS_DIR);
        }
        if (!mkDirs(DebugMacro.DEBUG_IL_DIR))
        {
            System.err.printf("mkdir %s failed\n", DebugMacro.DEBUG_IL_DIR);
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
        if (Macro.DEBUG || Macro.RELEASE_LINUX)
        {
            System.out.println(UserIntfConst.BANNER);
        }
        if (Macro.RELEASE_WINDOWS)
        {
            String BANNER_GBK = null;
            try
            {
                BANNER_GBK = new String(UserIntfConst.BANNER.getBytes(),
                        "GBK");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            System.out.println(BANNER_GBK);
        }
    }

    private static IBufferedQueue readStream(String fName)
    {
        try
        {
            IBufferedQueue fStream = new BufferedHandler(fName);
            return fStream;
        }
        catch (IOException e)
        {
            System.err.printf("Cannot find the file: %s%n", fName);
            System.exit(1);
        }
        // 不可达;
        return null;
    }
}
