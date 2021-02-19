package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.type.AbstractType;

import java.util.HashMap;
import java.util.List;

/**
 * @author  MI6 root
 * @TODO 改成继承;
 * @TODO 改成get set分开put方式;
 */
public final class MethodVariableTable
        extends HashMap<String, AbstractType>
{
    public MethodVariableTable()
    {
        super();
    }

    public void putVarList(List<AbstractDeclaration> formalList,
                           List<AbstractDeclaration> localList)
    {
        putVariableList(formalList);
        putVariableList(localList);
    }

    private void putVariableList(List<AbstractDeclaration> declList)
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
