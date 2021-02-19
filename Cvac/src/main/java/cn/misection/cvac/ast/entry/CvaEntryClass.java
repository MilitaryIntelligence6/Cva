package cn.misection.cvac.ast.entry;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.method.AbstractMethod;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.constant.LexerConst;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaEntry
 * @Description TODO
 * @CreateTime 2021年02月14日 18:27:00
 */
public final class CvaEntryClass extends AbstractEntryClass
{
    private CvaEntryClass(Builder builder)
    {
        this.name = builder.name;
        this.mainMethod = builder.entryMethod;
    }
    /**
     * @deprecated
     * @param name
     * @param statement
     */
    public CvaEntryClass(String name, AbstractStatement statement)
    {
        super(name, statement);
    }

    @Override
    public String parent()
    {
        // TODO java.lang.Object;
        return null;
    }

    @Override
    public List<AbstractDeclaration> getFieldList()
    {
        return null;
    }

    @Override
    public List<AbstractMethod> getMethodList()
    {
        return null;
    }

    public static class Builder
    {
        private String name = LexerConst.DEFAULT_MAIN_CLASS_NAME;

        private AbstractMethod entryMethod;

        public Builder()
        {

        }

        public Builder(AbstractMethod entryMethod)
        {
            this.entryMethod = entryMethod;
        }

        public CvaEntryClass build()
        {
            return new CvaEntryClass(this);
        }

        public Builder putName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder putEntryMethod(AbstractMethod entryMethod)
        {
            this.entryMethod = entryMethod;
            return this;
        }
    }
}
