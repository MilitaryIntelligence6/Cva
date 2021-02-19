package cn.misection.cvac.ast.method;

import cn.misection.cvac.ast.ASTree;
import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.expr.AbstractExpression;
import cn.misection.cvac.ast.statement.AbstractStatement;
import cn.misection.cvac.ast.type.AbstractType;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName AbstractMethod
 * @Description TODO
 * @CreateTime 2021年02月14日 18:01:00
 */
public interface IMethod extends ASTree
{
    /**
     * getName;
     * @return
     */
    String name();

    /**
     * 返回值;
     * @return
     */
    AbstractType getRetType();

    /**
     * 返回表达;
     * @return
     */
    AbstractExpression getRetExpr();

    /**
     * 形参List;
     * @return
     */
    List<AbstractDeclaration> getArgumentList();

    /**
     * 局部变量List;
     * @return
     */
    List<AbstractDeclaration> getLocalVarList();

    /**
     * 操作List;
     * @return
     */
    List<AbstractStatement> getStatementList();
}
