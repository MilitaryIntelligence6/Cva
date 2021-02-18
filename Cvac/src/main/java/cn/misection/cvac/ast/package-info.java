/**
 * @ClassName package-info
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @Description AST;
 * @TODO 用builder建造者来创建这些ast为妙;
 *  必须是纯常量, 所以只有绑定最好不修改, class.getSimple并非常量;
 * @CreateTime 2021年02月15日 21:50:00
 */
package cn.misection.cvac.ast;


final class CvaTypeClassName
{
    /**
     * CvaBoolean;
     */
    public static final String CVA_BOOLEAN_TYPE = "CvaBooleanType";

    /**
     * CvaClassType
     */
    public static final String CVA_CLASS_TYPE = "CvaClassType";

    /**
     * CvaInt
     */
    public static final String CVA_INT_TYPE = "CvaIntType";

    public static final String CVA_STRING_TYPE = "CvaStringType";
}


final class CvaExprClassName
{
    /**
     * CvaAddExpr
     */
    public static final String CVA_ADD_EXPR = "CvaAddExpr";

    /**
     * CvaAndAndExpr
     */
    public static final String CVA_AND_AND_EXPR = "CvaAndAndExpr";

    /**
     * CvaCallExpr
     */
    public static final String CVA_CALL_EXPR = "CvaCallExpr";

    /**
     * CvaIdentifier
     */
    public static final String CVA_IDENTIFIER = "CvaIdentifier";

    /**
     * CvaLTExpr
     */
    public static final String CVA_LESS_THAN_EXPR = "CvaLessThanExpr";

    /**
     * CvaNewExpr
     */
    public static final String CVA_NEW_EXPR = "CvaNewExpr";

    /**
     * CvaNegateExpr
     */
    public static final String CVA_NEGATE_EXPR = "CvaNegateExpr";

    /**
     * CvaSubExpr
     */
    public static final String CVA_SUB_EXPR = "CvaSubExpr";

    /**
     * CvaThisExpr
     */
    public static final String CVA_THIS_EXPR = "CvaThisExpr";

    /**
     * CvaMuliExpr
     */
    public static final String CVA_MULI_EXPR = "CvaMuliExpr";

    /**
     * true
     */
    public static final String CVA_TRUE_EXPR = "CvaTrueExpr";

    /**
     * CvaFalseExpr
     */
    public static final String CVA_FALSE_EXPR = "CvaFalseExpr";

    /**
     * CvaNumberIntExpr
     */
    public static final String CVA_NUMBER_INT_EXPR = "CvaNumberIntExpr";

    /**
     * string;
     */
    public static final String CVA_STRING_EXPR = "CvaStringExpr";

}

final class CvaStatementClassName
{
    /**
     * CvaAssign
     */
    public static final String CVA_ASSIGN = "CvaAssign";

    /**
     * CvaBlock
     */
    public static final String CVA_BLOCK = "CvaBlock";

    /**
     * CvaIfStatement
     */
    public static final String CVA_IF_STATEMENT = "CvaIfStatement";

    /**
     * CvaWriteOperation
     */
    public static final String CVA_WRITE_OPERATION = "CvaWriteOperation";

    /**
     * instanceo
     */
    public static final String CVA_WHILE_STATEMENT = "CvaWhileStatement";
}