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
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.advance.CvaStringType;


import java.util.ArrayList;

/**
 * Created by MI6 root 1/25.
 */
public final class UnReachableDel
        implements IVisitor, Optimizable
{
    private AbstractStatement curStm;
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
    public void visit(CvaAndAndExpr expr) {}

    @Override
    public void visit(CvaCallExpr expr) {}

    @Override
    public void visit(CvaConstFalseExpr expr) {}

    @Override
    public void visit(CvaIdentifierExpr expr) {}

    @Override
    public void visit(CvaLessOrMoreThanExpr expr) {}

    @Override
    public void visit(CvaNewExpr expr) {}

    @Override
    public void visit(CvaNegateExpr expr) {}

    @Override
    public void visit(CvaConstIntExpr expr) {}

    @Override
    public void visit(CvaConstStringExpr expr) {}

    @Override
    public void visit(CvaThisExpr expr) {}

    @Override
    public void visit(CvaConstTrueExpr expr) {}

    @Override
    public void visit(CvaOperandOperatorExpr expr)
    {
        // TODO
    }

    @Override
    public void visit(CvaIncDecExpr expr)
    {
        // TODO;
    }

    @Override
    public void visit(CvaAssignStatement stm)
    {
        this.curStm = stm;
    }

    @Override
    public void visit(CvaBlockStatement s)
    {
        ArrayList<AbstractStatement> stmList = new ArrayList<>();
        s.getStatementList().forEach(stm ->
        {
            this.visit(stm);
            if (curStm != null)
            {
                if (curStm instanceof CvaBlockStatement)
                {
                    stmList.addAll(((CvaBlockStatement) curStm).getStatementList());
                }
                else
                {
                    stmList.add(curStm);
                }
            }
        });
        s.setStatementList(stmList);
        this.curStm = s;
    }

    @Override
    public void visit(CvaIfStatement stm)
    {
        if (stm.getCondition() instanceof CvaConstTrueExpr)
        {
            this.isOptimizing = true;
            this.curStm = stm.getThenStatement();
            this.visit(this.curStm);
        }
        else if (stm.getCondition() instanceof CvaConstFalseExpr)
        {
            this.isOptimizing = true;
            this.curStm = stm.getElseStatement();
            this.visit(this.curStm);
        }
        else
        {
            this.curStm = stm;
        }
    }

    @Override
    public void visit(CvaWriteStatement stm)
    {
        this.curStm = stm;
    }

    @Override
    public void visit(CvaWhileForStatement stm)
    {
        if (stm.getCondition() instanceof CvaConstFalseExpr)
        {
            this.isOptimizing = true;
            this.curStm = null;
        }
        else if (stm.getCondition() instanceof CvaConstTrueExpr)
        {
            System.out.printf("Warning: at Line %d:  unend-loop!%n",
                    stm.getLineNum());
            this.curStm = stm;
        }
        else
        {
            this.curStm = stm;
        }
    }

    @Override
    public void visit(CvaIncreStatement stm)
    {
        // TODO;
    }

    @Override
    public void visit(CvaExprStatement stm)
    {
        visit(stm.getExpr());
    }


    @Override
    public void visit(CvaMethod m)
    {
        ArrayList<AbstractStatement> stmList = new ArrayList<>();
        m.getStatementList().forEach(stm ->
        {
            this.visit(stm);
            if (curStm != null)
            {
                if (this.curStm instanceof CvaBlockStatement)
                {
                    stmList.addAll(((CvaBlockStatement) this.curStm).getStatementList());
                }
                else
                {
                    stmList.add(curStm);
                }
            }
        });
        m.setStatementList(stmList);
    }

    @Override
    public void visit(CvaClass c)
    {
        c.getMethodList().forEach(this::visit);
    }

    @Override
    public void visit(CvaEntryClass entryClass)
    {
        this.visit((CvaMainMethod) entryClass.getMainMethod());
        entryClass.setStatement(this.curStm);
    }

    @Override
    public void visit(CvaMainMethod entryMethod)
    {
        // FIXME;
    }

    @Override
    public void visit(CvaProgram program)
    {
        this.isOptimizing = false;
        this.visit(program.getEntryClass());
        program.getClassList().forEach(this::visit);
    }

    @Override
    public boolean isOptimizing()
    {
        return this.isOptimizing;
    }
}
