package cn.misection.cvac.lexer;

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
                return new CvaToken(CvaKind.EOF, lineNum);
            case '+':
                return handlePlus();
            case '-':
                return handleMinus();
            case '*':
                return handleStar();
            case '&':
                return handleAnd();
            case '=':
                return handleEqual();
            case '<':
                return handleLessThan();
            case '>':
                return handleMoreThan();
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

            /// 需要注意转义只应该在字符串中出现!;
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

    private static boolean isSpecialCharacter(char c)
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


    /**
     * @TODO 转义处理;
     * @return
     */
    private CvaToken handleEscape()
    {
        return null;
    }

    private CvaToken handlePlus()
    {
        if (stream.hasNext())
        {
            switch (stream.peek())
            {
                case '+':
                {
                    // 截取两个;
                    stream.poll();
                    return new CvaToken(CvaKind.INCREMENT, lineNum);
                }
                case '=':
                {
                    stream.poll();
                    return new CvaToken(CvaKind.ADD_ASSIGN, lineNum);
                }
                default:
                {
                    break;
                }
            }
        }
        return new CvaToken(CvaKind.ADD, lineNum);
    }

    private CvaToken handleMinus()
    {
        if (stream.hasNext())
        {
            switch (stream.peek())
            {
                case '>':
                {
                    stream.poll();
                    return new CvaToken(CvaKind.ARROW, lineNum);
                }
                case '-':
                {
                    stream.poll();
                    return new CvaToken(CvaKind.DECREMENT, lineNum);
                }
                case '=':
                {
                    stream.poll();
                    return new CvaToken(CvaKind.SUB_ASSIGN, lineNum);
                }
                default:
                {
                    // 说明应该不是以上几种;
                    break;
                }
            }
        }
        return new CvaToken(CvaKind.SUB, lineNum);
    }

    private CvaToken handleStar()
    {
        if (stream.hasNext() && stream.peek() == '=')
        {
            stream.poll();
            return new CvaToken(CvaKind.MULTIPLY_ASSIGN, lineNum);
        }
        return new CvaToken(CvaKind.STAR, lineNum);
    }

    private CvaToken handleEqual()
    {
        if (stream.hasNext() && stream.peek() == '=')
        {
            stream.poll();
            return new CvaToken(CvaKind.EQUALS, lineNum);
        }
        return new CvaToken(CvaKind.ASSIGN, lineNum);
    }

    private CvaToken handleAnd()
    {
        if (stream.hasNext())
        {
            switch (stream.peek())
            {
                case '&':
                {
                    stream.poll();
                    return new CvaToken(CvaKind.AND_AND, lineNum);
                }
                case '=':
                {
                    stream.poll();
                    return new CvaToken(CvaKind.AND_ASSIGN, lineNum);
                }
                default:
                {
                    break;
                }
            }
        }

        return new CvaToken(CvaKind.AND, lineNum);
    }

    private CvaToken handleOr()
    {
        if (stream.hasNext())
        {
            switch (stream.peek())
            {
                case '|':
                {
                    stream.poll();
                    return new CvaToken(CvaKind.OR_OR, lineNum);
                }
                case '=':
                {
                    stream.poll();
                    return new CvaToken(CvaKind.OR_ASSIGN, lineNum);
                }
                default:
                {
                    break;
                }
            }
        }

        return new CvaToken(CvaKind.OR, lineNum);
    }

    private CvaToken handleXOr()
    {
        if (stream.hasNext()
                && stream.peek() == '=')
        {
            stream.poll();
            return new CvaToken(CvaKind.XOR_ASSIGN, lineNum);
        }

        return new CvaToken(CvaKind.XOR, lineNum);
    }

    private CvaToken handleSlash()
    {
        if (stream.hasNext()
                && stream.peek() == '=')
        {
            stream.poll();
            return new CvaToken(CvaKind.DIV_ASSIGN, lineNum);
        }

        return new CvaToken(CvaKind.DIV_OPERATOR, lineNum);
    }

    private CvaToken handlePercent()
    {
        if (stream.hasNext()
                && stream.peek() == '=')
        {
            stream.poll();
            return new CvaToken(CvaKind.REMAINDER_ASSIGN, lineNum);
        }

        return new CvaToken(CvaKind.REMAINDER_OPERATOR, lineNum);
    }

    private CvaToken handleBitNegate()
    {
        if (stream.hasNext()
                && stream.peek() == '=')
        {
            stream.poll();
            return new CvaToken(CvaKind.BIT_NEGATE_OPERATOR, lineNum);
        }

        return new CvaToken(CvaKind.BIT_NEGATE_OPERATOR, lineNum);
    }

    private CvaToken handleMoreThan()
    {
        if (stream.hasNext())
        {
            switch (stream.peek())
            {
                case '=':
                {
                    stream.poll();
                    // TODO 看不懂???;
                    return new CvaToken(CvaKind.MORE_OR_EQUALS, lineNum);
                }
                case '>':
                {
                    if (stream.hasNext(2)
                            && stream.peek(2) == '=')
                    {
                        stream.poll(2);
                        return new CvaToken(CvaKind.RIGHT_SHIFT_ASSIGN, lineNum);
                    }
                    stream.poll();
                    return new CvaToken(CvaKind.RIGHT_SHIFT, lineNum);
                }
                default:
                {
                    return new CvaToken(CvaKind.MORE_THAN, lineNum);
                }
            }
        }
        return new CvaToken(CvaKind.MORE_THAN, lineNum);
    }

    private CvaToken handleLessThan()
    {
        if (stream.hasNext())
        {
            switch (stream.peek())
            {
                case '=':
                {
                    stream.poll();
                    // TODO 看不懂???;
                    return new CvaToken(CvaKind.LESS_OR_EQUALS, lineNum);
                }

                case '<':
                {
                    if (stream.hasNext(2)
                            && stream.peek(2) == '=')
                    {
                        stream.poll(2);
                        return new CvaToken(CvaKind.LEFT_SHIFT_ASSIGN, lineNum);
                    }
                    stream.poll();
                    return new CvaToken(CvaKind.LEFT_SHIFT, lineNum);
                }
                default:
                {
                    // TODO 为啥?;
                    return new CvaToken(CvaKind.LESS_THAN, lineNum);
                }
            }
        }
        return new CvaToken(CvaKind.LESS_THAN, lineNum);
    }

//    private CvaToken handleQuotationMarks()
//    {
//        // 全局 index 不仅仅在循环中;
//        // TODO 转义字符都出现在字符串里, 这里应该处理;
//        readIndex++;
//        int begin = readIndex;
//        while (readIndex < charQueue.length())
//        {
//            if (charQueue.charAt(readIndex) != '"')
//            {
//                literalLength++;
//            }
//            else
//            {
//                break;
//            }
//            readIndex++;
//        }
//        if (readIndex > charQueue.length())
//        {
//            System.err.println("Missing the ending quotation mark!");
//            System.exit(1);
//        }
//        literal = charQueue.substring(begin, literalLength + 1);
//        // 2 是引号长度;
//        pollChar(literalLength + 2);
//        return new CvaToken(CvaKind.STRING, lineNum);
//    }
}
