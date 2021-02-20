package cn.misection.cvac.ast.expr.unary;

import cn.misection.cvac.ast.expr.unary.AbstractUnaryExpr;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName AbstractConstExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:25:00
 */
public abstract class AbstractConstExpr extends AbstractUnaryExpr
{
    protected AbstractConstExpr(int lineNum)
    {
        super(lineNum);
    }
}
