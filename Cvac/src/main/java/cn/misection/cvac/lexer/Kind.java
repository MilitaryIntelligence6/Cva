package cn.misection.cvac.lexer;

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