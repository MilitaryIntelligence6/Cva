package cn.misection.cvac.ast.expr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:09:00
 */
public enum EnumCvaExpr implements IExpression
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

    CONST_TRUE,

    CONST_FALSE,

    CONST_INT,

    CONST_DOUBLE,

    CONST_STRING,

    IDENTIFIER,

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

    LEFT_SHIFT,

    RIGHT_SHIFT,

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
}
