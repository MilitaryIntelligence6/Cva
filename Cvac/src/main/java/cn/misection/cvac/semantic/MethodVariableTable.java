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

    public void put(LinkedList<Ast.Decl.T> formals, LinkedList<Ast.Decl.T> locals)
    {
        for (Ast.Decl.T dec : formals)
        {
            Ast.Decl.CvaDeclaration decc = ((Ast.Decl.CvaDeclaration) dec);
            if (this.table.get(decc.literal) != null)
            {
                System.out.println("duplicated parameter: " + decc.literal +
                        " at line " + decc.lineNum);
                System.exit(1);
            } else this.table.put(decc.literal, decc.type);
        }

        for (Ast.Decl.T dec : locals)
        {
            Ast.Decl.CvaDeclaration decc = ((Ast.Decl.CvaDeclaration) dec);
            if (this.table.get(decc.literal) != null)
            {
                System.out.println("duplicated variable: " + decc.literal +
                        " at line " + decc.lineNum);
                System.exit(1);
            } else this.table.put(decc.literal, decc.type);
        }
    }

    public Ast.Type.T get(String id)
    {
        return this.table.get(id);
    }
}
