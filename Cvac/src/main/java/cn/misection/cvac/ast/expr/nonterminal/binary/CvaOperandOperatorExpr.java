package cn.misection.cvac.ast.expr.nonterminal.binary;

import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.expr.EnumCvaExpr;
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.codegen.bst.instructor.operand.EnumOperandType;
import cn.misection.cvac.codegen.bst.instructor.operand.EnumOperator;
import cn.misection.cvac.codegen.bst.instructor.Instructable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaOperandOperator
 * @Description TODO
 * @CreateTime 2021年02月21日 22:53:00
 */
public final class CvaOperandOperatorExpr
        extends AbstractBinaryExpr implements Instructable
{
    private EnumCvaType resType;

    private EnumOperandType instType;

    private EnumOperator instOp;

    private CvaOperandOperatorExpr(int lineNum,
                                   AbstractExpression left,
                                   AbstractExpression right)
    {
        super(lineNum, left, right);
    }

    private CvaOperandOperatorExpr(Builder builder)
    {
        super();
        this.lineNum = builder.lineNum;
        this.left = builder.left;
        this.right = builder.right;
        this.instType = builder.instType;
        this.instOp = builder.instOp;
    }

    @Override
    public EnumCvaType resType()
    {
        return this.left.resType();
    }

    @Override
    public EnumCvaExpr toEnum()
    {
        return EnumCvaExpr.BINARY_OPERAND_OP;
    }

    public EnumOperandType getInstType()
    {
        return instType;
    }

    public EnumOperator getInstOp()
    {
        return instOp;
    }

    @Override
    public String toInst()
    {
        return String.format("%s%s", instType.toInst(), instOp.toInst());
    }

    public static class Builder
    {
        private int lineNum;

        private AbstractExpression left;

        private AbstractExpression right;

        private EnumOperandType instType;

        private EnumOperator instOp;

        public Builder() {}

        public CvaOperandOperatorExpr build()
        {
            return new CvaOperandOperatorExpr(this);
        }

        public Builder putLineNum(int lineNum)
        {
            this.lineNum = lineNum;
            return this;
        }

        public Builder putLeft(AbstractExpression left)
        {
            this.left = left;
            return this;
        }

        public Builder putRight(AbstractExpression right)
        {
            this.right = right;
            return this;
        }


        public Builder putInstType(EnumOperandType instType)
        {
            this.instType = instType;
            return this;
        }

        public Builder putInstOp(EnumOperator instOp)
        {
            this.instOp = instOp;
            return this;
        }
    }
}
