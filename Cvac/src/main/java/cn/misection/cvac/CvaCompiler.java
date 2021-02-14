package cn.misection.cvac;

import cn.misection.cvac.ast.Ast;
import cn.misection.cvac.codegen.ByteCodeGenerator;
import cn.misection.cvac.codegen.TranslatorVisitor;
import cn.misection.cvac.config.Macro;
import cn.misection.cvac.optimize.Optimizer;
import cn.misection.cvac.parser.Parser;
import cn.misection.cvac.semantic.SemanticVisitor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Mengxu on 2017/1/4.
 */
public class CvaCompiler
{
    private static final String DEBUG_FILE = "res/cvasrc/debug.cva";

    private static String release(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Hello, this is a simple compiler!");
            System.out.println("Please input the file name which you want to compile");
            System.exit(0);
        }
        return args[0];
    }


    public static void main(String[] args)
    {
        String fname = null;
        if (Macro.RELEASE)
        {
            fname = release(args);
        }
        if (Macro.DEBUG)
        {
            fname = DEBUG_FILE;
        }
        InputStream fstream = null;
        try
        {
            fstream = new BufferedInputStream(new FileInputStream(fname));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Cannot find the file: " + fname);
            System.exit(1);
        }

        Parser parser = new Parser(fstream);
        Ast.Program.T prog = parser.parse();

        SemanticVisitor checker = new SemanticVisitor();
        checker.visit(prog);

        // if the program is correct, we generate code for it
        if (!checker.isOK())
        {
            return;
        }

        Optimizer optimizer = new Optimizer();
        optimizer.optimize(prog);

        TranslatorVisitor translator = new TranslatorVisitor();
        translator.visit(prog);

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(translator.prog);

        // ascii instructions to binary file
        jasmin.Main.main(new String[]{translator.prog.mainClass.id + ".il"});
        for (cn.misection.cvac.codegen.ast.Ast.Class.ClassSingle cla : translator.prog.classes)
        {
            jasmin.Main.main(new String[]{cla.id + ".il"});
        }

    }
}
