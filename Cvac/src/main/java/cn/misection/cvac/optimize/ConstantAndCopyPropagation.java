package cn.misection.cvac.optimize;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.expr.nonterminal.binary.*;
import cn.misection.cvac.ast.expr.terminator.*;
import cn.misection.cvac.ast.expr.nonterminal.unary.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.advance.CvaStringType;


import java.util.HashMap;
import java.util.Map;

/**
 * @author MI6 root
 * @FIXME  常量折叠引起了对i++的折叠, 需要改;
 */
public final class ConstantAndCopyPropagation
        implements IVisitor, Optimizable
{
    /**
     * // constant or copy in current method;
     */
    private Map<String, AbstractExpression> conorcopy;
    private AbstractExpression curExpr;
    private boolean canChange;

    /**
     * // if in while body, the left of assign should be delete from conorcopy
     */
    private boolean inWhile;
    private boolean isOptimizing;

    private boolean isEqual(AbstractExpression fir, AbstractExpression sec)
    {
        return (fir instanceof CvaConstIntExpr
                && sec instanceof CvaConstIntExpr
                && ((CvaConstIntExpr) fir).getValue() == ((CvaConstIntExpr) sec).getValue())
                || (fir instanceof CvaIdentifierExpr
                && sec instanceof CvaIdentifierExpr
                && ((CvaIdentifierExpr) fir).name().equals(((CvaIdentifierExpr) sec).name()));

    }

    private Map<String, AbstractExpression> intersection(
            Map<String, AbstractExpression> first,
            Map<String, AbstractExpression> second)
    {
        Map<String, AbstractExpression> result = new HashMap<>();
        first.forEach((k, v) ->
        {
            if (second.containsKey(k) && isEqual(v, second.get(k)))
            {
                result.put(k, v);
            }
        });
        return result;
    }

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
        this.visit(expr.getLeft());
        if (this.canChange)
        {
            expr.setLeft(this.curExpr);
        }
        this.visit(expr.getRight());
        if (this.canChange)
        {
            expr.setRight(this.curExpr);
        }
        this.canChange = false;
    }

    @Override
    public void visit(CvaCallExpr expr)
    {
        this.visit(expr.getExpr());
        for (int i = 0; i < expr.getArgs().size(); i++)
        {
            this.visit(expr.getArgs().get(i));
            if (this.canChange)
            {
                expr.getArgs().set(i, this.curExpr);
            }
        }
        this.canChange = false;
    }

    @Override
    public void visit(CvaConstFalseExpr expr)
    {
        this.curExpr = expr;
        this.canChange = true;
    }

    @Override
    public void visit(CvaIdentifierExpr expr)
    {
        if (this.conorcopy.containsKey(expr.name()))
        {
            this.isOptimizing = true;
            this.canChange = true;
            this.curExpr = this.conorcopy.get(expr.name());
        }
        else
        {
            this.canChange = false;
        }
    }

    @Override
    public void visit(CvaLessOrMoreThanExpr expr)
    {
        this.visit(expr.getLeft());
        if (this.canChange)
        {
            expr.setLeft(this.curExpr);
        }
        this.visit(expr.getRight());
        if (this.canChange)
        {
            expr.setRight(this.curExpr);
        }
        this.canChange = false;
    }

    @Override
    public void visit(CvaNewExpr expr)
    {
        this.canChange = false;
    }

    @Override
    public void visit(CvaNegateExpr expr)
    {
        this.visit(expr.getExpr());
        if (this.canChange)
        {
            expr.setExpr(this.curExpr);
        }
        this.canChange = false;
    }

    @Override
    public void visit(CvaConstIntExpr expr)
    {
        this.curExpr = expr;
        this.canChange = true;
    }

    @Override
    public void visit(CvaConstStringExpr expr)
    {
        // FIXME
    }


    @Override
    public void visit(CvaThisExpr expr)
    {
        this.canChange = false;
    }

    @Override
    public void visit(CvaConstTrueExpr expr)
    {
        this.curExpr = expr;
        this.canChange = true;
    }

    @Override
    public void visit(CvaOperandOperatorExpr expr)
    {
        switch (expr.toEnum())
        {
            case ADD:
            case SUB:
            case MUL:
            {
                this.visit(expr.getLeft());
                if (this.canChange)
                {
                    expr.setLeft(this.curExpr);
                }
                this.visit(expr.getRight());
                if (this.canChange)
                {
                    expr.setRight(this.curExpr);
                }
                this.canChange = false;
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
        if (this.inWhile)
        {
            this.conorcopy.remove(stm.getVarName());
            return;
        }

        if (stm.getExpr() instanceof CvaIdentifierExpr || stm.getExpr() instanceof CvaConstIntExpr)
        {
            this.conorcopy.put(stm.getVarName(), stm.getExpr());
        }
        else
        {
            this.visit(stm.getExpr());
            if (this.canChange)
            {
                stm.setExpr(this.curExpr);
            }
        }
    }

    @Override
    public void visit(CvaBlockStatement stm)
    {
        stm.getStatementList().forEach(this::visit);
    }

    @Override
    public void visit(CvaIfStatement stm)
    {
        if (this.inWhile)
        {
            return;
        }

        this.visit(stm.getCondition());
        if (this.canChange)
        {
            stm.setCondition(this.curExpr);
        }

        Map<String, AbstractExpression> originalMap = new HashMap<>();
        this.conorcopy.forEach(originalMap::put);
        this.visit(stm.getThenStatement());

        Map<String, AbstractExpression> leftMap = this.conorcopy;
        this.conorcopy = originalMap;
        this.visit(stm.getElseStatement());
        this.conorcopy = intersection(leftMap, this.conorcopy);
    }


    @Override
    public void visit(CvaWriteStatement stm)
    {
        if (this.inWhile)
        {
            return;
        }

        this.visit(stm.getExpr());
        if (this.canChange)
        {
            stm.setExpr(curExpr);
        }
    }

    @Override
    public void visit(CvaWhileForStatement stm)
    {
        // TODO: it is wrong when in multi-layer-loop
        // delete the var which be changed
        this.inWhile = true;
        this.visit(stm.getBody());
        this.inWhile = false;

        this.visit(stm.getCondition());
        this.visit(stm.getBody());
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
    public void visit(CvaMethod cvaMethod)
    {
        this.conorcopy = new HashMap<>();
        cvaMethod.getStatementList().forEach(this::visit);
        this.visit(cvaMethod.getRetExpr());
        if (this.canChange)
        {
            cvaMethod.setRetExpr(this.curExpr);
        }
    }

    @Override
    public void visit(CvaMainMethod entryMethod)
    {
        // FIXME;
    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        cvaClass.getMethodList().forEach(m ->
        {
            this.canChange = false;
            this.visit(m);
        });
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
        return isOptimizing;
    }
}
