package cn.misection.cvac.lexer;

/**
 * Created by Mengxu on 2017/1/6.
 */
public class Token
{
    public enum Kind
    {
        /**
         * +
         */
        ADD,

        /**
         * &&
         */
        AND,

        /**
         * =
         */
        ASSIGN,

        /**
         * boolean
         */
        BOOLEAN,

        /**
         * class
         */
        CLASS,

        /**
         * :
         */
        COLON,

        /**
         * ,
         */
        COMMA,

        /**
         * .
         */
        DOT,

        /**
         * true
         */
        TRUE,

        /**
         * false
         */
        FALSE,

        /**
         * Identifier
         */
        ID,

        /**
         * if
         */
        IF,

        /**
         * else
         */
        ELSE,

        /**
         * int
         */
        INT,

        /**
         * {
         */
        OPEN_CURLY_BRACE,

        /**
         * }
         */
        CLOSE_BRACE,

        /**
         * (
         */
        OPEN_PAREN,

        /**
         * )
         */
        CLOSE_PAREN,

        /**
         * <
         */
        LESS_THAN,

        /**
         * main
         */
        MAIN,

        /**
         * new
         */
        NEW,

        /**
         * !
         */
        NEGATE,

        /**
         * Integer literal
         */
        NUMBER,

        /**
         * print, we just treat it as a key word
         */
        WRITE,

        /**
         * return
         */
        RETURN,

        /**
         * ;
         */
        SEMI,

        /**
         * -
         */
        SUB,

        /**
         * this
         */
        THIS,

        /**
         * *
         */
        STAR,

        /**
         * void
         */
        VOID,

        /**
         * while
         */
        WHILE,

        /**
         * End of file
         */
        EOF,
        ;

    }

    /**
     * the kind of the token
     */
    public Kind kind;

    /**
     * extra lexeme of the token
     */
    public String lexeme;

    /**
     * the line number of the token
     */
    public int lineNum;


    public Token(Kind kind, int lineNum)
    {
        this.kind = kind;
        this.lineNum = lineNum;
    }

    public Token(Kind kind, int lineNum, String lexeme)
    {
        this.kind = kind;
        this.lineNum = lineNum;
        this.lexeme = lexeme;
    }

    @Override
    public String toString()
    {
        return "Token_" + this.kind.toString()
                + (lexeme == null ? "" : " : " + this.lexeme)
                + " : at line " + this.lineNum;
    }
}
