package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaWhileStatement
 * @Description TODO
 * @CreateTime 2021年02月14日 18:47:00
 */
public class CvaWhileForStatement extends AbstractStatement
{
    private AbstractStatement forInit;

    private AbstractExpression condition;

    private AbstractStatement body;

    private AbstractStatement afterBody;


    public CvaWhileForStatement(int lineNum,
                                AbstractExpression condition,
                                AbstractStatement body)
    {
        super(lineNum);
        this.condition = condition;
        this.body = body;
        initWhile();
    }

    public CvaWhileForStatement(Builder builder)
    {
        super(builder.lineNum);
        this.forInit = builder.forInit;
        this.condition = builder.condition;
        this.body = builder.body;
        this.afterBody = builder.afterBody;
    }

    private void initWhile()
    {
        this.forInit = CvaNullStatement.getInstance();
        this.afterBody = CvaNullStatement.getInstance();
    }

    @Override
    public EnumCvaStatement toEnum()
    {
        return EnumCvaStatement.WHILE_FOR;
    }

    public AbstractStatement getForInit()
    {
        return forInit;
    }

    public AbstractExpression getCondition()
    {
        return condition;
    }

    public AbstractStatement getBody()
    {
        return body;
    }

    public AbstractStatement getAfterBody()
    {
        return afterBody;
    }

    public void setCondition(AbstractExpression condition)
    {
        this.condition = condition;
    }

    public void setBody(AbstractStatement body)
    {
        this.body = body;
    }

    public static class Builder
    {
        private int lineNum;

        /**
         * 因为是for循环的, 先做成null, 是for再添加;
         */
        private AbstractStatement forInit = CvaNullStatement.getInstance();

        private AbstractExpression condition;

        private AbstractStatement body;

        private AbstractStatement afterBody = CvaNullStatement.getInstance();

        public Builder() {}

        public CvaWhileForStatement build()
        {
            return new CvaWhileForStatement(this);
        }

        public Builder putLineNum(int lineNum)
        {
            this.lineNum = lineNum;
            return this;
        }

        public Builder putForInit(AbstractStatement forInit)
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

        public Builder putAfterBody(AbstractStatement afterBody)
        {
            this.afterBody = afterBody;
            return this;
        }
    }
}
