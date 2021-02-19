package cn.misection.cvac.ast.method;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.type.AbstractType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaEntryMethod
 * @Description TODO
 * @CreateTime 2021年02月19日 15:59:00
 */
public final class CvaEntryMethod extends AbstractMethod
{
    private CvaEntryMethod(Builder builder)
    {
        super();
    }

    @Override
    public String name()
    {
        return null;
    }

    @Override
    public AbstractType getRetType()
    {
        return null;
    }

    @Override
    public AbstractExpression getRetExpr()
    {
        return null;
    }

    @Override
    public List<AbstractDeclaration> getArgumentList()
    {
        return null;
    }

    @Override
    public List<AbstractDeclaration> getLocalVarList()
    {
        return null;
    }

    @Override
    public List<AbstractStatement> getStatementList()
    {
        return null;
    }

    public static class Builder
    {
//        private final
    }
}
