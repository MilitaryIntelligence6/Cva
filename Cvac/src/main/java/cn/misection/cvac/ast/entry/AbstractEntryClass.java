package cn.misection.cvac.ast.entry;

import cn.misection.cvac.ast.clas.AbstractCvaClass;
import cn.misection.cvac.ast.method.AbstractMethod;
import cn.misection.cvac.ast.method.CvaMethod;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.codegen.bst.bmethod.BaseMethod;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMain
 * @Description main 方法入口, 由于Cva可能没有精力去实现static关键字了
 * 也为了能将实例和static类完全解耦, 所以EntryClass非常特殊只容许衣柜main方法
 * 更推荐直接在类外定义main方法;
 * 所以Cva的main class是一个阉割的class;
 * @TODO main方法要传入命令行参数!
 * @TODO main方法要支持多条statement;
 *
 * @CreateTime 2021年02月14日 17:54:00
 */
public abstract class AbstractEntryClass extends AbstractCvaClass
{
    protected String name;

    protected AbstractMethod entryMethod;

    public AbstractEntryClass()
    {
        super();
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

    @Override
    public String name()
    {
        return name;
    }

    public AbstractStatement getStatement()
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
