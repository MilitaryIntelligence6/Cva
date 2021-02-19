//package cn.misection.cvac.unit;
//
//import cn.misection.cvac.ast.program.CvaProgram;
//import cn.misection.cvac.io.BufferedQueueHandler;
//import cn.misection.cvac.io.IBufferedQueue;
//import cn.misection.cvac.parser.Parser;
//
//import java.io.*;
//
///**
// * Created by MI6 root 1/12.
// */
//public class ParserTest
//{
//    public static void main(String[] args)
//    {
//        final String fname;
//        if (args.length > 0)
//            fname = args[0];
//        else fname = "res/cvasrc/debug.cva";
//
//        IBufferedQueue fstream = null;
//        try
//        {
//            fstream = new BufferedQueueHandler(
//                    new FileReader(fname));
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        AstPrintVisitor visitor = new AstPrintVisitor();
//        Parser parser = new Parser(fstream);
//        visitor.visit(((CvaProgram) parser.parse()));
//    }
//}
