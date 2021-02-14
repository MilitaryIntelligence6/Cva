package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.Ast;

import java.util.LinkedList;

/**
 * Created by Mengxu on 2017/1/13.
 */
public class MethodType
{
    public Ast.Type.T retType;
    public LinkedList<Ast.Dec.T> argsType;

    public MethodType(Ast.Type.T retType, LinkedList<Ast.Dec.T> decs)
    {
        this.retType = retType;
        this.argsType = decs;
    }
}
