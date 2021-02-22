package cn.misection.cvac.lexer;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumCvaToken
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
    REM,

    BIT_AND,

    BIT_OR,

    BIT_XOR,


    /**
     * 新加的求反;
     */
    BIT_NEGATE,

    /**
     * 加了左右;
     */
    LEFT_SHIFT,

    RIGHT_SHIFT,

    UNSIGNED_RIGHT_SHIFT,

    /*
     * 加入的 += -= &= |= ~= >>= <<= !求反
     */
    /**
     * +=;
     */
    ADD_ASSIGN,

    /**
     * -=;
     */
    SUB_ASSIGN,

    MULTIPLY_ASSIGN,

    /**
     * /=
     */
    DIV_ASSIGN,

    REM_ASSIGN,

    /**
     * &=
     */
    BIT_AND_ASSIGN,

    /**
     * |=
     */
    BIT_OR_ASSIGN,

    BIT_XOR_ASSIGN,

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
     * >>>=
     */
    UNSIGNED_RIGHT_SHIFT_ASSIGN,

    /**
     * &&
     */
    AND_AND,

    OR_OR,

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

    /**
     * ;
     */
    SEMI(";"),

    /**
     * :
     * 以后可能有 :: ;
     */
    COLON(":"),

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
     * 数组;
     */
    ARRAY,

    /**
     * class
     */
    CLASS_DECL("class"),

    /**
     * ENUM DEFINE
     */
    ENUM_DECL("enum"),

    STRUCT_DECL("struct"),

    /**
     * 继承符;
     */
    EXTENDS("extends"),

    PACKAGE_DECL("pkg"),

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
    CONST_INT,

    /*
     * 由于暂时没有完善native包机制;
     */
    /**
     * write, we just treat it as a key word
     */
    WRITE("echo"),

    WRITE_LINE("println"),

    WRITE_FORMAT("printf"),

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

    /*
     * ternimal;
     * 终结符, 右值;
     */
    EQUALS,

    /**
     * 新加, *=, /=
     */
    INCREMENT,

    DECREMENT,

    ARROW,

    /**
     * %=
     */
    /**
     * SIGN;
     */

    WHITE_SPACE,

    T_TYPE,

    UNKNOWN_TOKEN,
    // end ternimal;
    ;

    private String kindLiteral;

    EnumCvaToken(String kindLiteral)
    {
        this.kindLiteral = kindLiteral;
    }

    EnumCvaToken() {}

    public String getKindLiteral()
    {
        return kindLiteral;
    }

    private static final Map<String, EnumCvaToken> lookup = new HashMap<>();

    static
    {
        for (EnumCvaToken kind : EnumSet.allOf(EnumCvaToken.class))
        {
            if (kind.kindLiteral != null)
            {
                lookup.put(kind.kindLiteral, kind);
            }
        }
    }

    public static boolean containsKind(String literal)
    {
        return lookup.containsKey(literal);
    }

    public static EnumCvaToken selectReverse(String literal)
    {
        // 可能出null;
        return lookup.get(literal);
    }

    public static boolean isBasicType(EnumCvaToken kind)
    {
        return kind.ordinal() >= EnumCvaToken.VOID.ordinal()
                && kind.ordinal() <= EnumCvaToken.POINTER.ordinal();
    }

    public static boolean isInternalRefType(EnumCvaToken kind)
    {
        return kind == STRING;
    }

    public static boolean isType(EnumCvaToken kind)
    {
        return isBasicType(kind) || isInternalRefType(kind);
    }
}