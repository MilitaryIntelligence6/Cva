//package cn.misection.cvac.unit;
//
//import cn.misection.cvac.lexer.Lexer;
//import cn.misection.cvac.lexer.Token;
//
//import java.io.BufferedInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
///**
// * Created by MI6 root 1/6.
// */
//public class LexerTest
//{
//    public static void main(String[] args)
//    {
//        final String fname;
//        if (args.length > 0)
//        {
//            fname = args[0];
//        }
//        else
//        {
//            fname = "res/cvasrc/debug.cva";
//        }
//
//        InputStream fstream = null;
//        try
//        {
//            fstream = new BufferedInputStream(new FileInputStream(fname));
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        Lexer lexer = new Lexer(fstream);
//        Token current;
//
//        int i = 0;
//        do
//        {
//            current = lexer.nextToken();
//            System.out.printf("Token %d is %s%n", i, current.toString());
//            i++;
//        } while (current.kind != Token.Kind.EOF);
//
//    }
//}
