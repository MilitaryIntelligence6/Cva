package cn.misection.cvac.semantic;

//import cn.misection.cvac.ast.FrontAst;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.decl.CvaDeclaration;
import cn.misection.cvac.ast.type.AbstractType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MI6 root 1/13.
 */
public final class MethodVariableTable
{
    private Map<String, AbstractType> table;

    public MethodVariableTable()
    {
        this.table = new HashMap<>();
    }

    public void put(List<AbstractDeclaration> formalList,
                    List<AbstractDeclaration> localList)
    {
        for (AbstractDeclaration formal : formalList)
        {
            CvaDeclaration decl = ((CvaDeclaration) formal);
            if (this.table.get(decl.getLiteral()) != null)
            {
                System.err.printf("duplicated parameter: %s at line %d%n",
                        decl.getLiteral(), decl.getLineNum());
                System.exit(1);
            }
            else
            {
                this.table.put(decl.getLiteral(), decl.getType());
            }
        }

        for (AbstractDeclaration local : localList)
        {
            CvaDeclaration decl = ((CvaDeclaration) local);
            if (this.table.get(decl.getLiteral()) != null)
            {
                System.err.printf("duplicated variable: %s at line %d%n",
                        decl.getLiteral(), decl.getLineNum());
                System.exit(1);
            }
            else
            {
                this.table.put(decl.getLiteral(), decl.getType());
            }
        }
    }

    public AbstractType get(String literal)
    {
        return this.table.get(literal);
    }
}
