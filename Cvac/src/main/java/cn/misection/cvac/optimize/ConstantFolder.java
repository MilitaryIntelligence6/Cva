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

import java.util.ArrayList;
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
        if (isConstant())
        {
            CvaNumberIntExpr temLeft = (CvaNumberIntExpr) this.lastExpr;
            this.visit(expr.getRight());
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
            this.lastExpr = expr;
        }
    }

    @Override
    public void visit(CvaAndAndExpr expr)
    {
        this.visit(expr.getLeft());
        AbstractExpression temLeft = this.lastExpr;
        this.visit(expr.getRight());
        AbstractExpression temRight = this.lastExpr;

        this.isOptimizing = true;
        if (temLeft instanceof CvaFalseExpr
                || temRight instanceof CvaFalseExpr)
        {
            this.lastExpr = new CvaFalseExpr(expr.getLineNum());
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
            this.lastExpr = expr;
        }
    }

    @Override
    public void visit(CvaCallExpr expr)
    {
        List<AbstractExpression> argList = new ArrayList<>();
        expr.getArgs().forEach(arg ->
        {
            this.visit(arg);
            argList.add(this.lastExpr);
        });
        expr.setArgs(argList);
        this.lastExpr = expr;
    }

    @Override
    public void visit(CvaFalseExpr expr)
    {
        this.lastExpr = expr;
    }

    @Override
    public void visit(CvaIdentifierExpr expr)
    {
        this.lastExpr = expr;
    }

    @Override
    public void visit(CvaLessThanExpr expr)
    {
        this.visit(expr.getLeft());
        if (isConstant())
        {
            CvaNumberIntExpr temLeft = (CvaNumberIntExpr) this.lastExpr;
            this.visit(expr.getRight());
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
            this.lastExpr = expr;
        }
    }

    @Override
    public void visit(CvaNewExpr expr)
    {
        this.lastExpr = expr;
    }

    @Override
    public void visit(CvaNegateExpr expr)
    {
        this.visit(expr.getExpr());
        if (isConstant())
        {
            this.isOptimizing = true;
            this.lastExpr = this.lastExpr instanceof CvaTrueExpr
                    ? new CvaFalseExpr(this.lastExpr.getLineNum())
                    : new CvaTrueExpr(this.lastExpr.getLineNum());
        }
        else
        {
            this.lastExpr = expr;
        }
    }

    @Override
    public void visit(CvaNumberIntExpr expr)
    {
        this.lastExpr = expr;
    }

    @Override
    public void visit(CvaStringExpr expr)
    {
        this.lastExpr = expr;
    }

    @Override
    public void visit(CvaSubExpr expr)
    {
        this.visit(expr.getLeft());
        if (isConstant())
        {
            CvaNumberIntExpr temLeft = (CvaNumberIntExpr) this.lastExpr;
            this.visit(expr.getRight());
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
            this.lastExpr = expr;
        }
    }

    @Override
    public void visit(CvaThisExpr expr)
    {
        this.lastExpr = expr;
    }

    @Override
    public void visit(CvaMulExpr expr)
    {
        this.visit(expr.getLeft());
        if (isConstant())
        {
            CvaNumberIntExpr temLeft = (CvaNumberIntExpr) this.lastExpr;
            this.visit(expr.getRight());
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
            this.lastExpr = expr;
        }
    }

    @Override
    public void visit(CvaTrueExpr expr)
    {
        this.lastExpr = expr;
    }

    @Override
    public void visit(CvaAssignStatement stm)
    {
        this.visit(stm.getExpr());
        stm.setExpr(this.lastExpr);
    }

    @Override
    public void visit(CvaBlockStatement stm)
    {
        stm.getStatementList().forEach(this::visit);
    }

    @Override
    public void visit(CvaIfStatement stm)
    {
        this.visit(stm.getCondition());
        stm.setCondition(this.lastExpr);
        this.visit(stm.getThenStatement());
        if (stm.getElseStatement() != null)
        {
            this.visit(stm.getElseStatement());
        }
    }

    @Override
    public void visit(CvaWriteStatement stm)
    {
        this.visit(stm.getExpr());
        stm.setExpr(this.lastExpr);
    }

    @Override
    public void visit(CvaWhileStatement stm)
    {
        this.visit(stm.getCondition());
        stm.setCondition(this.lastExpr);
        this.visit(stm.getBody());
    }

    @Override
    public void visit(CvaMethod cvaMethod)
    {
        cvaMethod.getStatementList().forEach(this::visit);
        this.visit(cvaMethod.getRetExpr());
        cvaMethod.setRetExpr(this.lastExpr);
    }

    @Override
    public void visit(CvaMainMethod entryMethod)
    {
        // FIXME;
    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        cvaClass.getMethodList().forEach(this::visit);
    }

    @Override
    public void visit(CvaEntryClass entryClass)
    {
        this.visit((CvaMainMethod) entryClass.getMainMethod());
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
