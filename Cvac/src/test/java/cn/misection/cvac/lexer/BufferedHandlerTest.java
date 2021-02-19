package cn.misection.cvac.lexer;

import cn.misection.cvac.io.BufferedHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * BufferedHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>2月 14, 2021</pre>
 */
public class BufferedHandlerTest
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
     * Method: load()
     */
    @Test
    public void testLoad() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: read()
     */
    @Test
    public void testRead() throws Exception
    {
//TODO: Test goes here... 
    }


    /**
     * Method: pollChar(int num)
     */
    @Test
    public void testPollCharNum() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = BufferedHandler.getClass().getMethod("pollChar", int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: pollChar()
     */
    @Test
    public void testPollChar() throws Exception
    {
//TODO: Test goes here... 
/* 
try { 
   Method method = BufferedHandler.getClass().getMethod("pollChar"); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    private static final int MAGIC_NUMBER = 20;

    public static void main(String[] args) throws IOException
    {
        String path = "debug/hello/hello.c";
//        File file = new File(path);
        BufferedHandler handler = new BufferedHandler(path);

        System.out.printf("handler.getBuffer() = %s%n", handler.getBuffer());


        for (int i = 0; i < MAGIC_NUMBER; i++)
        {
            System.out.printf("handler.peek() = %s%n", handler.peek());
        }

        for (int i = 0; i < MAGIC_NUMBER; i++)
        {
            System.out.printf("handler.poll() = %s%n", handler.poll());
        }
    }
} 
