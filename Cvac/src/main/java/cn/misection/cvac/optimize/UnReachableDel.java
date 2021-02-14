//package cn.misection.cvac.optimize;
//
//import cn.misection.cvac.ast.FrontAst;
//
//import java.util.LinkedList;
//
///**
// * Created by Mengxu on 2017/1/25.
// */
//public class UnReachableDel implements cn.misection.cvac.ast.Visitor, Optimizable
//{
//    private FrontAst.Stm.T curStm;
//    private boolean isOptimizing;
//
//    @Override
//    public void visit(FrontAst.Type.CvaBoolean t) {}
//
//    @Override
//    public void visit(FrontAst.Type.CvaClassType t) {}
//
//    @Override
//    public void visit(FrontAst.Type.CvaInt t) {}
//
//    @Override
//    public void visit(FrontAst.Decl.CvaDeclaration d) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaAddExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaAndAndExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaCallExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaFalseExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaIdentifier e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaLTExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaNewExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaNegateExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaNumberInt e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaSubExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaThisExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaMuliExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaTrueExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Stm.CvaAssign s)
//    {
//        this.curStm = s;
//    }
//
//    @Override
//    public void visit(FrontAst.Stm.CvaBlock s)
//    {
//        LinkedList<FrontAst.Stm.T> _stms = new LinkedList<>();
//        s.stms.forEach(stm ->
//        {
//            this.visit(stm);
//            if (curStm != null)
//                if (curStm instanceof FrontAst.Stm.CvaBlock)
//                    ((FrontAst.Stm.CvaBlock) curStm).stms.forEach(_stms::add);
//                else _stms.add(curStm);
//        });
//        s.stms = _stms;
//        this.curStm = s;
//    }
//
//    @Override
//    public void visit(FrontAst.Stm.CvaIfStatement s)
//    {
//        if (s.condition instanceof FrontAst.Expr.CvaTrueExpr)
//        {
//            this.isOptimizing = true;
//            this.curStm = s.thenStm;
//            this.visit(this.curStm);
//        } else if (s.condition instanceof FrontAst.Expr.CvaFalseExpr)
//        {
//            this.isOptimizing = true;
//            this.curStm = s.elseStm;
//            this.visit(this.curStm);
//        } else this.curStm = s;
//    }
//
//    @Override
//    public void visit(FrontAst.Stm.CvaWriteOperation s)
//    {
//        this.curStm = s;
//    }
//
//    @Override
//    public void visit(FrontAst.Stm.CvaWhileStatement s)
//    {
//        if (s.condition instanceof FrontAst.Expr.CvaFalseExpr)
//        {
//            this.isOptimizing = true;
//            this.curStm = null;
//        }
//        else if (s.condition instanceof FrontAst.Expr.CvaTrueExpr)
//        {
//            System.out.println("Warning: at line " + s.lineNum
//                    + " : " + "unend-loop!");
//            this.curStm = s;
//        } else this.curStm = s;
//
//    }
//
//    @Override
//    public void visit(FrontAst.Method.CvaMethod m)
//    {
//        LinkedList<FrontAst.Stm.T> _stms = new LinkedList<>();
//        m.stms.forEach(stm ->
//        {
//            this.visit(stm);
//            if (this.curStm != null)
//                if (this.curStm instanceof FrontAst.Stm.CvaBlock)
//                    ((FrontAst.Stm.CvaBlock) this.curStm).stms.forEach(_stms::add);
//                else _stms.add(curStm);
//        });
//        m.stms = _stms;
//    }
//
//    @Override
//    public void visit(FrontAst.Clas.CvaClass c)
//    {
//        c.methods.forEach(this::visit);
//    }
//
//    @Override
//    public void visit(FrontAst.MainClass.CvaEntry c)
//    {
//        this.visit(c.stm);
//        c.stm = this.curStm;
//    }
//
//    @Override
//    public void visit(FrontAst.Program.CvaProgram p)
//    {
//        this.isOptimizing = false;
//        this.visit(p.mainClass);
//        p.classes.forEach(this::visit);
//    }
//
//    @Override
//    public boolean isOptimizing()
//    {
//        return this.isOptimizing;
//    }
//}
