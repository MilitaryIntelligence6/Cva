//package cn.misection.cvac.optimize;
//
//import cn.misection.cvac.ast.FrontAst;
//
///**
// * Created by Mengxu on 2017/1/31.
// */
//public class Optimizer
//{
//    public void optimize(FrontAst.Program.T prog)
//    {
//        UnUsedVarDel varDeler = new UnUsedVarDel();
//        varDeler.givesWarning = true;
//        ConstantFolder folder = new ConstantFolder();
//        UnReachableDel deler = new UnReachableDel();
//        DeadCodeDel deadDeler = new DeadCodeDel();
//        ConstantAndCopyPropagation proper = new ConstantAndCopyPropagation();
//
//        boolean flag;
//        do
//        {
//            varDeler.visit(prog);
//            varDeler.givesWarning = false;
//            folder.visit(prog);
//            deler.visit(prog);
//            deadDeler.visit(prog);
//            proper.visit(prog);
//            flag = varDeler.isOptimizing()
//                    || folder.isOptimizing()
//                    || deler.isOptimizing()
//                    || deadDeler.isOptimizing()
//                    || proper.isOptimizing();
//        } while (flag);
//    }
//}
