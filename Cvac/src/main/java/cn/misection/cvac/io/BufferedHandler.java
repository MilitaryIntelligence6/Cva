package cn.misection.cvac.io;

import cn.misection.cvac.constant.LexerCommon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName BufferedHandler
 * @Description TODO
 * @TODO 后面把int改回char;
 * @CreateTime 2021年02月14日 14:10:00
 */
public final class BufferedHandler implements IBufferedQueue
{
    private BufferedReader reader = null;

    private final StringBuffer buffer = new StringBuffer();

    public BufferedHandler(String filePath) throws IOException
    {
        reader = new BufferedReader(
                new FileReader(filePath)
        );
        init();
    }

    private void init() throws IOException
    {
        load();
    }

    private void load() throws IOException
    {
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            buffer.append(line).append(LexerCommon.NEW_LINE);
        }
        // 改进完毕, 只需要装载一个EOF了, 必须装载至少一个,
        //因为StringBuffer跟文件流不一样, 不会有EOF;
        // 加上才能模拟文件流;
        buffer.append(LexerCommon.EOF);
        reader.close();
    }

    /**
     * 默认属于peek 1;
     * @return
     */
    @Override
    public char peek()
    {
        // buffer末位是eof, 就不怕溢出了;
        return buffer.charAt(0);
    }

    @Override
    public char peek(int advance)
    {
        // TODO 判空不要在这里, 影响效率, 放到后面lexer中;
        // 这里由于poll掉了, 所以要-1;
        return buffer.length() < advance ? 0 : buffer.charAt(advance - 1);
    }

    @Override
    public char poll()
    {
        char c = peek();
        buffer.delete(0, 1);
        return c;
    }

    @Override
    public String poll(int advance)
    {
        String polled = buffer.substring(0, advance);
        buffer.delete(0, advance);
        return polled;
    }

    @Override
    public boolean hasNext()
    {
        // 当前已经poll掉了, 如果 == 1, 是EOF, 所以至少2;
        return buffer.length() > 1;
    }

    @Override
    public boolean hasNext(int advance)
    {
        return buffer.length() > advance;
    }

    @Override
    public boolean isEmpty()
    {
        return buffer.length() == 0;
    }

    public StringBuffer getBuffer()
    {
        return buffer;
    }

    @Override
    public String toString()
    {
        return String.format("BufferedHandler{buffer=%s}", buffer);
    }
}
