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

/**
 * Created by MI6 root 1/25.
 */
public final class UnReachableDel
        implements IVisitor, Optimizable
{
    private AbstractStatement curStm;
    private boolean isOptimizing;

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
    public void visit(CvaAddExpr e) {}

    @Override
    public void visit(CvaAndAndExpr e) {}

    @Override
    public void visit(CvaCallExpr e) {}

    @Override
    public void visit(CvaFalseExpr e) {}

    @Override
    public void visit(CvaIdentifierExpr e) {}

    @Override
    public void visit(CvaLessThanExpr e) {}

    @Override
    public void visit(CvaNewExpr e) {}

    @Override
    public void visit(CvaNegateExpr e) {}

    @Override
    public void visit(CvaNumberIntExpr e) {}

    @Override
    public void visit(CvaStringExpr expr) {}

    @Override
    public void visit(CvaSubExpr e) {}

    @Override
    public void visit(CvaThisExpr e) {}

    @Override
    public void visit(CvaMulExpr e) {}

    @Override
    public void visit(CvaTrueExpr e) {}

    @Override
    public void visit(CvaAssignStatement s)
    {
        this.curStm = s;
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
                    ((CvaBlockStatement) curStm).getStatementList().forEach(stmList::add);
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
    public void visit(CvaWriteStatement s)
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
        ArrayList<AbstractStatement> stmList = new ArrayList<>();
        m.getStatementList().forEach(stm ->
        {
            this.visit(stm);
            if (this.curStm != null)
            {
                if (this.curStm instanceof CvaBlockStatement)
                {
                    ((CvaBlockStatement) this.curStm).getStatementList().forEach(stmList::add);
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
    public void visit(CvaEntryClass cvaEntryClass)
    {
        this.visit((CvaMainMethod) cvaEntryClass.getMainMethod());
        cvaEntryClass.setStatement(this.curStm);
    }

    @Override
    public void visit(CvaMainMethod entryMethod)
    {
        // FIXME;
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
