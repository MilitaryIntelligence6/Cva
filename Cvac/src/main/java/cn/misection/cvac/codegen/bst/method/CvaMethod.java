package cn.misection.cvac.codegen.bst.method;

import cn.misection.cvac.codegen.bst.decl.AbstractDeclaration;
import cn.misection.cvac.codegen.bst.statement.AbstractStatement;
import cn.misection.cvac.codegen.bst.type.AbstractType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMethod
 * @Description TODO
 * @CreateTime 2021年02月14日 20:02:00
 */
public class CvaMethod extends AbstractMethod
{
    private String literal;

    private AbstractType retType;

    /**
     * @deprecated
     */
//    private AbstractExpression retExpr;

    /**
     * new;
     */
    private String classId;

    private List<AbstractDeclaration> formalList;

    private List<AbstractDeclaration> localList;

    private List<AbstractStatement> statementList;

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
    public CvaMethod(
            String literal,
            AbstractType retType,
            String classId,
            List<AbstractDeclaration> formalList,
            List<AbstractDeclaration> localList,
            List<AbstractStatement> statementList,
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

    public AbstractType getRetType()
    {
        return retType;
    }


    public List<AbstractDeclaration> getFormalList()
    {
        return formalList;
    }

    public List<AbstractDeclaration> getLocalList()
    {
        return localList;
    }

    public List<AbstractStatement> getStatementList()
    {
        return statementList;
    }

    public void setLiteral(String literal)
    {
        this.literal = literal;
    }

    public void setRetType(AbstractType retType)
    {
        this.retType = retType;
    }

    public void setFormalList(List<AbstractDeclaration> formalList)
    {
        this.formalList = formalList;
    }

    public void setLocalList(List<AbstractDeclaration> localList)
    {
        this.localList = localList;
    }

    public void setStatementList(List<AbstractStatement> statementList)
    {
        this.statementList = statementList;
    }
}
