package cn.misection.cvac.ast.method;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.type.ICvaType;
import cn.misection.cvac.constant.EnumLexerCommon;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaEntryMethod
 * @Description 建造者模式构建复杂但参数其实固定的main方法;
 * @CreateTime 2021年02月19日 15:59:00
 */
public final class CvaMainMethod extends AbstractMethod {
    private CvaMainMethod(Builder builder) {
        super();
        this.name = builder.name;
        this.retType = builder.retType;
        // main return后虚拟机停转,没有动作;
        this.retExpr = builder.retExpr;
        this.argumentList = builder.argumentList;
        this.localVarList = builder.localVarList;
        this.statementList = builder.statementList;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ICvaType getRetType() {
        return retType;
    }

    @Override
    public AbstractExpression getRetExpr() {
        return retExpr;
    }

    @Override
    public List<AbstractDeclaration> getArgumentList() {
        return argumentList;
    }

    @Override
    public List<AbstractDeclaration> getLocalVarList() {
        return localVarList;
    }

    @Override
    public List<AbstractStatement> getStatementList() {
        return statementList;
    }

    public static class Builder {
        private final String name = EnumLexerCommon.MAIN_METHOD_NAME.string();

        private ICvaType retType;

        private AbstractExpression retExpr;

        /**
         * @TODO String[] args
         */
        private List<AbstractDeclaration> argumentList;

        private List<AbstractDeclaration> localVarList;

        private List<AbstractStatement> statementList;

        public Builder() {
        }

        /**
         * 类似原型模式的builder;
         *
         * @param prototype 创建原型;
         */
        public Builder(AbstractMethod prototype) {
            this.retType = prototype.retType;
            this.retExpr = prototype.retExpr;
            this.argumentList = prototype.argumentList;
            this.localVarList = prototype.localVarList;
            this.statementList = prototype.statementList;
        }

        public CvaMainMethod build() {
            return new CvaMainMethod(this);
        }

        public Builder putRetType(ICvaType retType) {
            this.retType = retType;
            return this;
        }

        public Builder putRetExpr(AbstractExpression retExpr) {
            this.retExpr = retExpr;
            return this;
        }

        public Builder putMainArgList(List<AbstractDeclaration> argumentList) {
            this.argumentList = argumentList;
            return this;
        }

        public Builder putLocalVarList(List<AbstractDeclaration> localVarList) {
            this.localVarList = localVarList;
            return this;
        }

        public Builder putStatementList(List<AbstractStatement> statementList) {
            this.statementList = statementList;
            return this;
        }
    }
}
