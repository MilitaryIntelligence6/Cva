package cn.misection.cvac.ast.expr.nullptr;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName NullExpr
 * @Description TODO
 * @CreateTime 2021年02月23日 13:06:00
 */
public final class CvaNullExpr extends AbstractExpression
{
    private static CvaNullExpr instance = null;

    private CvaNullExpr() {}

    public static CvaNullExpr getInstance()
    {
        if (instance == null)
        {
            synchronized (CvaNullExpr.class)
            {
                if (instance == null)
                {
                    instance = new CvaNullExpr();
                }
            }
        }
        return instance;
    }

    @Override
    public EnumCvaType resType()
    {
        return EnumCvaType.NULL;
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.NULL;
    }

    @Override
    public boolean isNull()
    {
        return true;
    }
}
