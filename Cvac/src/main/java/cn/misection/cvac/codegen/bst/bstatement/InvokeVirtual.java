package cn.misection.cvac.codegen.bst.bstatement;

import cn.misection.cvac.codegen.bst.btype.BaseType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName InvokeVirtual
 * @Description TODO
 * @CreateTime 2021年02月16日 00:53:00
 */
public class InvokeVirtual extends BaseStatement
{
    /**
     * @FIXME
     */
    private String f;

    private String c;

    private List<BaseType> argTypeList;

    private BaseType retType;

    public InvokeVirtual(String f, String c, List<BaseType> argTypeList, BaseType retType)
    {
        this.f = f;
        this.c = c;
        this.argTypeList = argTypeList;
        this.retType = retType;
    }

    public String getF()
    {
        return f;
    }

    public void setF(String f)
    {
        this.f = f;
    }

    public String getC()
    {
        return c;
    }

    public void setC(String c)
    {
        this.c = c;
    }

    public List<BaseType> getArgTypeList()
    {
        return argTypeList;
    }

    public void setArgTypeList(List<BaseType> argTypeList)
    {
        this.argTypeList = argTypeList;
    }

    public BaseType getRetType()
    {
        return retType;
    }

    public void setRetType(BaseType retType)
    {
        this.retType = retType;
    }
}
