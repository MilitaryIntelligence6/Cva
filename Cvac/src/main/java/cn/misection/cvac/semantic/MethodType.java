package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.type.ICvaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MI6 root
 */
public final class MethodType
{
    private ICvaType retType;
    private List<AbstractDeclaration> argsType;

    public MethodType(ICvaType retType,
                      List<AbstractDeclaration> argsType)
    {
        this.setRetType(retType);
        this.setArgsType(argsType);
    }

    public ICvaType getRetType()
    {
        return retType;
    }

    public void setRetType(ICvaType retType)
    {
        this.retType = retType;
    }

    public List<AbstractDeclaration> getArgsType()
    {
        return argsType;
    }

    public void setArgsType(List<AbstractDeclaration> argsType)
    {
        this.argsType = argsType;
    }
}
