package cn.misection.cvac.lexer;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CvaKind
{
    /**
     * +
     */
    ADD,

    /**
     * -
     */
    SUB,

    /**
     * *
     */
    STAR,

    DIV,

    /**
     * 加的求余;
     */
    REMAINDER,

    /**
     * ;
     */
    SEMI(";"),

    /**
     * :
     * 以后可能有 :: ;
     */
    COLON(":"),

    /**
     * 新加的;
     */
    OR_OR,

    AND,

    OR,

    XOR,

    /**
     * &&
     */
    AND_AND,

    /**
     * =
     * 是前缀字符, 不能初始化之;
     */
    ASSIGN,

    /**
     * <
     * 多元符号, 不能初始化;
     */
    LESS_THAN,

    /**
     * >
     */
    MORE_THAN,

    /**
     * <=
     */
    LESS_OR_EQUALS,

    /**
     * >=
     */
    MORE_OR_EQUALS,

    QUEST("?"),
    /**
     * ,
     */
    COMMA(","),

    /**
     * .
     */
    DOT("."),

    /**
     * true
     */
    TRUE("true"),

    /**
     * false
     */
    FALSE("false"),

    /**
     * if
     */
    IF_STATEMENT("if"),

    /**
     * else
     */
    ELSE_STATEMENT("else"),

    STATIC("static"),

    NULL("null"),

    /**
     * void
     */
    VOID("void"),

    /**
     * byte;
     */
    BYTE("byte"),

    /**
     * short;
     */
    SHORT("short"),

    /**
     * char
     */
    CHAR("char"),

    /**
     * int
     */
    INT("int"),

    /**
     * long;
     */
    LONG("long"),

    FLOAT("float"),

    DOUBLE("double"),

    /**
     * boolean
     */
    BOOLEAN("boolean"),

    /**
     * 指针;
     */
    POINTER("pointer"),

    /**
     * String 不是基本类型, 但应是CvaDK内置类, 暂时不标记为class只有;
     */
    STRING("string"),

    /**
     * class
     */
    CLASS("class"),

    /**
     * ENUM DEFINE
     */
    ENUM("enum"),

    STRUCT("struct"),

    /**
     * 继承符;
     */
    EXTENDS("extends"),

    PACKAGE("pkg"),

    /*
     * non-prefix char;
     */

    /**
     * {
     */
    OPEN_CURLY_BRACE("{"),

    /**
     * }
     */
    CLOSE_CURLY_BRACE("}"),

    /**
     * 左右括号;
     */
    OPEN_BRACKETS("["),

    CLOSE_BRACKETS("]"),

    /**
     * (
     */
    OPEN_PAREN("("),

    /**
     * )
     */
    CLOSE_PAREN(")"),


    /**
     * main
     */
    MAIN("main"),

    /**
     * new
     */
    NEW("new"),

    /**
     * !
     */
    NEGATE("!"),

    /**
     * return
     */
    RETURN("return"),

    /**
     * call;
     */
    CALL("call"),

    /**
     * this
     */
    THIS("this"),

    FOR_STATEMENT("for"),

    /**
     * while
     */
    WHILE_STATEMENT("while"),

    SWITCH("switch"),

    CASE("switch"),

    DEFAULT("default"),

    BREAK("break"),

    DO("do"),

    CONTINUE("continue"),

    GOTO("goto"),

    TYPE_DEF("typedef"),

//    SHARP_DEFINE("#define"),

    /**
     * Integer literal
     */
    NUMBER,

    /*
     * 由于暂时没有完善native包机制;
     */
    /**
     * write, we just treat it as a key word
     */
    WRITE("echo"),
//
//    WRITE_LINE("println"),
//
//    WRITE_F("printf"),

    /**
     * Identifier
     */
    IDENTIFIER,

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

    EQUALS,

    /**
     * 新加的求反;
     */
    BIT_NEGATE,

    /**
     * 加了左右;
     */
    LEFT_SHIFT,

    RIGHT_SHIFT,


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

    INCREMENT,

    DECREMENT,

    ARROW,

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
    ADD_ASSIGN,

    /**
     * -=;
     */
    SUB_ASSIGN,

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

    /**
     * SIGN;
     */

    WHITE_SPACE,

    T_TYPE,

    UNKNOWN_TOKEN,
    // end ternimal;
    ;

    private String kindLiteral;

    CvaKind(String kindLiteral)
    {
        this.kindLiteral = kindLiteral;
    }

    CvaKind()
    {

    }

    public String getKindLiteral()
    {
        return kindLiteral;
    }

    private static final Map<String, CvaKind> lookup = new HashMap<>();

    static
    {
        for (CvaKind kind : EnumSet.allOf(CvaKind.class))
        {
            if (kind.kindLiteral != null)
            {
                lookup.put(kind.kindLiteral, kind);
            }
        }

        /**
         * 除了默认之外, 还有一些本来多义词;
         * @TODO 后面应该将他们分离;
         */
        // printf 已经写入了;
        lookup.put("printf", CvaKind.WRITE);
//        lookup.put("echof", CvaKind.WRITE);
//        lookup.put("echoln", CvaKind.WRITE);
        lookup.put("println", CvaKind.WRITE);
    }

    public static boolean containsKind(String literal)
    {
        return lookup.containsKey(literal);
    }

    public static CvaKind selectReverse(String literal)
    {
        // 可能出null;
        return lookup.get(literal);
    }

    public static boolean isBasicType(CvaKind kind)
    {
        return kind.ordinal() >= CvaKind.VOID.ordinal()
                && kind.ordinal() <= CvaKind.POINTER.ordinal();
    }

    public static boolean isReferenceType(CvaKind kind)
    {
        return kind.ordinal() >= STRING.ordinal()
                && kind.ordinal() <= STRUCT.ordinal();
    }

    public static boolean isType(CvaKind kind)
    {
        return isBasicType(kind) || isReferenceType(kind);
    }
}