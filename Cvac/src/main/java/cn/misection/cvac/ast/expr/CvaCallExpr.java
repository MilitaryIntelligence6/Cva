package cn.misection.cvac.ast.expr;

import cn.misection.cvac.ast.type.AbstractType;

import java.util.List;
import java.util.Queue;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaCallExpr
 * @Description TODO
 * @CreateTime 2021年02月14日 18:56:00
 */
public class CvaCallExpr extends AbstractExpression
{
    private String id;

    private AbstractExpression expr;

    /**
     * 原来是linkedlist;
     */
    private List<AbstractExpression> args;

    /**
     * type of first field "exp";
     */
    private String type;

    /**
     * arg's type;
     */
    private List<AbstractType> argTypeList;

    public CvaCallExpr(int lineNum, String id, AbstractExpression expr, List<AbstractExpression> args, List<AbstractType> argTypeList)
    {
        super(lineNum);
        this.id = id;
        this.expr = expr;
        this.args = args;
        this.argTypeList = argTypeList;
        init();
    }

    private void init()
    {
        this.type = null;
    }

    public String getId()
    {
        return id;
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

    public List<AbstractType> getArgTypeList()
    {
        return argTypeList;
    }
}
