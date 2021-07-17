package cn.misection.cvac.ast.method;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.type.ICvaType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMethod
 * @Description TODO
 * @CreateTime 2021年02月14日 20:02:00
 */
public final class CvaMethod extends AbstractMethod {
    private CvaMethod(String name,
                      ICvaType retType,
                      AbstractExpression retExpr,
                      List<AbstractDeclaration> argumentList,
                      List<AbstractDeclaration> localVarList,
                      List<AbstractStatement> statementList) {
        super(name,
                retType,
                retExpr,
                argumentList,
                localVarList,
                statementList);
    }

    public CvaMethod(Builder builder) {
        this.name = builder.name;
        this.retType = builder.retType;
        this.retExpr = builder.retExpr;
        this.argumentList = builder.argumentList;
        this.localVarList = builder.localVarList;
        this.statementList = builder.statementList;
    }

    public static class Builder {
        private String name;

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

        public CvaMethod build() {
            return new CvaMethod(this);
        }

        public Builder putName(String name) {
            this.name = name;
            return this;
        }

        public Builder putRetType(ICvaType retType) {
            this.retType = retType;
            return this;
        }

        public Builder putRetExpr(AbstractExpression retExpr) {
            this.retExpr = retExpr;
            return this;
        }

        public Builder putArgList(List<AbstractDeclaration> argumentList) {
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
