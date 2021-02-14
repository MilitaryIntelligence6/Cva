package cn.misection.cvac.lexer;

import cn.misection.cvac.lexer.Token.Kind;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mengxu on 2017/1/6.
 */
public class Lexer
{
    private InputStream fstream; // input stream of the file
    private int lineNum;

    public Lexer(InputStream fstream)
    {
        this.fstream = fstream;
        this.lineNum = 1;
    }

    public Token nextToken()
    {
        Token token = null;
        try
        {
            token = nextTokenInternal();
        }
        catch (IOException e)
        {
            System.out.println("A IO exception!");
            e.printStackTrace();
        }

        return token;
    }

    private Token nextTokenInternal() throws IOException
    {
        int c = this.fstream.read();

        if (-1 == c)
        {
            return new Token(Kind.EOF, lineNum);
        }

        // skip all kinds of blanks
        while (' ' == c || '\t' == c || '\r' == c || '\n' == c)
        {
            if ('\n' == c)
            {
                lineNum++;
            }
            c = this.fstream.read();
        }

        // deal with comments
        if ('/' == c)
        {
            c = fstream.read();
            if ('/' == c)
            {
                while ('\n' != c)
                {
                    c = this.fstream.read();
                }
                lineNum++;
                return nextTokenInternal(); // tail recursion
            }
            else
            {
                System.out.println("Comment should begin with \"//\"");
                System.out.println("Error is found at line " + lineNum);
                System.exit(1);
            }
        }

        switch (c)
        {
            case '+':
                return new Token(Kind.Add, lineNum);
            case '&':
                c = this.fstream.read();
                if ('&' == c)
                {
                    return new Token(Kind.And, lineNum);
                }
                else
                {
                    System.out.println("Expect two &, but only got one at line " + lineNum);
                    System.exit(1);
                }
            case '=':
                return new Token(Kind.Assign, lineNum);
            case ':':
                return new Token(Kind.Colon, lineNum);
            case ',':
                return new Token(Kind.Commer, lineNum);
            case '.':
                return new Token(Kind.Dot, lineNum);
            case '{':
                return new Token(Kind.Lbrace, lineNum);
            case '(':
                return new Token(Kind.Lparen, lineNum);
            case '<':
                return new Token(Kind.LT, lineNum);
            case '!':
                return new Token(Kind.Not, lineNum);
            case '}':
                return new Token(Kind.Rbrace, lineNum);
            case ')':
                return new Token(Kind.Rparen, lineNum);
            case ';':
                return new Token(Kind.Semi, lineNum);
            case '-':
                return new Token(Kind.Sub, lineNum);
            case '*':
                return new Token(Kind.Times, lineNum);
            default:
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true)
                {
                    this.fstream.mark(1);
                    c = this.fstream.read();
                    if (-1 != c && ' ' != c && '\t' != c
                            && '\n' != c && '\r' != c
                            && !isSpecialCharacter(c))
                    {
                        sb.append((char) c);
                    }
                    else
                    {
                        this.fstream.reset();
                        break;
                    }
                }
                switch (sb.toString())
                {
                    case "boolean":
                        return new Token(Kind.Boolean, lineNum);
                    case "class":
                        return new Token(Kind.Class, lineNum);
                    case "else":
                        return new Token(Kind.Else, lineNum);
                    case "false":
                        return new Token(Kind.False, lineNum);
                    case "if":
                        return new Token(Kind.If, lineNum);
                    case "int":
                        return new Token(Kind.Int, lineNum);
                    case "main":
                        return new Token(Kind.Main, lineNum);
                    case "new":
                        return new Token(Kind.New, lineNum);
                    case "print":
                        return new Token(Kind.Print, lineNum);
                    case "return":
                        return new Token(Kind.Return, lineNum);
                    case "this":
                        return new Token(Kind.This, lineNum);
                    case "true":
                        return new Token(Kind.True, lineNum);
                    case "void":
                        return new Token(Kind.Void, lineNum);
                    case "while":
                        return new Token(Kind.While, lineNum);
                    default:
                        if (isNumber(sb.toString()))
                        {
                            return new Token(Kind.NUM, lineNum, sb.toString());
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
