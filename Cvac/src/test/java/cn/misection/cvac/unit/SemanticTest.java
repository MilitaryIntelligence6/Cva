package cn.misection.cvac.unit;

import cn.misection.cvac.ast.program.AbstractProgram;
import cn.misection.cvac.io.BufferedHandler;
import cn.misection.cvac.io.IBufferedQueue;
import cn.misection.cvac.parser.Parser;
import cn.misection.cvac.semantic.SemanticVisitor;

import java.io.IOException;

/**
 * Created by MI6 root 1/14.
 */
public class SemanticTest
{
    public static void main(String[] args)
    {
        final String fname;
        if (args.length > 0)
        {
            fname = args[0];
        }
        else
        {
            fname = "res/cvasrc/debug.cva";
        }

        IBufferedQueue fstream = null;
        try
        {
            fstream = new BufferedHandler(fname);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        Parser parser = new Parser(fstream);
        AbstractProgram prog = parser.parse();

        SemanticVisitor visitor = new SemanticVisitor();
        visitor.visit(prog);

        //new cn.misection.cvac.unit.AstPrintVisitor().visit(prog);
    }
}
