package cn.misection.cvac.ast.expr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:09:00
 */
public enum EnumCvaExpr
{
    /**
     * 单目;
     */
    CALL,

    NEGATE,

    BIT_NEGATE,

    /**
     * 应该是包括true false const int等等的;
     */
    CONST,

    CONST_NULL,

    CONST_TRUE,

    CONST_FALSE,

    CONST_INT,

    CONST_DOUBLE,

    CONST_STRING,

    IDENTIFIER,

    INCREMENT,

    DECREMENT,

    NEW,

    THIS,

    /*
     * end 单目;
     */

    /**
     * 双目;
     */
    ADD,

    SUB,

    MUL,

    DIV,

    REMAINDER,

    BIT_AND,

    BIT_OR,

    BIT_XOR,

    LEFT_SHIFT,

    RIGHT_SHIFT,

    UNSIGNED_RIGHT_SHIFT,

    XOR,

    LESS_THAN,

    MORE_THAN,

    AND_AND,

    OR_OR,
    /*
     * end双目;
     */

    /**
     * 三目;
     */
    /*
     * end 三目;
     */
    CONDITION_CALC,
    ;

    public static boolean isUnary(EnumCvaExpr expr)
    {
        return expr.ordinal() <= THIS.ordinal();
    }

    public static boolean isBinary(EnumCvaExpr expr)
    {
        return expr.ordinal() >= ADD.ordinal()
                && expr.ordinal() <= OR_OR.ordinal();
    }

    public static boolean isTernary(EnumCvaExpr expr)
    {
        return expr == CONDITION_CALC;
    }
}
