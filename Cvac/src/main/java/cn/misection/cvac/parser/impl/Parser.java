package cn.misection.cvac.parser.impl;

import cn.misection.cvac.lexer.Lexer;
import cn.misection.cvac.lexer.IBufferedQueue;
import cn.misection.cvac.lexer.Token;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Parser
 * @Description TODO
 * @CreateTime 2021年02月14日 16:49:00
 */
public class Parser
{
    private Lexer lexer;

    /**
     * 目前的Token;
     */
    private Token curToken;

    /**
     * for vardecl cn.misection.cvac.parser;
     */
    private boolean varDeclFlag;

    private boolean markingFlag = false;

    private Queue<Token> markedTokenQueue;

    public Parser(IBufferedQueue queueStream)
    {
        lexer = new Lexer(queueStream);
        curToken = lexer.nextToken();
        markedTokenQueue = new LinkedList<>();
    }

    private void advance()
    {
        if (markingFlag)
        {
            curToken = lexer.nextToken();
            markedTokenQueue.offer(curToken);
        }
        // 没有标记;
        // 队列空, 继续解析;
        else if (markedTokenQueue.isEmpty())
        {
            curToken = lexer.nextToken();
        }
        // 没标记但队列不空, 获取出队;
        else
        {
            // 队列中有就出队;
            curToken = markedTokenQueue.poll();
        }
    }

    /**
     * 开始记录 buff;
     */
    private void mark()
    {
        markingFlag = true;
        markedTokenQueue.offer(curToken);
    }

    /**
     * 解除记录buff;
     * @SideEffect 清空队列;
     */
    private void deMark()
    {
        markingFlag = false;
        // 队伍出清;
        markedTokenQueue.clear();
    }

    private void reset()
    {
        markingFlag = false;
        advance();
    }

//    private void eatToken
}
