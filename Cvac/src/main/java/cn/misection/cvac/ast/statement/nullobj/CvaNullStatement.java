package cn.misection.cvac.ast.statement.nullobj;

import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.statement.EnumCvaStatement;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaNullStatement
 * @Description TODO
 * @CreateTime 2021年02月23日 13:52:00
 */
public final class CvaNullStatement extends AbstractStatement {
    private volatile static CvaNullStatement instance = null;

    public static CvaNullStatement getInstance() {
        if (instance == null) {
            synchronized (CvaNullStatement.class) {
                if (instance == null) {
                    instance = new CvaNullStatement();
                }
            }
        }
        return instance;
    }

    private CvaNullStatement() {
        // 只有第一行, 第0行视为-1, 当然其实虚拟机中iconst_m1就是-1;
        super(0);
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public EnumCvaStatement toEnum() {
        return EnumCvaStatement.NULL_POINTER;
    }
}
