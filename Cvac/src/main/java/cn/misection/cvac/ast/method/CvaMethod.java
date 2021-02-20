package cn.misection.cvac.ast.method;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.type.ICvaType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaMethod
 * @Description TODO
 * @CreateTime 2021年02月14日 20:02:00
 */
public final class CvaMethod extends AbstractMethod
{
    public CvaMethod(String name,
                     ICvaType retType,
                     AbstractExpression retExpr,
                     List<AbstractDeclaration> argumentList,
                     List<AbstractDeclaration> localVarList,
                     List<AbstractStatement> statementList)
    {
        super(name,
                retType,
                retExpr,
                argumentList,
                localVarList,
                statementList);
    }
}
