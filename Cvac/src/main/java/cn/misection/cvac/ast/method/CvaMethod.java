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
    private String name;

    private AbstractType retType;

    private AbstractExpression retExpr;

    private List<AbstractDeclaration> argumentList;

    private List<AbstractDeclaration> localList;

    private List<AbstractStatement> statementList;

    public CvaMethod(String name,
                     AbstractType retType,
                     AbstractExpression retExpr,
                     List<AbstractDeclaration> argumentList,
                     List<AbstractDeclaration> localList,
                     List<AbstractStatement> statementList)
    {
        this.name = name;
        this.retType = retType;
        this.retExpr = retExpr;
        this.argumentList = argumentList;
        this.localList = localList;
        this.statementList = statementList;
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public AbstractType retType()
    {
        return retType;
    }

    @Override
    public AbstractExpression retExpr()
    {
        return retExpr;
    }

    @Override
    public List<AbstractDeclaration> argumentList()
    {
        return argumentList;
    }

    @Override
    public List<AbstractDeclaration> localList()
    {
        return localList;
    }

    @Override
    public List<AbstractStatement> statementList()
    {
        return statementList;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setRetType(AbstractType retType)
    {
        this.retType = retType;
    }

    public void setRetExpr(AbstractExpression retExpr)
    {
        this.retExpr = retExpr;
    }

    public void setArgumentList(List<AbstractDeclaration> argumentList)
    {
        this.argumentList = argumentList;
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
