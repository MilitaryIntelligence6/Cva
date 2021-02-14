package cn.misection.cvac.lexer;

import cn.misection.cvac.config.Macro;

/**
 * Created by Mengxu on 2017/1/6.
 */
public class Lexer
{
    /**
     * input stream of the file;
     */
    private IBufferedQueue queueStream;

    private int lineNum;

    public Lexer(IBufferedQueue queueStream)
    {
        this.queueStream = queueStream;
        this.lineNum = 1;
    }

    public CvaToken nextToken()
    {
        CvaToken token = null;
        token = lex();

        return token;
    }

    private CvaToken lex()
    {
        int c = this.queueStream.poll();

        // skip all kinds of blanks
        while (c == ' ' || c == '\t' || c == '\r' || c == '\n')
        {
            if ('\n' == c)
            {
                lineNum++;
            }
            c = this.queueStream.poll();
        }

        // deal with comments
        if (c == '/')
        {
            char nextCh = (char) queueStream.peek();
            switch (nextCh)
            {
                case '/':
                {
                    while (nextCh != ConstantPool.NEW_LINE)
                    {
                        nextCh = (char) this.queueStream.poll();
                    }
                    lineNum++;
                    // tail recursion;
                    return lex();
                }
                case '*':
                {
                    // TODO Block comment;
                    break;
                }
                default:
//                System.out.println("Comment should begin with \"//\"");
//                System.out.printf("Error is found at line %d%n", lineNum);
//                System.exit(1);
                {
                    return new CvaToken(CvaKind.DIV_OPERATOR, lineNum);
                }
            }
        }

        switch (c)
        {
            case -1:
            {
                if (Macro.DEBUG)
                {
                    System.err.println("bug place");
                }
                return new CvaToken(CvaKind.EOF, lineNum);
            }
            case '+':
                return new CvaToken(CvaKind.ADD, lineNum);
            case '-':
                return new CvaToken(CvaKind.SUB, lineNum);
            case '*':
                return new CvaToken(CvaKind.STAR, lineNum);
            case '&':
                c = this.queueStream.poll();
                if ('&' == c)
                {
                    return new CvaToken(CvaKind.AND_AND, lineNum);
                }
                else
                {
                    System.out.printf("Expect two &, but only got one at line %d%n", lineNum);
                    System.exit(1);
                }
            case '=':
                return new CvaToken(CvaKind.ASSIGN, lineNum);
            case ':':
                return new CvaToken(CvaKind.COLON, lineNum);
            case ',':
                return new CvaToken(CvaKind.COMMA, lineNum);
            case '.':
                return new CvaToken(CvaKind.DOT, lineNum);
            case '{':
                return new CvaToken(CvaKind.OPEN_CURLY_BRACE, lineNum);
            case '(':
                return new CvaToken(CvaKind.OPEN_PAREN, lineNum);
            case '<':
                return new CvaToken(CvaKind.LESS_THAN, lineNum);
            case '!':
                return new CvaToken(CvaKind.NEGATE, lineNum);
            case '}':
                return new CvaToken(CvaKind.CLOSE_CURLY_BRACE, lineNum);
            case ')':
                return new CvaToken(CvaKind.CLOSE_PAREN, lineNum);
            case ';':
                return new CvaToken(CvaKind.SEMI, lineNum);
            default:
            {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true)
                {
                    c = queueStream.peek();
                    if (c != -1 && c != ' ' && c != '\t'
                            && c != '\n' && c != '\r'
                            && !isSpecialCharacter(c))
                    {
                        sb.append((char) c);
                        this.queueStream.poll();
                    }
                    else
                    {
                        break;
                    }
                }
                switch (sb.toString())
                {
                    case "boolean":
                        return new CvaToken(CvaKind.BOOLEAN, lineNum);
                    case "class":
                        return new CvaToken(CvaKind.CLASS, lineNum);
                    case "else":
                        return new CvaToken(CvaKind.ELSE, lineNum);
                    case "false":
                        return new CvaToken(CvaKind.FALSE, lineNum);
                    case "if":
                        return new CvaToken(CvaKind.IF, lineNum);
                    case "int":
                        return new CvaToken(CvaKind.INT, lineNum);
                    case "main":
                        return new CvaToken(CvaKind.MAIN, lineNum);
                    case "new":
                        return new CvaToken(CvaKind.NEW, lineNum);
                    // TODO 删掉对print的兼容;
                    case "print":
                    case "printf":
                    case "echo":
                        return new CvaToken(CvaKind.WRITE, lineNum);
                    case "return":
                        return new CvaToken(CvaKind.RETURN, lineNum);
                    case "this":
                        return new CvaToken(CvaKind.THIS, lineNum);
                    case "true":
                        return new CvaToken(CvaKind.TRUE, lineNum);
                    case "void":
                        return new CvaToken(CvaKind.VOID, lineNum);
                    case "while":
                        return new CvaToken(CvaKind.WHILE, lineNum);
                    default:
                    {
                        if (isNumber(sb.toString()))
                        {
                            return new CvaToken(CvaKind.NUMBER, lineNum, sb.toString());
                        }
                        else if (isIdentifier(sb.toString()))
                        {
                            return new CvaToken(CvaKind.IDENTIFIER, lineNum, sb.toString());
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
        // 只接受字母开头, 不接受下划线开头;
        return Character.isAlphabetic(str.charAt(0));
    }

}
