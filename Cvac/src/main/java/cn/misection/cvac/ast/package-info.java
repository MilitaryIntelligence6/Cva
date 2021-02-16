/**
 * @ClassName package-info
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @Description AST;
 * @TODO 用builder建造者来创建这些ast为妙;
 * @CreateTime 2021年02月15日 21:50:00
 */
package cn.misection.cvac.ast;


final class CvaType
{
    /**
     * CvaBoolean
     */
    public static final String CVABOOLEAN = "CvaBoolean";

    /**
     * CvaClassType
     */
    public static final String CVACLASSTYPE = "CvaClassType";

    /**
     * CvaInt
     */
    public static final String CVAINT = "CvaInt";
}


final class CvaExpr
{
    /**
     * CvaAddExpr
     */
    public static final String CVAADDEXPR = "CvaAddExpr";

    /**
     * CvaAndAndExpr
     */
    public static final String CVAANDANDEXPR = "CvaAndAndExpr";

    /**
     * CvaCallExpr
     */
    public static final String CVACALLEXPR = "CvaCallExpr";

    /**
     * CvaFalseExpr
     */
    public static final String CVAFALSEEXPR = "CvaFalseExpr";

    /**
     * CvaIdentifier
     */
    public static final String CVAIDENTIFIER = "CvaIdentifier";

    /**
     * CvaLTExpr
     */
    public static final String CVALTEXPR = "CvaLTExpr";

    /**
     * CvaNewExpr
     */
    public static final String CVANEWEXPR = "CvaNewExpr";

    /**
     * CvaNegateExpr
     */
    public static final String CVANEGATEEXPR = "CvaNegateExpr";

    /**
     * CvaNumberInt
     */
    public static final String CVANUMBERINT = "CvaNumberInt";

    /**
     * CvaSubExpr
     */
    public static final String CVASUBEXPR = "CvaSubExpr";

    /**
     * CvaThisExpr
     */
    public static final String CVATHISEXPR = "CvaThisExpr";

    /**
     * CvaMuliExpr
     */
    public static final String CVAMULIEXPR = "CvaMuliExpr";

    /**
     * true
     */
    public static final String CVATRUEEXPR = "CvaTrue";

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