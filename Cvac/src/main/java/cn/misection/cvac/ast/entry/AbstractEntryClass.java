package cn.misection.cvac.ast.entry;

import cn.misection.cvac.ast.clas.AbstractCvaClass;
import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.method.AbstractMethod;
import cn.misection.cvac.ast.statement.AbstractStatement;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMain
 * @Description main 方法入口;
 * @TODO main方法要传入命令行参数!
 * @TODO main方法要支持多条statement;
 * @CreateTime 2021年02月14日 17:54:00
 */
public abstract class AbstractEntryClass extends AbstractCvaClass
{
    protected String name;

    protected List<AbstractStatement> statementList;

    public AbstractEntryClass(String name,
                              String parent,
                              List<AbstractDeclaration> fieldList,
                              List<AbstractMethod> methodList)
    {
        super(name, parent, fieldList, methodList);
    }


    /**
     * @deprecated
     */
    private AbstractStatement statement = null;

    /**
     * @deprecated
     * @param name
     * @param statement
     */
    public AbstractEntryClass(String name,
                              AbstractStatement statement)
    {
        this.name = name;
        this.statement = statement;
    }

    /**
     * @TODO
     * @param name
     * @param statementList
     */
    protected AbstractEntryClass(String name,
                                 List<AbstractStatement> statementList)
    {
        this.name = name;
        this.statementList = statementList;
    }

    @Override
    public String name()
    {
        return name;
    }

    public AbstractStatement statement()
    {
        return statement;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setStatement(AbstractStatement statement)
    {
        this.statement = statement;
    }
}
