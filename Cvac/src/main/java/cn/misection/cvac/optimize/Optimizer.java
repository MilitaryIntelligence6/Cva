package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.program.AbstractProgram;

/**
 * Created by MI6 root 1/31.
 */
public final class Optimizer
{
    public void optimize(AbstractProgram cvaProgram)
    {
        UnUsedVarDecl varDeler = new UnUsedVarDecl();
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
