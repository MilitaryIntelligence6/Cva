package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaForStatement
 * @Description TODO
 * @CreateTime 2021年02月22日 21:00:00
 */
public final class CvaForStatement extends CvaWhileForStatement
{
    private AbstractExpression forInit;

    private AbstractExpression afterBody;

    private CvaForStatement(int lineNum,
                           AbstractExpression condition,
                           AbstractStatement body,
                           AbstractExpression forInit,
                           AbstractExpression afterBody)
    {
        super(lineNum, condition, body);
        this.forInit = forInit;
        this.afterBody = afterBody;
    }

//    public CvaForStatement(Builder builder)
//    {
//        super(builder.lineNum);
//        this
//    }

    public static class Builder
    {
        private int lineNum;

        private AbstractExpression forInit;

        private AbstractExpression condition;

        private AbstractStatement body;

        private AbstractExpression afterBody;

        public Builder() {}

//        public CvaForStatement build()
//        {
////            return new CvaForStatement(this);
//        }

        public Builder putLineNum(int lineNum)
        {
            this.lineNum = lineNum;
            return this;
        }

        public Builder putForInit(AbstractExpression forInit)
        {
            this.forInit = forInit;
            return this;
        }

        public Builder putCondition(AbstractExpression condition)
        {
            this.condition = condition;
            return this;
        }

        public Builder putBody(AbstractStatement body)
        {
            this.body = body;
            return this;
        }

        public Builder putAfterBody(AbstractExpression afterBody)
        {
            this.afterBody = afterBody;
            return this;
        }
    }
}
