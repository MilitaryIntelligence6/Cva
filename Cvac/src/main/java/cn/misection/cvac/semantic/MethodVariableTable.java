package cn.misection.cvac.semantic;

//import cn.misection.cvac.ast.FrontAst;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.decl.CvaDeclaration;
import cn.misection.cvac.ast.type.AbstractType;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by Mengxu on 2017/1/13.
 */
public class MethodVariableTable
{
    private Hashtable<String, AbstractType> table;

    public MethodVariableTable()
    {
        this.table = new Hashtable<>();
    }

    public void put(List<AbstractDeclaration> formals, List<AbstractDeclaration> locals)
    {
        for (AbstractDeclaration dec : formals)
        {
            CvaDeclaration decc = ((CvaDeclaration) dec);
            if (this.table.get(decc.getLiteral()) != null)
            {
                System.out.printf("duplicated parameter: %s at line %d%n", decc.getLiteral(), decc.getLineNum());
                System.exit(1);
            }
            else
            {
                this.table.put(decc.getLiteral(), decc.getType());
            }
        }

        for (AbstractDeclaration dec : locals)
        {
            CvaDeclaration decc = ((CvaDeclaration) dec);
            if (this.table.get(decc.getLiteral()) != null)
            {
                System.out.printf("duplicated variable: %s at line %d%n",
                        decc.getLiteral(), decc.getLineNum());
                System.exit(1);
            }
            else
            {
                this.table.put(decc.getLiteral(), decc.getType());
            }
        }
    }

    public AbstractType get(String id)
    {
        return this.table.get(id);
    }
}
