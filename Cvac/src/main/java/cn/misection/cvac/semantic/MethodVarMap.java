package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.type.ICvaType;

import java.util.HashMap;
import java.util.List;

/**
 * @author  MI6 root
 */
public final class MethodVarMap
        extends HashMap<String, ICvaType>
{
    public MethodVarMap()
    {
        super();
    }

    /**
     * 暴露给用户, 防止搞忘;
     * @param formalList 方法形参;
     * @param localVarList 方法本地变量;
     */
    public void putVarList(List<AbstractDeclaration> formalList,
                           List<AbstractDeclaration> localVarList)
    {
        putMulList(formalList, localVarList);
    }

    /**
     * 安全的, 因为不暴露给用户, 其实final都不需要, private是隐式final;
     * @param varListArray varLists;
     */
    @SafeVarargs
    private final void putMulList(List<AbstractDeclaration>... varListArray)
    {
        for (List<AbstractDeclaration> varList : varListArray)
        {
            putSingleList(varList);
        }
    }

    private void putSingleList(List<AbstractDeclaration> declList)
    {
        for (AbstractDeclaration decl : declList)
        {
            // FIXME 不知道有无隐患!;
            if (this.containsKey(decl.name()))
            {
                System.err.printf("duplicated parameter: %s at line %d%n",
                        decl.name(), decl.getLineNum());
                System.exit(1);
            }
            else
            {
                this.put(decl.name(), decl.type());
            }
        }
    }
}
