package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.type.AbstractType;

import java.util.HashMap;
import java.util.List;

/**
 * @author  MI6 root
 */
public final class MethodVariableTable
        extends HashMap<String, AbstractType>
{
    public MethodVariableTable()
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
            if (this.get(decl.literal()) != null)
            {
                System.err.printf("duplicated parameter: %s at line %d%n",
                        decl.literal(), decl.getLineNum());
                System.exit(1);
            }
            else
            {
                this.put(decl.literal(), decl.type());
            }
        }
    }
}
