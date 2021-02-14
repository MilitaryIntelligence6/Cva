package cn.misection.cvac.lexer;

import cn.misection.cvac.config.Macro;

import java.io.IOException;

/**
 * Created by Mengxu on 2017/1/6.
 */
public class Lexer
{
    /**
     * input stream of the file;
     */
    private QueueHandleable queueStream;

    private int lineNum;

    public Lexer(QueueHandleable queueStream)
    {
        this.queueStream = queueStream;
        this.lineNum = 1;
    }

    public Token nextToken()
    {
        Token token = null;
        token = nextTokenInternal();

        return token;
    }

    private Token nextTokenInternal()
    {
        int c = this.queueStream.poll();

//        if (queueStream.isEmpty())
//        {
//            return new Token(Kind.EOF, lineNum);
//        }
        if (c == -1)
        {
            if (Macro.DEBUG)
            {
                System.err.println("bug place");
            }
            return new Token(Kind.EOF, lineNum);
        }


        // skip all kinds of blanks
        while (' ' == c || '\t' == c || '\r' == c || '\n' == c)
        {
            if ('\n' == c)
            {
                lineNum++;
            }
            c = this.queueStream.poll();
        }

        // deal with comments
        if ('/' == c)
        {
            c = queueStream.poll();
            if ('/' == c)
            {
                while ('\n' != c)
                {
                    c = this.queueStream.poll();
                }
                lineNum++;
                return nextTokenInternal(); // tail recursion
            }
            else
            {
                System.out.println("Comment should begin with \"//\"");
                System.out.printf("Error is found at line %d%n", lineNum);
                System.exit(1);
            }
        }

        switch (c)
        {
            case '+':
                return new Token(Kind.ADD, lineNum);
            case '&':
                c = this.queueStream.poll();
                if ('&' == c)
                {
                    return new Token(Kind.AND, lineNum);
                }
                else
                {
                    System.out.printf("Expect two &, but only got one at line %d%n", lineNum);
                    System.exit(1);
                }
            case '=':
                return new Token(Kind.ASSIGN, lineNum);
            case ':':
                return new Token(Kind.COLON, lineNum);
            case ',':
                return new Token(Kind.COMMA, lineNum);
            case '.':
                return new Token(Kind.DOT, lineNum);
            case '{':
                return new Token(Kind.OPEN_CURLY_BRACE, lineNum);
            case '(':
                return new Token(Kind.OPEN_PAREN, lineNum);
            case '<':
                return new Token(Kind.LESS_THAN, lineNum);
            case '!':
                return new Token(Kind.NEGATE, lineNum);
            case '}':
                return new Token(Kind.CLOSE_BRACE, lineNum);
            case ')':
                return new Token(Kind.CLOSE_PAREN, lineNum);
            case ';':
                return new Token(Kind.SEMI, lineNum);
            case '-':
                return new Token(Kind.SUB, lineNum);
            case '*':
                return new Token(Kind.STAR, lineNum);
            default:
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true)
                {
//                    this.queueStream.mark(1);
                    c = queueStream.peek();
                    if (-1 != c && ' ' != c && '\t' != c
                            && '\n' != c && '\r' != c
                            && !isSpecialCharacter(c))
                    {
                        sb.append((char) c);
                        this.queueStream.poll();
                    }
                    else
                    {
//                        this.queueStream.reset();
                        break;
                    }
                }
                switch (sb.toString())
                {
                    case "boolean":
                        return new Token(Kind.BOOLEAN, lineNum);
                    case "class":
                        return new Token(Kind.CLASS, lineNum);
                    case "else":
                        return new Token(Kind.ELSE, lineNum);
                    case "false":
                        return new Token(Kind.FALSE, lineNum);
                    case "if":
                        return new Token(Kind.IF, lineNum);
                    case "int":
                        return new Token(Kind.INT, lineNum);
                    case "main":
                        return new Token(Kind.MAIN, lineNum);
                    case "new":
                        return new Token(Kind.NEW, lineNum);
                    case "print":
                        return new Token(Kind.WRITE, lineNum);
                    case "return":
                        return new Token(Kind.RETURN, lineNum);
                    case "this":
                        return new Token(Kind.THIS, lineNum);
                    case "true":
                        return new Token(Kind.TRUE, lineNum);
                    case "void":
                        return new Token(Kind.VOID, lineNum);
                    case "while":
                        return new Token(Kind.WHILE, lineNum);
                    default:
                        if (isNumber(sb.toString()))
                        {
                            return new Token(Kind.NUMBER, lineNum, sb.toString());
                        }
                        else if (isIdentifier(sb.toString()))
                        {
                            return new Token(Kind.ID, lineNum, sb.toString());
                        }
                        else
                        {
                            System.out.println("This is an illegal identifier at line " + lineNum);
                            System.exit(1);
                            return null;
                        }
                }
        }
    }

    private static boolean isSpecialCharacter(int c)
    {
        return '+' == c || '&' == c || '=' == c || ',' == c || '.' == c
                || '{' == c || '(' == c || '<' == c || '!' == c
                || '}' == c || ')' == c || ';' == c || ':' == c
                || '-' == c || '*' == c;
    }

    private static boolean isNumber(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) < '0' || str.charAt(i) > '9')
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isIdentifier(String str)
    {
        return str.charAt(0) >= 'a' || str.charAt(0) <= 'z'
                || str.charAt(0) >= 'A' || str.charAt(0) <= 'Z'
                || str.charAt(0) == '_';
    }

}
