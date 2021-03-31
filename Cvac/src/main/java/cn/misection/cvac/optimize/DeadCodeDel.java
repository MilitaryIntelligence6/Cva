package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.nonterminal.binary.*;
import cn.misection.cvac.ast.expr.terminator.*;
import cn.misection.cvac.ast.expr.nonterminal.unary.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.statement.nullobj.CvaNullStatement;
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.advance.CvaStringType;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by MI6 root 1/27.
 */
public final class DeadCodeDel
        implements IVisitor, Optimizable
{
    /**
     *  the fields of current class
     */
    private Set<String> curFields;

    /**
     *  the local variables and formals in current method
     */
    private Set<String> localVars;

    /**
     *  the living id in current statement
     */
    private Set<String> localLiveness;

    /**
     * current id is in the left of assign(true), or is being evaluated(false);
     */
    // private boolean isAssign;

    /**
     * current statement contains method call?;
     */
    private boolean containsCall;

    /**
     * should delete current statement?;
     */
    private boolean shouldDel;
    private boolean isOptimizing;

    @Override
    public void visit(EnumCvaType basicType) {}

    @Override
    public void visit(CvaStringType type) {}

    @Override
    public void visit(CvaClassType type) {}

    @Override
    public void visit(CvaDeclaration decl) {}


    @Override
    public void visit(CvaAndAndExpr expr)
    {
        visit(expr.getLeft());
        visit(expr.getRight());
    }

    @Override
    public void visit(CvaCallExpr expr)
    {
        visit(expr.getExpr());
        expr.getArgs().forEach(this::visit);
        this.containsCall = true;
    }

    @Override
    public void visit(CvaConstFalseExpr expr)
    {
    }

    @Override
    public void visit(CvaIdentifierExpr expr)
    {
        if (this.localVars.contains(expr.name()))
        {
            this.localLiveness.add(expr.name());
        }
    }

    @Override
    public void visit(CvaLessOrMoreThanExpr expr)
    {
        visit(expr.getLeft());
        visit(expr.getRight());
    }

    @Override
    public void visit(CvaNewExpr expr)
    {
    }

    @Override
    public void visit(CvaNegateExpr expr)
    {
        visit(expr.getExpr());
    }

    @Override
    public void visit(CvaConstIntExpr expr) {}

    @Override
    public void visit(CvaConstStringExpr expr)
    {
        // FIXME
    }

    @Override
    public void visit(CvaThisExpr expr) {}

    @Override
    public void visit(CvaConstTrueExpr expr) {}

    @Override
    public void visit(CvaOperandOperatorExpr expr)
    {
        switch (expr.toEnum())
        {
            case ADD:
            case SUB:
            case MUL:
            {
                visit(expr.getLeft());
                visit(expr.getRight());
                break;
            }
            default:
            {
                break;
            }
        }
    }

    @Override
    public void visit(CvaIncDecExpr expr)
    {
        // TODO;
    }

    @Override
    public void visit(CvaAssignStatement stm)
    {
        if (this.localLiveness.contains(stm.getVarName())
                || this.curFields.contains(stm.getVarName()))
        {
            this.localLiveness.remove(stm.getVarName());
            visit(stm.getExpr());
            this.shouldDel = false;
            return;
        }

        this.containsCall = false;
        visit(stm.getExpr());
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
            visit(stm.getStatementList().get(i));
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
        Set<String> temOriginal = new HashSet<>(localLiveness);
        visit(stm.getThenStatement());
        if (this.shouldDel)
        {
            stm.setThenStatement(null);
        }
        Set<String> tehLeftLiveness = this.localLiveness;

        this.localLiveness = temOriginal;
        visit(stm.getElseStatement());
        if (this.shouldDel)
        {
            stm.setElseStatement(CvaNullStatement.getInstance());
        }
        this.localLiveness.addAll(tehLeftLiveness);

        this.shouldDel = stm.getThenStatement() == null;
        if (this.shouldDel)
        {
            this.localLiveness = temOriginal;
            return;
        }
        visit(stm.getCondition());
    }

    @Override
    public void visit(CvaWriteStatement stm)
    {
        visit(stm.getExpr());
        this.shouldDel = false;
    }

    @Override
    public void visit(CvaWhileForStatement stm)
    {
        Set<String> temOriginal = new HashSet<>(localLiveness);
        visit(stm.getBody());
        if (this.shouldDel) // this statement will be deleted totally
        {
            this.localLiveness = temOriginal;
            return;         // so return is safe.
        }
        // this.isAssign = false;
        visit(stm.getCondition());
    }

    @Override
    public void visit(CvaExprStatement stm)
    {
        visit(stm.getExpr());
    }


    @Override
    public void visit(CvaMethod m)
    {
        this.localVars = new HashSet<>();
        m.getArgumentList().forEach(f ->
                localVars.add((f.name())));

        m.getLocalVarList().forEach(l ->
                localVars.add((l.name())));

        localLiveness = new HashSet<>();

        visit(m.getRetExpr());
        for (int i = m.getStatementList().size() - 1; i >= 0; i--)
        {
            visit(m.getStatementList().get(i));
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
                this.curFields.add((f.name())));

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
