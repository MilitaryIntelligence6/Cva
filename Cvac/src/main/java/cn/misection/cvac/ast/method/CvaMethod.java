package cn.misection.cvac.ast.method;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.type.AbstractType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMethod
 * @Description TODO
 * @CreateTime 2021年02月14日 20:02:00
 */
public final class CvaMethod extends AbstractMethod
{
    private String literal;

    private AbstractType retType;

    private AbstractExpression retExpr;

    private List<AbstractDeclaration> formalList;

    private List<AbstractDeclaration> localList;

    private List<AbstractStatement> statementList;

    public CvaMethod(String literal,
                     AbstractType retType,
                     AbstractExpression retExpr,
                     List<AbstractDeclaration> formalList,
                     List<AbstractDeclaration> localList,
                     List<AbstractStatement> statementList)
    {
        this.literal = literal;
        this.retType = retType;
        this.retExpr = retExpr;
        this.formalList = formalList;
        this.localList = localList;
        this.statementList = statementList;
    }

    public String getLiteral()
    {
        return literal;
    }

    public AbstractType getRetType()
    {
        return retType;
    }

    public AbstractExpression getRetExpr()
    {
        return retExpr;
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

    public void setRetExpr(AbstractExpression retExpr)
    {
        this.retExpr = retExpr;
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
