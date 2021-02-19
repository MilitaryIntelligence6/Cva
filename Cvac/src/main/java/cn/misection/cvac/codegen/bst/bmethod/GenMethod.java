package cn.misection.cvac.codegen.bst.bmethod;

import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.instruction.BaseInstruction;
import cn.misection.cvac.codegen.bst.btype.BaseType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMethod
 * @Description TODO
 * @CreateTime 2021年02月14日 20:02:00
 */
public final class GenMethod extends BaseMethod
{
    private String name;

    private BaseType retType;

    /**
     * deprecated
     *  只是该属性在后端不被需要;
     */
//    private AbstractExpression retExpr;

    /**
     * new;
     */
    private String className;

    private List<GenDeclaration> formalList;

    private List<GenDeclaration> localList;

    private List<BaseInstruction> statementList;

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
    public GenMethod(
            String name,
            BaseType retType,
            String className,
            List<GenDeclaration> formalList,
            List<GenDeclaration> localList,
            List<BaseInstruction> statementList,
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

    public BaseType getRetType()
    {
        return retType;
    }


    public List<BaseInstruction> getStatementList()
    {
        return statementList;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setRetType(BaseType retType)
    {
        this.retType = retType;
    }

    public void setStatementList(List<BaseInstruction> statementList)
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

    public List<GenDeclaration> getFormalList()
    {
        return formalList;
    }

    public void setFormalList(List<GenDeclaration> formalList)
    {
        this.formalList = formalList;
    }

    public List<GenDeclaration> getLocalList()
    {
        return localList;
    }

    public void setLocalList(List<GenDeclaration> localList)
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
