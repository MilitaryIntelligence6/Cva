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


final class CvaType
{
    /**
     * CvaBoolean;
     */
    public static final String CVA_BOOLEAN = "CvaBoolean";

    /**
     * CvaClassType
     */
    public static final String CVA_CLASS_TYPE = "CvaClassType";

    /**
     * CvaInt
     */
    public static final String CVA_INT = "CvaInt";
}


final class CvaExpr
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
     * true
     */
    public static final String CVA_TRUE_EXPR = "CvaTrueExpr";

    /**
     * CvaFalseExpr
     */
    public static final String CVA_FALSE_EXPR = "CvaFalseExpr";

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
     * CvaNumberInt
     */
    public static final String CVA_NUMBER_INT = "CvaNumberIntExpr";

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
}

final class CvaStatement
{
    /**
     * CvaAssign
     */
    public static final String CVAASSIGN = "CvaAssign";

    /**
     * CvaBlock
     */
    public static final String CVABLOCK = "CvaBlock";

    /**
     * CvaIfStatement
     */
    public static final String CVAIFSTATEMENT = "CvaIfStatement";

    /**
     * CvaWriteOperation
     */
    public static final String CVAWRITEOPERATION = "CvaWriteOperation";

    /**
     * instanceo
     */
    public static final String CVA_WHILE_STATEMENT = "CvaWhileStatement";
}