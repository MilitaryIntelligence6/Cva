package cn.misection.cvac.codegen.bst.bmethod;

import cn.misection.cvac.codegen.bst.bdecl.TargetDeclaration;
import cn.misection.cvac.codegen.bst.instruction.IInstructor;
import cn.misection.cvac.codegen.bst.btype.ITargetType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMethod
 * @Description TODO
 * @CreateTime 2021年02月14日 20:02:00
 */
public final class TargetMethod extends BaseMethod
{
    private String name;

    private ITargetType retType;

    /**
     * deprecated
     *  只是该属性在后端不被需要;
     */
//    private AbstractExpression retExpr;

    /**
     * new;
     */
    private String className;

    private List<TargetDeclaration> formalList;

    private List<TargetDeclaration> localList;

    private List<IInstructor> statementList;

    private int index;

    private int retExpr;

    /**
     * 顺序很多, 注意!;
     * @param name
     * @param retType
     * @param className
     * @param formalList
     * @param localList
     * @param statementList
     * @param retExpr
     * @param index
     */
    public TargetMethod(
            String name,
            ITargetType retType,
            String className,
            List<TargetDeclaration> formalList,
            List<TargetDeclaration> localList,
            List<IInstructor> statementList,
            int retExpr,
            int index
    )
    {
        this.name = name;
        this.retType = retType;
        this.className = className;
        this.formalList = formalList;
        this.localList = localList;
        this.statementList = statementList;
        this.retExpr = retExpr;
        this.index = index;
    }

    public String getName()
    {
        return name;
    }

    public ITargetType getRetType()
    {
        return retType;
    }


    public List<IInstructor> getStatementList()
    {
        return statementList;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setRetType(ITargetType retType)
    {
        this.retType = retType;
    }

    public void setStatementList(List<IInstructor> statementList)
    {
        this.statementList = statementList;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public List<TargetDeclaration> getFormalList()
    {
        return formalList;
    }

    public void setFormalList(List<TargetDeclaration> formalList)
    {
        this.formalList = formalList;
    }

    public List<TargetDeclaration> getLocalList()
    {
        return localList;
    }

    public void setLocalList(List<TargetDeclaration> localList)
    {
        this.localList = localList;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getRetExpr()
    {
        return retExpr;
    }

    public void setRetExpr(int retExpr)
    {
        this.retExpr = retExpr;
    }
}
