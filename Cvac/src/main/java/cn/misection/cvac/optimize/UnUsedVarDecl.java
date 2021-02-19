package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.CvaDeclaration;
import cn.misection.cvac.ast.entry.CvaEntryClass;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.CvaMainMethod;
import cn.misection.cvac.ast.method.CvaMethod;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.basic.CvaBooleanType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.basic.CvaIntType;
import cn.misection.cvac.ast.type.reference.CvaStringType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MI6 root 1/24.
 */
public final class UnUsedVarDecl
        implements IVisitor, Optimizable
{
    private Map<String, CvaDeclaration> unUsedLocals;
    private Map<String, CvaDeclaration> unUsedArgs;
    private boolean isOptimizing;
    public boolean givesWarning;

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
    }

    @Override
    public void visit(CvaFalseExpr e) {}

    @Override
    public void visit(CvaIdentifierExpr e)
    {
        if (this.unUsedLocals.containsKey(e.getLiteral()))
        {
            this.unUsedLocals.remove(e.getLiteral());
        }
        else if (this.unUsedArgs.containsKey(e.getLiteral()))
        {
            this.unUsedArgs.remove(e.getLiteral());
        }
    }

    @Override
    public void visit(CvaLessThanExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaNewExpr e) {}

    @Override
    public void visit(CvaNegateExpr e)
    {
        this.visit(e.getExpr());
    }

    @Override
    public void visit(CvaNumberIntExpr e) {}

    @Override
    public void visit(CvaStringExpr expr)
    {
        // FIXME
    }

    @Override
    public void visit(CvaSubExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaThisExpr e) {}

    @Override
    public void visit(CvaMulExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaTrueExpr e) {}

    @Override
    public void visit(CvaAssignStatement s)
    {
        this.visit(new CvaIdentifierExpr(s.getLineNum(), s.getLiteral()));
        this.visit(s.getExpr());
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
    }

    @Override
    public void visit(CvaWhileStatement s)
    {
        this.visit(s.getCondition());
        this.visit(s.getBody());
    }

    @Override
    public void visit(CvaMethod m)
    {
        this.unUsedLocals = new HashMap<>();
        m.getLocalVarList().forEach(local ->
        {
            CvaDeclaration l = (CvaDeclaration) local;
            this.unUsedLocals.put(l.literal(), l);
        });

        this.unUsedArgs = new HashMap<>();
        m.getArgumentList().forEach(formal ->
        {
            CvaDeclaration f = (CvaDeclaration) formal;
            this.unUsedArgs.put(f.literal(), f);
        });

        m.getStatementList().forEach(this::visit);
        this.visit(m.getRetExpr());

        this.isOptimizing = this.unUsedArgs.size() > 0
                || this.unUsedLocals.size() > 0;
        this.unUsedArgs.forEach((uak, uao) ->
        {
            if (givesWarning)
            {
                System.out.printf("Warning: at Line %d:  the argument \"%s\" of" +
                                " method \"%s\" you have never used.%n",
                        uao.getLineNum(), uak, m.name());
            }
        });

        this.unUsedLocals.forEach((ulk, ulo) ->
        {
            if (givesWarning)
            {
                System.out.printf("Warning: at Line %d:  the local variable " +
                                "\"%s\" you have never used. Now we delete it.%n",
                        ulo.getLineNum(), ulk);
            }
            m.getLocalVarList().remove(ulo);
        });
    }

    @Override
    public void visit(CvaMainMethod entryMethod)
    {
        // FIXME;
    }

    @Override
    public void visit(CvaClass c)
    {
        c.getMethodList().forEach(this::visit);
    }

    @Override
    public void visit(CvaEntryClass c)
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
