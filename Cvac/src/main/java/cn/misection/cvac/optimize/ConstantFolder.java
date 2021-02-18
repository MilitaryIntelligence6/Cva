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
import java.util.List;

/**
 * Created by MI6 root 1/23.
 */
public final class ConstantFolder
        implements IVisitor, Optimizable
{
    private AbstractExpression lastExpr;
    private boolean isOptimizing;

    private boolean isConstant()
    {
        return lastExpr != null && this.isConstant(this.lastExpr);
    }

    private boolean isConstant(AbstractExpression exp)
    {
        return exp instanceof CvaNumberIntExpr
                || exp instanceof CvaTrueExpr
                || exp instanceof CvaFalseExpr;
    }

    @Override
    public void visit(CvaBooleanType t) {}

    @Override
    public void visit(CvaClassType t) {}

    @Override
    public void visit(CvaIntType t) {}

    @Override
    public void visit(CvaStringType type) {}

    @Override
    public void visit(CvaDeclaration d) {}

    @Override
    public void visit(CvaAddExpr e)
    {
        this.visit(e.getLeft());
        if (isConstant())
        {
            CvaNumberIntExpr temLeft = (CvaNumberIntExpr) this.lastExpr;
            this.visit(e.getRight());
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExpr = new CvaNumberIntExpr(
                        temLeft.getValue() + ((CvaNumberIntExpr) this.lastExpr).getValue(),
                        this.lastExpr.getLineNum());
            }
            else
            {
                this.lastExpr = new CvaAddExpr(
                        this.lastExpr.getLineNum(),
                        temLeft,
                        this.lastExpr);
            }
        }
        else
        {
            this.lastExpr = e;
        }
    }

    @Override
    public void visit(CvaAndAndExpr e)
    {
        this.visit(e.getLeft());
        AbstractExpression temLeft = this.lastExpr;
        this.visit(e.getRight());
        AbstractExpression temRight = this.lastExpr;

        this.isOptimizing = true;
        if (temLeft instanceof CvaFalseExpr
                || temRight instanceof CvaFalseExpr)
        {
            this.lastExpr = new CvaFalseExpr(e.getLineNum());
        }
        else if (temLeft instanceof CvaTrueExpr
                && temRight instanceof CvaTrueExpr)
        {
            this.lastExpr = temLeft;
        }
        else if (temLeft instanceof CvaTrueExpr)
        {
            this.lastExpr = temRight;
        }
        else if (temRight instanceof CvaTrueExpr)
        {
            this.lastExpr = temLeft;
        }
        else
        {
            this.isOptimizing = false;
            this.lastExpr = e;
        }
    }

    @Override
    public void visit(CvaCallExpr e)
    {
        List<AbstractExpression> argList = new LinkedList<>();
        e.getArgs().forEach(arg ->
        {
            this.visit(arg);
            argList.add(this.lastExpr);
        });
        e.setArgs(argList);
        this.lastExpr = e;
    }

    @Override
    public void visit(CvaFalseExpr e)
    {
        this.lastExpr = e;
    }

    @Override
    public void visit(CvaIdentifierExpr e)
    {
        this.lastExpr = e;
    }

    @Override
    public void visit(CvaLessThanExpr e)
    {
        this.visit(e.getLeft());
        if (isConstant())
        {
            CvaNumberIntExpr temLeft = (CvaNumberIntExpr) this.lastExpr;
            this.visit(e.getRight());
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExpr = temLeft.getValue() < ((CvaNumberIntExpr) this.lastExpr).getValue()
                        ? new CvaTrueExpr(this.lastExpr.getLineNum())
                        : new CvaFalseExpr(this.lastExpr.getLineNum());
            }
            else
            {
                this.lastExpr = new CvaLessThanExpr(
                        this.lastExpr.getLineNum(),
                        temLeft,
                        this.lastExpr);
            }
        }
        else
        {
            this.lastExpr = e;
        }
    }

    @Override
    public void visit(CvaNewExpr e)
    {
        this.lastExpr = e;
    }

    @Override
    public void visit(CvaNegateExpr e)
    {
        this.visit(e.getExpr());
        if (isConstant())
        {
            this.isOptimizing = true;
            this.lastExpr = this.lastExpr instanceof CvaTrueExpr
                    ? new CvaFalseExpr(this.lastExpr.getLineNum())
                    : new CvaTrueExpr(this.lastExpr.getLineNum());
        }
        else
        {
            this.lastExpr = e;
        }
    }

    @Override
    public void visit(CvaNumberIntExpr e)
    {
        this.lastExpr = e;
    }

    @Override
    public void visit(CvaStringExpr expr)
    {
        this.lastExpr = expr;
    }

    @Override
    public void visit(CvaSubExpr e)
    {
        this.visit(e.getLeft());
        if (isConstant())
        {
            CvaNumberIntExpr temLeft = (CvaNumberIntExpr) this.lastExpr;
            this.visit(e.getRight());
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExpr = new CvaNumberIntExpr(
                        temLeft.getValue() - ((CvaNumberIntExpr) this.lastExpr).getValue(),
                        this.lastExpr.getLineNum());
            }
            else
            {
                this.lastExpr = new CvaSubExpr(
                        this.lastExpr.getLineNum(),
                        temLeft,
                        this.lastExpr);
            }
        }
        else
        {
            this.lastExpr = e;
        }
    }

    @Override
    public void visit(CvaThisExpr e)
    {
        this.lastExpr = e;
    }

    @Override
    public void visit(CvaMulExpr e)
    {
        this.visit(e.getLeft());
        if (isConstant())
        {
            CvaNumberIntExpr temLeft = (CvaNumberIntExpr) this.lastExpr;
            this.visit(e.getRight());
            if (isConstant())
            {
                this.isOptimizing = true;
                this.lastExpr = new CvaNumberIntExpr(
                        temLeft.getValue() * ((CvaNumberIntExpr) this.lastExpr).getValue(),
                        this.lastExpr.getLineNum());
            }
            else
            {
                this.lastExpr = new CvaMulExpr(
                        this.lastExpr.getLineNum(),
                        temLeft,
                        this.lastExpr);
            }
        }
        else
        {
            this.lastExpr = e;
        }
    }

    @Override
    public void visit(CvaTrueExpr e)
    {
        this.lastExpr = e;
    }

    @Override
    public void visit(CvaAssignStatement s)
    {
        this.visit(s.getExpr());
        s.setExpr(this.lastExpr);
    }

    @Override
    public void visit(CvaBlockStatement s)
    {
        s.getStatementList().forEach(this::visit);
    }

    @Override
    public void visit(CvaIfStatement s)
    {
        this.visit(s.getCondition());
        s.setCondition(this.lastExpr);
        this.visit(s.getThenStatement());
        if (s.getElseStatement() != null)
        {
            this.visit(s.getElseStatement());
        }
    }

    @Override
    public void visit(CvaWriteStatement s)
    {
        this.visit(s.getExpr());
        s.setExpr(this.lastExpr);
    }

    @Override
    public void visit(CvaWhileStatement s)
    {
        this.visit(s.getCondition());
        s.setCondition(this.lastExpr);
        this.visit(s.getBody());
    }

    @Override
    public void visit(CvaMethod cvaMethod)
    {
        cvaMethod.getStatementList().forEach(this::visit);
        this.visit(cvaMethod.getRetExpr());
        cvaMethod.setRetExpr(this.lastExpr);
    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        cvaClass.getMethodList().forEach(this::visit);
    }

    @Override
    public void visit(CvaEntry c)
    {
        this.visit(c.getStatement());
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
