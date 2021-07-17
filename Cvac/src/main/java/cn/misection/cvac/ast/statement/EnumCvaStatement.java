package cn.misection.cvac.ast.statement;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumCvaStatement
 * @Description TODO
 * @CreateTime 2021年02月21日 10:11:00
 */
public enum EnumCvaStatement {
    /**
     * statement;
     */
    NULL_POINTER,

    ASSIGN,

    BLOCK,

    IF,

    WHILE_FOR,

    WRITE,

    EXPR_STATEMENT,

    /**
     * 包含声明的statement;
     */
    DECL_STATEMENT,
    ;
}
