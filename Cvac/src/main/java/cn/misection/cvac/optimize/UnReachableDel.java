package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;


import java.util.LinkedList;

/**
 * Created by MI6 root 1/25.
 */
public final class UnReachableDel
        implements IVisitor, Optimizable
{
    private AbstractStatement curStm;
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
    }

    @Override
    public void visit(CvaAndAndExpr e)
    {
    }

    @Override
    public void visit(CvaCallExpr e)
    {
    }

    @Override
    public void visit(CvaFalseExpr e)
    {
    }

    @Override
    public void visit(CvaIdentifier e)
    {
    }

    @Override
    public void visit(CvaLessThanExpr e)
    {
    }

    @Override
    public void visit(CvaNewExpr e)
    {
    }

    @Override
    public void visit(CvaNegateExpr e)
    {
    }

    @Override
    public void visit(CvaNumberIntExpr e)
    {
    }

    @Override
    public void visit(CvaSubExpr e)
    {
    }

    @Override
    public void visit(CvaThisExpr e)
    {
    }

    @Override
    public void visit(CvaMuliExpr e)
    {
    }

    @Override
    public void visit(CvaTrueExpr e)
    {
    }

    @Override
    public void visit(CvaAssign s)
    {
        this.curStm = s;
    }

    @Override
    public void visit(CvaBlock s)
    {
        LinkedList<AbstractStatement> stmList = new LinkedList<>();
        s.getStatementList().forEach(stm ->
        {
            this.visit(stm);
            if (curStm != null)
            {
                if (curStm instanceof CvaBlock)
                {
                    ((CvaBlock) curStm).getStatementList().forEach(stmList::add);
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
    public void visit(CvaIfStatement s)
    {
        if (s.getCondition() instanceof CvaTrueExpr)
        {
            this.isOptimizing = true;
            this.curStm = s.getThenStatement();
            this.visit(this.curStm);
        }
        else if (s.getCondition() instanceof CvaFalseExpr)
        {
            this.isOptimizing = true;
            this.curStm = s.getElseStatement();
            this.visit(this.curStm);
        }
        else
        {
            this.curStm = s;
        }
    }

    @Override
    public void visit(CvaWriteOperation s)
    {
        this.curStm = s;
    }

    @Override
    public void visit(CvaWhileStatement s)
    {
        if (s.getCondition() instanceof CvaFalseExpr)
        {
            this.isOptimizing = true;
            this.curStm = null;
        }
        else if (s.getCondition() instanceof CvaTrueExpr)
        {
            System.out.printf("Warning: at Line %d:  unend-loop!%n",
                    s.getLineNum());
            this.curStm = s;
        }
        else
        {
            this.curStm = s;
        }

    }

    @Override
    public void visit(CvaMethod m)
    {
        LinkedList<AbstractStatement> stmList = new LinkedList<>();
        m.getStatementList().forEach(stm ->
        {
            this.visit(stm);
            if (this.curStm != null)
            {
                if (this.curStm instanceof CvaBlock)
                {
                    ((CvaBlock) this.curStm).getStatementList().forEach(stmList::add);
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
    public void visit(CvaEntry c)
    {
        this.visit(c.getStatement());
        c.setStatement(this.curStm);
    }

    @Override
    public void visit(CvaProgram p)
    {
        this.isOptimizing = false;
        this.visit(p.getEntry());
        p.getClassList().forEach(this::visit);
    }

    @Override
    public boolean isOptimizing()
    {
        return this.isOptimizing;
    }
}
