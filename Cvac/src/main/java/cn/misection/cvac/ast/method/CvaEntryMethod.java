package cn.misection.cvac.ast.method;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.type.AbstractType;
import cn.misection.cvac.lexer.CvaKind;
import sun.reflect.generics.tree.ReturnType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaEntryMethod
 * @Description 建造者模式构建复杂但参数其实固定的main方法;
 * @CreateTime 2021年02月19日 15:59:00
 */
public final class CvaEntryMethod extends AbstractMethod
{
    private CvaEntryMethod(Builder builder)
    {
        super();
        this.name = builder.name;
        this.retType = builder.retType;
        // main return后虚拟机停转,没有动作;
        this.retExpr = null;
        this.argumentList = builder.argumentList;
        this.localVarList = builder.localVarList;
        this.statementList = builder.statementList;
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public AbstractType getRetType()
    {
        return retType;
    }

    @Override
    public AbstractExpression getRetExpr()
    {
        return null;
    }

    @Override
    public List<AbstractDeclaration> getArgumentList()
    {
        return argumentList;
    }

    @Override
    public List<AbstractDeclaration> getLocalVarList()
    {
        return localVarList;
    }

    @Override
    public List<AbstractStatement> getStatementList()
    {
        return statementList;
    }

    public static class Builder
    {
        private final String name = CvaKind.MAIN.getKindLiteral();

        private final AbstractExpression retExpr = null;

        private final List<AbstractDeclaration> argumentList = new ArrayList<>();

        private AbstractType retType;

        private List<AbstractDeclaration> localVarList;

        private List<AbstractStatement> statementList;

        public Builder()
        {

        }

        public Builder(AbstractType retType,
                       List<AbstractDeclaration> localVarList,
                       List<AbstractStatement> statementList)
        {
            this.retType = retType;
            this.localVarList = localVarList;
            this.statementList = statementList;
        }

        public CvaEntryMethod build()
        {
            return new CvaEntryMethod(this);
        }

        public Builder setRetType(AbstractType retType)
        {
            this.retType = retType;
            return this;
        }

        public Builder setLocalVarList(List<AbstractDeclaration> localVarList)
        {
            this.localVarList = localVarList;
            return this;
        }

        public Builder setStatementList(List<AbstractStatement> statementList)
        {
            this.statementList = statementList;
            return this;
        }
    }
}
