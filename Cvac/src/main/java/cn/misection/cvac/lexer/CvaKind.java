package cn.misection.cvac.lexer;

public enum CvaKind
{
    /**
     * +
     */
    ADD,

    /**
     * &&
     */
    AND_AND,


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
    IDENTIFIER,

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
    CLOSE_CURLY_BRACE,

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

    /*
     * 新的还未支持的;
     */
    /**
     * end of line;
     */
    EOL,

    PROGRAM,

    EXT_DEFINE_LIST,

    EXT_DEFINE,

    OPT_SPECIFIERS,

    EXT_DECLARATION_LIST,

    EXT_DECLARATION,

    VARIABLE_DECLARATION,

    SPECIFIERS,

    TYPE_OR_CLASS,

    /**
     * 新加的;
     */
//    TYPE_DEF,

    TYPE_NT,

    /*
     * struct;
     */
    /**
     * 专门用来定义结构体;
     */
    STRUCT_SPECIFIER,

    /**
     * struct名字;
     */
    STRUCT_OPT_TAG_NAME,

    /**
     * 结构体内变量列表;
     */
    STRUCT_DEFINE_LIST,

    TAG,

    DEFINE,

    DECLARATION_LIST,

    DECLARATION,

    /**
     * function
     */
    FUNCTION_DECLARATION,

    /**
     * VAR_LIST;
     */
    ARGUMENTS_LIST,

    PARAM_DECLARATION,

    /**
     * 导包语句;
     */
    CALL,

    /**
     * enum;
     */
    ENUM_SPECIFIER,

    OPT_ENUM_LIST,

    ENUMERATOR_LIST,

    ENUMERATOR,

    ENUM_NT,


    /**
     *
     */
    CONST_EXPR,

    ARGUMENTS,

    /**
     *
     */
    COMPOUND_STATEMENT,

    LOCAL_DEFINES,

    STATEMENT_LIST,

    INITIALIZER,

    EXPR,

    NO_COMMA_EXPR,

    /**
     * ?二元运算符;
     */
    BINARY,

    /**
     * 一元运算符;
     */
    UNARY,

    STATEMENT,


    /**
     *
     */
    TEST,

    OPT_EXPR,

    END_OPT_EXPR,

    TARGET,

    ELSE_STATEMENT,

    IF_STATEMENT,

    IF_ELSE_STATEMENT,

    /**
     *
     */
    TYPE_SPECIFIER,

    /**
     * 结构体尾部别名;
     */
    STRUCT_NEW_NAME,

    IDENTIFIER_NT,


    /*
     * ternimal;
     * 终结符, 右值;
     */

    TYPE,

    STRUCT,

    OPEN_PARENTHESES,

    CLOSE_PARENTHESES,

    /**
     * 左右括号?;
     */
    OPEN_BRACKETS,

    CLOSE_BRACKETS,

    OPEN_CURLY_BRACES,

    CLOSE_CURLY_BRACES,

    STRING,

    QUEST,

    RELOP_FACT_EQUALS,

    /**
     * 新加的;
     */
    OR_OR,

    AND,

    OR,

    XOR,

    EQUALS_OPERATOR,

    /**
     * 新加的求反;
     */
    BIT_NEGATE_OPERATOR,

    /**
     * 加了左右;
     */
    LEFT_SHIFT_OPERATOR,

    RIGHT_SHIFT_OPERATOR,

    DIV_OPERATOR,

    /**
     * 加的求余;
     */
    REMAINDER_OPERATOR,

    PLUS_OPERATOR,

    MINUS_OPERATOR,

    /*
     * 乘法, 原来无, 由于要和指针混淆, 所以暂时难以处理;
     */
//    MULTIPLY_OPERATOR,

    /**
     * 新加, *=, /=
     */
    MULTIPLY_ASSIGN,

    /**
     * /=
     */
    DIV_ASSIGN,

    INCREMENT_OPERATOR,

    DECREMENT_OPERATOR,

    STRUCT_OPERATOR,

    /*
     * 加入的 += -= &= |= ~= >>= <<= !求反
     */
    /**
     * !;
     */
    NEGATE_OPERATOR,

    /**
     * +=;
     */
    PLUS_ASSIGN,

    /**
     * -=;
     */
    MINUS_ASSIGN,

    /**
     * &=
     */
    AND_ASSIGN,

    /**
     * |=
     */
    OR_ASSIGN,

    XOR_ASSIGN,

    /**
     * ~=, ~ =
     */
    BIT_NEGATE_ASSIGN,

    /**
     * >>=
     */
    LEFT_SHIFT_ASSIGN,

    /**
     * <<=
     */
    RIGHT_SHIFT_ASSIGN,

    /**
     * %=
     */
    REMAINDER_ASSIGN,

    SWITCH,

    CASE,

    DEFAULT,

    BREAK,

    FOR,

    DO,

    CONTINUE,

    GOTO,

    /**
     * ENUM DEFINE
     */
    ENUM,

    /**
     * SIGN;
     */

    SEMICOLON,

    WHITE_SPACE,

    EQUALS_FACT_ASSIGN,

    T_TYPE,

    UNKNOWN_TOKEN,
    // end ternimal;

    ;
}