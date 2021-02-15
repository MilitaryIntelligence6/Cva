package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;


import java.util.HashSet;

/**
 * Created by Mengxu on 2017/1/27.
 */
public class DeadCodeDel implements cn.misection.cvac.ast.Visitor, Optimizable
{
    private HashSet<String> curFields;  // the fields of current class
    private HashSet<String> localVars;  // the local variables and formals in current method
    private HashSet<String> localLiveness;  // the living id in current statement
    // private boolean isAssign;   // current id is in the left of assign(true), or is being evaluated(false)
    private boolean containsCall;   // current statement contains method call?
    private boolean shouldDel;  // should delete current statement?
    private boolean isOptimizing;

    @Override
    public void visit(CvaBoolean t)
    {
    }

    @Override
    public void visit(CvaClassType t)
    {
    }

    @Override
    public void visit(CvaInt t)
    {
    }

    @Override
    public void visit(CvaDeclaration d)
    {
    }

    @Override
    public void visit(CvaAddExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaAndAndExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaCallExpr e)
    {
        this.visit(e.getExpr());
        e.getArgs().forEach(this::visit);
        this.containsCall = true;
    }

    @Override
    public void visit(CvaFalseExpr e)
    {
    }

    @Override
    public void visit(CvaIdentifier e)
    {
        if (this.localVars.contains(e.getLiteral()))
        {
            this.localLiveness.add(e.getLiteral());
        }
    }

    @Override
    public void visit(CvaLTExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaNewExpr e)
    {
    }

    @Override
    public void visit(CvaNegateExpr e)
    {
        this.visit(e.getExpr());
    }

    @Override
    public void visit(CvaNumberInt e)
    {
    }

    @Override
    public void visit(CvaSubExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaThisExpr e)
    {
    }

    @Override
    public void visit(CvaMuliExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaTrueExpr e)
    {
    }

    @Override
    public void visit(CvaAssign s)
    {
        if (this.localLiveness.contains(s.getLiteral())
                || this.curFields.contains(s.getLiteral()))
        {
            this.localLiveness.remove(s.getLiteral());
            this.visit(s.getExpr());
            this.shouldDel = false;
            return;
        }

        this.containsCall = false;
        this.visit(s.getExpr());
        if (this.containsCall)
        {
            this.shouldDel = false;
        }
    }

    @Override
    public void visit(CvaBlock s)
    {
        for (int i = s.getStatementList().size() - 1; i >= 0; i--)
        {
            this.visit(s.getStatementList().get(i));
            if (this.shouldDel)
            {
                this.isOptimizing = true;
                s.getStatementList().remove(i);
            }
        }
        this.shouldDel = s.getStatementList().size() == 0;
    }

    @Override
    public void visit(CvaIfStatement s)
    {
        HashSet<String> temOriginal = new HashSet<>();
        this.localLiveness.forEach(temOriginal::add);
        this.visit(s.getThenStatement());
        if (this.shouldDel)
        {
            s.setThenStatement(null);
        }
        HashSet<String> tehLeftLiveness = this.localLiveness;

        this.localLiveness = temOriginal;
        this.visit(s.getElseStatement());
        if (this.shouldDel)
        {
            s.setElseStatement(null);
        }
        tehLeftLiveness.forEach(this.localLiveness::add);

//        this.shouldDel = s.getThenStatement() == null && s.getThenStatement() == null;
        this.shouldDel = s.getThenStatement() == null;
        if (this.shouldDel)
        {
            this.localLiveness = temOriginal;
            return;
        }
        // this.isAssign = false;
        this.visit(s.getCondition());
    }

    @Override
    public void visit(CvaWriteOperation s)
    {
        // this.isAssign = false;
        this.visit(s.getExpr());
        this.shouldDel = false;
    }

    @Override
    public void visit(CvaWhileStatement s)
    {
        HashSet<String> temOriginal = new HashSet<>();
        this.localLiveness.forEach(temOriginal::add);
        this.visit(s.getBody());
        if (this.shouldDel) // this statement will be deleted totally
        {
            this.localLiveness = temOriginal;
            return;         // so return is safe.
        }
        // this.isAssign = false;
        this.visit(s.getCondition());
    }

    @Override
    public void visit(CvaMethod m)
    {
        this.localVars = new HashSet<>();
        m.getFormalList().forEach(f ->
                this.localVars.add(((CvaDeclaration) f).getLiteral()));
        m.getLocalList().forEach(l ->
                this.localVars.add(((CvaDeclaration) l).getLiteral()));
        this.localLiveness = new HashSet<>();

        // this.isAssign = false;
        this.visit(m.getRetExpr());

        for (int i = m.getStatementList().size() - 1; i >= 0; i--)
        {
            this.visit(m.getStatementList().get(i));
            if (this.shouldDel)
            {
                this.isOptimizing = true;
                m.getStatementList().remove(i);
            }
        }
    }

    @Override
    public void visit(CvaClass c)
    {
        this.curFields = new HashSet<>();
        c.getFieldList().forEach(f ->
                this.curFields.add(((CvaDeclaration) f).getLiteral()));

        c.getMethodList().forEach(this::visit);
    }

    @Override
    public void visit(CvaEntry c)
    {
    }

    @Override
    public void visit(CvaProgram p)
    {
        this.isOptimizing = false;
        p.getClassList().forEach(this::visit);
    }

    @Override
    public boolean isOptimizing()
    {
        return this.isOptimizing;
    }
}
