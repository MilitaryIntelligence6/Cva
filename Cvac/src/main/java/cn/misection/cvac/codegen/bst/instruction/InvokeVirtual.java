package cn.misection.cvac.codegen.bst.instruction;

import cn.misection.cvac.codegen.bst.btype.ITargetType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName InvokeVirtual
 * @Description TODO
 * @CreateTime 2021年02月16日 00:53:00
 */
public final class InvokeVirtual extends BaseInstructor
{
    /**
     * @FIXME
     */
    private String funcName;

    /**
     * type of first field;
     */
    private String firstFieldType;

    private List<ITargetType> argTypeList;

    private ITargetType retType;

    public InvokeVirtual(String funcName,
                         String firstFieldType,
                         List<ITargetType> argTypeList, 
                         ITargetType retType)
    {
        this.funcName = funcName;
        this.firstFieldType = firstFieldType;
        this.argTypeList = argTypeList;
        this.retType = retType;
    }

    public String getFuncName()
    {
        return funcName;
    }

    public void setFuncName(String funcName)
    {
        this.funcName = funcName;
    }

    public String getFirstFieldType()
    {
        return firstFieldType;
    }

    public void setFirstFieldType(String firstFieldType)
    {
        this.firstFieldType = firstFieldType;
    }

    public List<ITargetType> getArgTypeList()
    {
        return argTypeList;
    }

    public void setArgTypeList(List<ITargetType> argTypeList)
    {
        this.argTypeList = argTypeList;
    }

    public ITargetType getRetType()
    {
        return retType;
    }

    public void setRetType(ITargetType retType)
    {
        this.retType = retType;
    }
}
