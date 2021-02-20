package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.ICvaType;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaCallExpr
 * @Description TODO
 * @CreateTime 2021年02月14日 18:56:00
 */
public final class CvaCallExpr extends AbstractExpression
{
    private String literal;

    private AbstractExpression expr;

    /**
     * 原来是ArrayList;
     */
    private List<AbstractExpression> args;

    /**
     * type of first field "exp";
     */
    private String type;

    /**
     * arg's type;
     */
    private List<ICvaType> argTypeList;

    public ICvaType retType;

    public CvaCallExpr(int lineNum,
                       String literal,
                       AbstractExpression expr,
                       List<AbstractExpression> args)
    {
        super(lineNum);
        this.literal = literal;
        this.expr = expr;
        this.args = args;
        init();
    }

    private void init()
    {
        this.type = null;
    }

    @Override
    public EnumCvaType resType()
    {
        return retType.toEnum();
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.CALL;
    }

    public String getLiteral()
    {
        return literal;
    }

    public AbstractExpression getExpr()
    {
        return expr;
    }

    public List<AbstractExpression> getArgs()
    {
        return args;
    }

    public String getType()
    {
        return type;
    }

    public List<ICvaType> getArgTypeList()
    {
        return argTypeList;
    }

    public ICvaType getRetType()
    {
        return retType;
    }

    public void setLiteral(String literal)
    {
        this.literal = literal;
    }

    public void setExpr(AbstractExpression expr)
    {
        this.expr = expr;
    }

    public void setArgs(List<AbstractExpression> args)
    {
        this.args = args;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setArgTypeList(List<ICvaType> argTypeList)
    {
        this.argTypeList = argTypeList;
    }

    public void setRetType(ICvaType retType)
    {
        this.retType = retType;
    }
}
