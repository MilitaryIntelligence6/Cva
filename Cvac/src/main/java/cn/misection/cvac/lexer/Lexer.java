package cn.misection.cvac.lexer;

import cn.misection.cvac.config.Macro;

/**
 * Created by MI6 root 1/6.
 */
public final class Lexer
{
    /**
     * input stream of the file;
     */
    private final IBufferedQueue stream;

    private int lineNum;

    public Lexer(IBufferedQueue stream)
    {
        this.stream = stream;
        this.lineNum = 1;
    }

    public CvaToken nextToken()
    {
        return lex();
    }

    private CvaToken lex()
    {
        char ch = this.stream.poll();

        // skip all kinds of blanks
        while (Character.isWhitespace(ch))
        {
            switch (ch)
            {
                case LexerConstPool.NEW_LINE:
                {
                    lineNum++;
                    break;
                }
                case LexerConstPool.EOF:
                {
                    return new CvaToken(CvaKind.EOF, lineNum);
                }
                default:
                {
                    break;
                }
            }
            ch = this.stream.poll();
        }

        // deal with comments
        if (ch == '/')
        {
            char nextCh = stream.peek();
            switch (nextCh)
            {
                case '/':
                {
                    while (nextCh != LexerConstPool.NEW_LINE)
                    {
                        nextCh = this.stream.poll();
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
                {
                    return new CvaToken(CvaKind.DIV_OPERATOR, lineNum);
                }
            }
        }
        // 把单目符给抽象出来;
        switch (ch)
        {
            case LexerConstPool.EOF:
            {
                return new CvaToken(CvaKind.EOF, lineNum);
            }
            case '+':
                return new CvaToken(CvaKind.ADD, lineNum);
            case '-':
                return new CvaToken(CvaKind.SUB, lineNum);
            case '*':
                return new CvaToken(CvaKind.STAR, lineNum);
            case '&':
                ch = this.stream.poll();
                if ('&' == ch)
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
            case '<':
                return new CvaToken(CvaKind.LESS_THAN, lineNum);
            case '>':
                return new CvaToken(CvaKind.MORE_THAN, lineNum);
//            case ':':
//                return new CvaToken(CvaKind.COLON, lineNum);
//            case ',':
//                return new CvaToken(CvaKind.COMMA, lineNum);
//            case '.':
//                return new CvaToken(CvaKind.DOT, lineNum);

//            case '!':
//                return new CvaToken(CvaKind.NEGATE, lineNum);
//            case '{':
//                return new CvaToken(CvaKind.OPEN_CURLY_BRACE, lineNum);
//            case '}':
//                return new CvaToken(CvaKind.CLOSE_CURLY_BRACE, lineNum);
//            case '(':
//                return new CvaToken(CvaKind.OPEN_PAREN, lineNum);
//            case ')':
//                return new CvaToken(CvaKind.CLOSE_PAREN, lineNum);
//            case ';':
//                return new CvaToken(CvaKind.SEMI, lineNum);

            case '\\':
                return handleEscape();

            default:
            {
                // 先看c是否是非前缀字符, 这里是 int, 必须先转成char看在不在表中;
                if (CvaKind.containsKind(String.valueOf(ch)))
                {
                    return new CvaToken(CvaKind.selectReverse(String.valueOf(ch)), lineNum);
                }
                StringBuilder builder = new StringBuilder();
                builder.append(ch);
                while (true)
                {
                    ch = stream.peek();
                    if (ch != LexerConstPool.EOF
                            && !Character.isWhitespace(ch)
                            && !isSpecialCharacter(ch))
                    {
                        builder.append(ch);
                        this.stream.poll();
                    }
                    else
                    {
                        break;
                    }
                }
                String literal = builder.toString();
                if (CvaKind.containsKind(literal))
                {
                    return new CvaToken(CvaKind.selectReverse(literal), lineNum);
                }
                else
                {
                    if (isNumber(literal))
                    {
                        return new CvaToken(CvaKind.NUMBER, lineNum, builder.toString());
                    }
                    else if (isIdentifier(literal))
                    {
                        return new CvaToken(CvaKind.IDENTIFIER, lineNum, builder.toString());
                    }
                    else
                    {
                        System.err.printf("This is an illegal identifier at line %d%n", lineNum);
                        System.exit(1);
                        return null;
                    }
                }
            }
        }
    }

    private static boolean isSpecialCharacter(int c)
    {
        return '+' == c || '&' == c || '=' == c || ',' == c || '.' == c
                || '{' == c || '(' == c || '<' == c || c == '>'
                || '!' == c || c == '[' || c == ']'
                || '}' == c || ')' == c || ';' == c || ':' == c
                || '-' == c || '*' == c || c == LexerConstPool.EOF;
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


    private CvaToken handleEscape()
    {
        return null;
    }
}
