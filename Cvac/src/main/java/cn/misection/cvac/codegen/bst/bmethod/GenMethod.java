package cn.misection.cvac.codegen.bst.bmethod;

import cn.misection.cvac.codegen.bst.bdecl.BaseDeclaration;
import cn.misection.cvac.codegen.bst.statement.BaseStatement;
import cn.misection.cvac.codegen.bst.btype.BaseType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMethod
 * @Description TODO
 * @CreateTime 2021年02月14日 20:02:00
 */
public class GenMethod extends BaseMethod
{
    private String literal;

    private BaseType retType;

    /**
     * @deprecated
     */
//    private AbstractExpression retExpr;

    /**
     * new;
     */
    private String classId;

    private List<BaseDeclaration> formalList;

    private List<BaseDeclaration> localList;

    private List<BaseStatement> statementList;

    private int index;

    private int retExpr;

    /**
     * 顺序很多, 注意!;
     * @param literal
     * @param retType
     * @param classId
     * @param formalList
     * @param localList
     * @param statementList
     * @param retExpr
     * @param index
     */
    public GenMethod(
            String literal,
            BaseType retType,
            String classId,
            List<BaseDeclaration> formalList,
            List<BaseDeclaration> localList,
            List<BaseStatement> statementList,
            int retExpr,
            int index
    )
    {
        this.literal = literal;
        this.retType = retType;
        this.classId = classId;
        this.formalList = formalList;
        this.localList = localList;
        this.statementList = statementList;
        this.retExpr = retExpr;
        this.index = index;
    }

    public String getLiteral()
    {
        return literal;
    }

    public BaseType getRetType()
    {
        return retType;
    }


    public List<BaseDeclaration> getFormalList()
    {
        return formalList;
    }

    public List<BaseDeclaration> getLocalList()
    {
        return localList;
    }

    public List<BaseStatement> getStatementList()
    {
        return statementList;
    }

    public void setLiteral(String literal)
    {
        this.literal = literal;
    }

    public void setRetType(BaseType retType)
    {
        this.retType = retType;
    }

    public void setFormalList(List<BaseDeclaration> formalList)
    {
        this.formalList = formalList;
    }

    public void setLocalList(List<BaseDeclaration> localList)
    {
        this.localList = localList;
    }

    public void setStatementList(List<BaseStatement> statementList)
    {
        this.statementList = statementList;
    }
}
