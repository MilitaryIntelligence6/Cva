//package cn.misection.cvac.optimize;
//
//import cn.misection.cvac.ast.FrontAst;
//
//import java.util.Hashtable;
//
///**
// * Created by Mengxu on 2017/1/24.
// */
//public class UnUsedVarDel implements cn.misection.cvac.ast.Visitor, Optimizable
//{
//    private Hashtable<String, FrontAst.Decl.CvaDeclaration> unUsedLocals;
//    private Hashtable<String, FrontAst.Decl.CvaDeclaration> unUsedArgs;
//    private boolean isOptimizing;
//    public boolean givesWarning;
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
//    public void visit(FrontAst.Expr.CvaAddExpr e)
//    {
//        this.visit(e.left);
//        this.visit(e.right);
//    }
//
//    @Override
//    public void visit(FrontAst.Expr.CvaAndAndExpr e)
//    {
//        this.visit(e.left);
//        this.visit(e.right);
//    }
//
//    @Override
//    public void visit(FrontAst.Expr.CvaCallExpr e)
//    {
//        this.visit(e.exp);
//        e.args.forEach(this::visit);
//    }
//
//    @Override
//    public void visit(FrontAst.Expr.CvaFalseExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaIdentifier e)
//    {
//        if (this.unUsedLocals.containsKey(e.literal))
//            this.unUsedLocals.remove(e.literal);
//        else if (this.unUsedArgs.containsKey(e.literal))
//            this.unUsedArgs.remove(e.literal);
//    }
//
//    @Override
//    public void visit(FrontAst.Expr.CvaLTExpr e)
//    {
//        this.visit(e.left);
//        this.visit(e.right);
//    }
//
//    @Override
//    public void visit(FrontAst.Expr.CvaNewExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaNegateExpr e)
//    {
//        this.visit(e.expr);
//    }
//
//    @Override
//    public void visit(FrontAst.Expr.CvaNumberInt e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaSubExpr e)
//    {
//        this.visit(e.left);
//        this.visit(e.right);
//    }
//
//    @Override
//    public void visit(FrontAst.Expr.CvaThisExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Expr.CvaMuliExpr e)
//    {
//        this.visit(e.left);
//        this.visit(e.right);
//    }
//
//    @Override
//    public void visit(FrontAst.Expr.CvaTrueExpr e) {}
//
//    @Override
//    public void visit(FrontAst.Stm.CvaAssign s)
//    {
//        this.visit(new FrontAst.Expr.CvaIdentifier(s.id, s.lineNum));
//        this.visit(s.exp);
//    }
//
//    @Override
//    public void visit(FrontAst.Stm.CvaBlock s)
//    {
//        s.stms.forEach(this::visit);
//    }
//
//    @Override
//    public void visit(FrontAst.Stm.CvaIfStatement s)
//    {
//        this.visit(s.condition);
//        this.visit(s.thenStm);
//        this.visit(s.elseStm);
//    }
//
//    @Override
//    public void visit(FrontAst.Stm.CvaWriteOperation s)
//    {
//        this.visit(s.exp);
//    }
//
//    @Override
//    public void visit(FrontAst.Stm.CvaWhileStatement s)
//    {
//        this.visit(s.condition);
//        this.visit(s.body);
//    }
//
//    @Override
//    public void visit(FrontAst.Method.CvaMethod m)
//    {
//        this.unUsedLocals = new Hashtable<>();
//        m.locals.forEach(local ->
//        {
//            FrontAst.Decl.CvaDeclaration l = (FrontAst.Decl.CvaDeclaration) local;
//            this.unUsedLocals.put(l.literal, l);
//        });
//
//        this.unUsedArgs = new Hashtable<>();
//        m.formals.forEach(formal ->
//        {
//            FrontAst.Decl.CvaDeclaration f = (FrontAst.Decl.CvaDeclaration) formal;
//            this.unUsedArgs.put(f.literal, f);
//        });
//
//        m.stms.forEach(this::visit);
//        this.visit(m.retExp);
//
//        this.isOptimizing = this.unUsedArgs.size() > 0
//                || this.unUsedLocals.size() > 0;
//        this.unUsedArgs.forEach((uak, uao) ->
//        {
//            if (givesWarning)
//                System.out.println("Warning: at line " + uao.lineNum + " : "
//                        + "the argument \"" + uak + "\" of method \""
//                        + m.literal + "\" you have never used.");
//        });
//
//        this.unUsedLocals.forEach((ulk, ulo) ->
//        {
//            if (givesWarning)
//                System.out.println("Warning: at line " + ulo.lineNum + " : "
//                        + "the local variable \"" + ulk + "\" you have never used."
//                        + " Now we delete it.");
//            m.locals.remove(ulo);
//        });
//    }
//
//    @Override
//    public void visit(FrontAst.Clas.CvaClass c)
//    {
//        c.methods.forEach(this::visit);
//    }
//
//    @Override
//    public void visit(FrontAst.MainClass.CvaEntry c) {}
//
//    @Override
//    public void visit(FrontAst.Program.CvaProgram p)
//    {
//        this.isOptimizing = false;
//        p.classes.forEach(this::visit);
//    }
//
//    @Override
//    public boolean isOptimizing()
//    {
//        return this.isOptimizing;
//    }
//}
