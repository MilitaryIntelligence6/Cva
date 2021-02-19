package cn.misection.cvac.ast.entry;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.method.AbstractMethod;
import cn.misection.cvac.ast.statement.AbstractStatement;

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

    }
}
