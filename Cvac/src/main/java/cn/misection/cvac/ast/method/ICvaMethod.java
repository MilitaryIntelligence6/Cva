package cn.misection.cvac.ast.method;

import cn.misection.cvac.ast.ASTree;
import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.type.ICvaType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName AbstractMethod
 * @Description TODO
 * @CreateTime 2021年02月14日 18:01:00
 */
public interface ICvaMethod extends ASTree
{
    /**
     * getName;
     * @return name;
     */
    String name();

    /**
     * 返回值;
     * @return retType;
     */
    ICvaType getRetType();

    /**
     * 返回表达;
     * @return retExpr;
     */
    AbstractExpression getRetExpr();

    /**
     * 形参List;
     * @return argList;
     */
    List<AbstractDeclaration> getArgumentList();

    /**
     * 局部变量List;
     * @return localVar;
     */
    List<AbstractDeclaration> getLocalVarList();

    /**
     * 操作List;
     * @return stmList;
     */
    List<AbstractStatement> getStatementList();
}
