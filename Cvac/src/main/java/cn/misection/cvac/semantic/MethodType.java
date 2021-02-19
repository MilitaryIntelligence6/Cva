package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.type.AbstractType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MI6 root 1/13.
 */
public final class MethodType
{
    private AbstractType retType;
    private List<AbstractDeclaration> argsType;

    public MethodType(AbstractType retType,
                      List<AbstractDeclaration> argsType)
    {
        this.setRetType(retType);
        this.setArgsType(argsType);
    }

    public AbstractType getRetType()
    {
        return retType;
    }

    public void setRetType(AbstractType retType)
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
