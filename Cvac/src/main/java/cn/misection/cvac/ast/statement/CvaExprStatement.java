package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.expr.AbstractExpression;
import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOptsException;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaExprStatement
 * @Description TODO
 * @CreateTime 2021年02月23日 12:19:00
 */
public final class CvaExprStatement extends AbstractStatement
{
    private final AbstractExpression expr;

    public CvaExprStatement(int lineNum,
                            AbstractExpression expr)
    {
        super(lineNum);
        this.expr = expr;
    }

    public AbstractExpression getExpr()
    {
        return expr;
    }

    @Override
    public EnumCvaStatement toEnum()
    {
        return EnumCvaStatement.EXPR_STATEMENT;
    }
}
