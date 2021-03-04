package cn.misection.cvac.parser;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.decl.nullobj.CvaNullDecl;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.statement.EnumCvaStatement;
import cn.misection.cvac.ast.statement.nullobj.CvaNullStatement;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDeclStatement
 * @Description 纯粹的工具类, 生命周期只在Parser中;
 * @CreateTime 2021年02月23日 19:52:00
 */
public final class CvaDeclStatement extends AbstractStatement
{
    private AbstractDeclaration decl;

    private AbstractStatement assign;

    private CvaDeclStatement(Builder builder)
    {
        super(builder.lineNum);
        this.decl = builder.decl;
        this.assign = builder.assign;
    }

    public AbstractDeclaration getDecl()
    {
        return decl;
    }

    public AbstractStatement getAssign()
    {
        return assign;
    }

    @Override
    public EnumCvaStatement toEnum()
    {
        return EnumCvaStatement.DECL_STATEMENT;
    }

    public static class Builder
    {
        private int lineNum;

        private AbstractDeclaration decl = CvaNullDecl.getInstance();

        private AbstractStatement assign = CvaNullStatement.getInstance();

        public Builder()
        {
        }

        public CvaDeclStatement build()
        {
            return new CvaDeclStatement(this);
        }

        public Builder putLineNum(int lineNum)
        {
            this.lineNum = lineNum;
            return this;
        }

        public Builder putDecl(AbstractDeclaration decl)
        {
            this.decl = decl;
            return this;
        }

        public Builder putAssign(AbstractStatement assign)
        {
            this.assign = assign;
            return this;
        }
    }
}
