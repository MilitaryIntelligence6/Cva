package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.CvaDeclaration;
import cn.misection.cvac.ast.entry.CvaEntryClass;
import cn.misection.cvac.ast.expr.nonterminal.binary.*;
import cn.misection.cvac.ast.expr.terminator.*;
import cn.misection.cvac.ast.expr.nonterminal.unary.*;
import cn.misection.cvac.ast.method.CvaMainMethod;
import cn.misection.cvac.ast.method.CvaMethod;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.advance.CvaStringType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MI6 root
 */
public final class UnUsedVarDecl
        implements IVisitor, Optimizable
{
    private Map<String, CvaDeclaration> unUsedLocals;
    private Map<String, CvaDeclaration> unUsedArgs;
    private boolean isOptimizing;
    public boolean givesWarning;

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
    }

    @Override
    public void visit(CvaConstFalseExpr expr) {}

    @Override
    public void visit(CvaIdentifierExpr expr)
    {
        if (this.unUsedLocals.containsKey(expr.literal()))
        {
            this.unUsedLocals.remove(expr.literal());
        }
        else
        {
            this.unUsedArgs.remove(expr.literal());
        }
    }

    @Override
    public void visit(CvaLessOrMoreThanExpr expr)
    {
        visit(expr.getLeft());
        visit(expr.getRight());
    }

    @Override
    public void visit(CvaNewExpr expr) {}

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
        visit(expr.getLeft());
        visit(expr.getRight());
    }

    @Override
    public void visit(CvaIncDecExpr expr)
    {
        // TODO;
    }

    @Override
    public void visit(CvaAssignStatement stm)
    {
        visit(new CvaIdentifierExpr(stm.getLineNum(), stm.getLiteral()));
        visit(stm.getExpr());
    }

    @Override
    public void visit(CvaBlockStatement stm)
    {
        stm.getStatementList().forEach(this::visit);
    }

    @Override
    public void visit(CvaIfStatement stm)
    {
        visit(stm.getCondition());
        visit(stm.getThenStatement());
        if (stm.getElseStatement() != null)
        {
            visit(stm.getElseStatement());
        }
    }

    @Override
    public void visit(CvaWriteStatement stm)
    {
        visit(stm.getExpr());
    }

    @Override
    public void visit(CvaWhileForStatement stm)
    {
        visit(stm.getCondition());
        visit(stm.getBody());
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
        visit(m.getRetExpr());

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
