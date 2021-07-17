package cn.misection.cvac.ast.expr.nonterminal.ternary;

import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaConditionalExpr
 * @Description TODO
 * @CreateTime 2021年02月20日 20:17:00
 */
public class CvaConditionCalcExpr extends AbstractTernaryExpr {
    protected CvaConditionCalcExpr(int lineNum) {
        super(lineNum);
    }

    @Override
    public EnumCvaType resType() {
        // FIXME;
        return null;
    }

    @Override
    public EnumCvaExpr toEnum() {
        return EnumCvaExpr.CONDITION_CALC;
    }
}
