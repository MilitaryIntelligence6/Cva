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