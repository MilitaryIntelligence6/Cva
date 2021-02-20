package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.basic.CvaBooleanType;
import cn.misection.cvac.ast.type.basic.CvaIntType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.reference.CvaStringType;


import java.util.HashSet;

/**
 * Created by MI6 root 1/27.
 */
public final class DeadCodeDel
        implements IVisitor, Optimizable
{
    private HashSet<String> curFields;  // the fields of current class
    private HashSet<String> localVars;  // the local variables and formals in current method
    private HashSet<String> localLiveness;  // the living id in current statement
    // private boolean isAssign;   // current id is in the left of assign(true), or is being evaluated(false)
    private boolean containsCall;   // current statement contains method call?
    private boolean shouldDel;  // should delete current statement?
    private boolean isOptimizing;

    @Override
    public void visit(CvaBooleanType type) {}

    @Override
    public void visit(CvaClassType type) {}

    @Override
    public void visit(CvaIntType type) {}

    @Override
    public void visit(CvaStringType type) {}

    @Override
    public void visit(CvaDeclaration decl) {}

    @Override
    public void visit(CvaAddExpr expr)
    {
        this.visit(expr.getLeft());
        this.visit(expr.getRight());
    }

    @Override
    public void visit(CvaAndAndExpr expr)
    {
        this.visit(expr.getLeft());
        this.visit(expr.getRight());
    }

    @Override
    public void visit(CvaCallExpr expr)
    {
        this.visit(expr.getExpr());
        expr.getArgs().forEach(this::visit);
        this.containsCall = true;
    }

    @Override
    public void visit(CvaFalseExpr expr)
    {
    }

    @Override
    public void visit(CvaIdentifierExpr expr)
    {
        if (this.localVars.contains(expr.getLiteral()))
        {
            this.localLiveness.add(expr.getLiteral());
        }
    }

    @Override
    public void visit(CvaLessThanExpr expr)
    {
        this.visit(expr.getLeft());
        this.visit(expr.getRight());
    }

    @Override
    public void visit(CvaNewExpr expr)
    {
    }

    @Override
    public void visit(CvaNegateExpr expr)
    {
        this.visit(expr.getExpr());
    }

    @Override
    public void visit(CvaNumberIntExpr expr) {}

    @Override
    public void visit(CvaStringExpr expr)
    {
        // FIXME
    }

    @Override
    public void visit(CvaSubExpr expr)
    {
        this.visit(expr.getLeft());
        this.visit(expr.getRight());
    }

    @Override
    public void visit(CvaThisExpr expr)
    {
    }

    @Override
    public void visit(CvaMulExpr expr)
    {
        this.visit(expr.getLeft());
        this.visit(expr.getRight());
    }

    @Override
    public void visit(CvaTrueExpr expr)
    {
    }

    @Override
    public void visit(CvaAssignStatement stm)
    {
        if (this.localLiveness.contains(stm.getLiteral())
                || this.curFields.contains(stm.getLiteral()))
        {
            this.localLiveness.remove(stm.getLiteral());
            this.visit(stm.getExpr());
            this.shouldDel = false;
            return;
        }

        this.containsCall = false;
        this.visit(stm.getExpr());
        if (this.containsCall)
        {
            this.shouldDel = false;
        }
    }

    @Override
    public void visit(CvaBlockStatement stm)
    {
        for (int i = stm.getStatementList().size() - 1; i >= 0; i--)
        {
            this.visit(stm.getStatementList().get(i));
            if (this.shouldDel)
            {
                this.isOptimizing = true;
                stm.getStatementList().remove(i);
            }
        }
        this.shouldDel = stm.getStatementList().size() == 0;
    }

    @Override
    public void visit(CvaIfStatement stm)
    {
        HashSet<String> temOriginal = new HashSet<>();
        this.localLiveness.forEach(temOriginal::add);
        this.visit(stm.getThenStatement());
        if (this.shouldDel)
        {
            stm.setThenStatement(null);
        }
        HashSet<String> tehLeftLiveness = this.localLiveness;

        this.localLiveness = temOriginal;
        if (stm.getElseStatement() != null)
        {
            this.visit(stm.getElseStatement());
        }
        if (this.shouldDel)
        {
            stm.setElseStatement(null);
        }
        tehLeftLiveness.forEach(this.localLiveness::add);

//        this.shouldDel = s.getThenStatement() == null && s.getThenStatement() == null;
        this.shouldDel = stm.getThenStatement() == null;
        if (this.shouldDel)
        {
            this.localLiveness = temOriginal;
            return;
        }
        // this.isAssign = false;
        this.visit(stm.getCondition());
    }

    @Override
    public void visit(CvaWriteStatement stm)
    {
        // this.isAssign = false;
        this.visit(stm.getExpr());
        this.shouldDel = false;
    }

    @Override
    public void visit(CvaWhileStatement stm)
    {
        HashSet<String> temOriginal = new HashSet<>();
        this.localLiveness.forEach(temOriginal::add);
        this.visit(stm.getBody());
        if (this.shouldDel) // this statement will be deleted totally
        {
            this.localLiveness = temOriginal;
            return;         // so return is safe.
        }
        // this.isAssign = false;
        this.visit(stm.getCondition());
    }

    @Override
    public void visit(CvaMethod m)
    {
        this.localVars = new HashSet<>();
        m.getArgumentList().forEach(f ->
                this.localVars.add(((CvaDeclaration) f).literal()));
        m.getLocalVarList().forEach(l ->
                this.localVars.add(((CvaDeclaration) l).literal()));
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
    public void visit(CvaMainMethod entryMethod)
    {
        // FIXME;
    }

    @Override
    public void visit(CvaClass c)
    {
        this.curFields = new HashSet<>();
        c.getFieldList().forEach(f ->
                this.curFields.add(((CvaDeclaration) f).literal()));

        c.getMethodList().forEach(this::visit);
    }

    @Override
    public void visit(CvaEntryClass entryClass)
    {
    }

    @Override
    public void visit(CvaProgram program)
    {
        this.isOptimizing = false;
        program.getClassList().forEach(this::visit);
    }

    @Override
    public boolean isOptimizing()
    {
        return this.isOptimizing;
    }
}
