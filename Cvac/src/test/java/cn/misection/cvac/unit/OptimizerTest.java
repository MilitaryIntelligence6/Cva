//package cn.misection.cvac.unit;
//
//import cn.misection.cvac.ast.Ast;
//import cn.misection.cvac.optimize.Optimizer;
//import cn.misection.cvac.parser.Parser;
//import cn.misection.cvac.semantic.SemanticVisitor;
//
//import java.io.BufferedInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
///**
// * Created by Mengxu on 2017/1/23.
// */
//public class OptimizerTest
//{
//    public static void main(String[] args)
//    {
//        if (args.length == 0)
//        {
//            System.out.println("Hello, this is a simple compiler!");
//            System.out.println("Please input the file name which you want to compile");
//            System.exit(0);
//        }
//
//        final String fname = args[0];
//        InputStream fstream = null;
//        try
//        {
//            fstream = new BufferedInputStream(new FileInputStream(fname));
//        } catch (FileNotFoundException e)
//        {
//            System.out.println("Cannot find the file: " + fname);
//            System.exit(1);
//        }
//
//        Parser parser = new Parser(fstream);
//        Ast.Program.T prog = parser.parse();
//
//        SemanticVisitor checker = new SemanticVisitor();
//        checker.visit(prog);
//
//        // if the program is correct, we generate code for it
//        if (!checker.isOK())
//            return;
//
//        Optimizer optimizer = new Optimizer();
//        optimizer.optimize(prog);
//
//        AstPrintVisitor printer = new AstPrintVisitor();
//        printer.visit(prog);
//
//    }
//}
