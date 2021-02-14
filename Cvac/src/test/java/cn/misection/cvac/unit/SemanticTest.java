//package cn.misection.cvac.unit;
//
//import cn.misection.cvac.ast.Ast;
//import cn.misection.cvac.parser.Parser;
//import cn.misection.cvac.semantic.SemanticVisitor;
//
//import java.io.BufferedInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
///**
// * Created by Mengxu on 2017/1/14.
// */
//public class SemanticTest
//{
//    public static void main(String[] args)
//    {
//        final String fname;
//        if (args.length > 0)
//            fname = args[0];
//        else fname = "Example.soo";
//
//        InputStream fstream = null;
//        try
//        {
//            fstream = new BufferedInputStream(new FileInputStream(fname));
//        } catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        Parser parser = new Parser(fstream);
//        Ast.Program.T prog = parser.parse();
//
//        SemanticVisitor visitor = new SemanticVisitor();
//        visitor.visit(prog);
//
//        //new cn.misection.cvac.unit.AstPrintVisitor().visit(prog);
//    }
//}
