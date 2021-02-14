package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.Ast;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Created by Mengxu on 2017/1/13.
 */
public class MethodVariableTable
{
    private Hashtable<String, Ast.Type.T> table;

    public MethodVariableTable()
    {
        this.table = new Hashtable<>();
    }

    public void put(LinkedList<Ast.Dec.T> formals, LinkedList<Ast.Dec.T> locals)
    {
        for (Ast.Dec.T dec : formals)
        {
            Ast.Dec.DecSingle decc = ((Ast.Dec.DecSingle) dec);
            if (this.table.get(decc.id) != null)
            {
                System.out.println("duplicated parameter: " + decc.id +
                        " at line " + decc.lineNum);
                System.exit(1);
            } else this.table.put(decc.id, decc.type);
        }

        for (Ast.Dec.T dec : locals)
        {
            Ast.Dec.DecSingle decc = ((Ast.Dec.DecSingle) dec);
            if (this.table.get(decc.id) != null)
            {
                System.out.println("duplicated variable: " + decc.id +
                        " at line " + decc.lineNum);
                System.exit(1);
            } else this.table.put(decc.id, decc.type);
        }
    }

    public Ast.Type.T get(String id)
    {
        return this.table.get(id);
    }
}
