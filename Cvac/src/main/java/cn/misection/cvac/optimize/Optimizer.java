package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.FrontAst;
import cn.misection.cvac.ast.program.CvaProgram;

/**
 * Created by Mengxu on 2017/1/31.
 */
public class Optimizer
{
    public void optimize(CvaProgram cvaProgram)
    {
        UnUsedVarDel varDeler = new UnUsedVarDel();
        varDeler.givesWarning = true;
        ConstantFolder folder = new ConstantFolder();
        UnReachableDel deler = new UnReachableDel();
        DeadCodeDel deadDeler = new DeadCodeDel();
        ConstantAndCopyPropagation proper = new ConstantAndCopyPropagation();

        boolean flag;
        do
        {
            varDeler.visit(cvaProgram);
            varDeler.givesWarning = false;
            folder.visit(cvaProgram);
            deler.visit(cvaProgram);
            deadDeler.visit(cvaProgram);
            proper.visit(cvaProgram);
            flag = varDeler.isOptimizing()
                    || folder.isOptimizing()
                    || deler.isOptimizing()
                    || deadDeler.isOptimizing()
                    || proper.isOptimizing();
        } while (flag);
    }
}
