package cn.misection.cvac.lexer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * Lexer Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>2月 14, 2021</pre>
 */
public class LexerTest
{

    @Before
    public void before() throws Exception
    {
    }

    @After
    public void after() throws Exception
    {
    }

    /**
     * Method: nextToken()
     */
    @Test
    public void testNextToken() throws Exception
    {
//TODO: Test goes here... 
    }


    /**
     * Method: nextTokenInternal()
     */
    @Test
    public void testNextTokenInternal() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = Lexer.getClass().getMethod("nextTokenInternal"); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: isSpecialCharacter(int c)
     */
    @Test
    public void testIsSpecialCharacter() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = Lexer.getClass().getMethod("isSpecialCharacter", int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: isNumber(String str)
     */
    @Test
    public void testIsNumber() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = Lexer.getClass().getMethod("isNumber", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: isIdentifier(String str)
     */
    @Test
    public void testIsIdentifier() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = Lexer.getClass().getMethod("isIdentifier", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

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
            fstream = new BufferedQueueHandler(new FileReader(fname));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        Lexer lexer = new Lexer(fstream);
        Token current;

        int i = 0;
        do
        {
            current = lexer.nextToken();
            System.out.printf("Token %d is %s%n", i, current.toString());
            i++;
        } while (current.getKind() != Kind.EOF);

    }
} 
