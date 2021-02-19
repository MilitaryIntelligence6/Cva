package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.decl.CvaDeclaration;
import cn.misection.cvac.ast.type.AbstractType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MI6 root 1/13.
 * @TODO 改成继承;
 * @TODO 改成get set分开put方式;
 */
public final class MethodVariableTable
{
    private Map<String, AbstractType> table;

    public MethodVariableTable()
    {
        this.table = new HashMap<>();
    }

    public void putVar(List<AbstractDeclaration> formalList,
                       List<AbstractDeclaration> localList)
    {
        putVariable(formalList);
        putVariable(localList);
//        for (AbstractDeclaration local : localList)
//        {
//            CvaDeclaration decl = ((CvaDeclaration) local);
//            if (this.table.get(decl.literal()) != null)
//            {
//                System.err.printf("duplicated variable: %s at line %d%n",
//                        decl.literal(), decl.getLineNum());
//                System.exit(1);
//            }
//            else
//            {
//                this.table.put(decl.literal(), decl.type());
//            }
//        }
    }

    private void putVariable(List<AbstractDeclaration> declList)
    {
        for (AbstractDeclaration decl : declList)
        {
            // FIXME 不知道有无隐患!;
//            CvaDeclaration decl = ((CvaDeclaration) decl);
            if (this.table.get(decl.literal()) != null)
            {
                System.err.printf("duplicated parameter: %s at line %d%n",
                        decl.literal(), decl.getLineNum());
                System.exit(1);
            }
            else
            {
                this.table.put(decl.literal(), decl.type());
            }
        }
    }

    public AbstractType get(String literal)
    {
        return this.table.get(literal);
    }
}
