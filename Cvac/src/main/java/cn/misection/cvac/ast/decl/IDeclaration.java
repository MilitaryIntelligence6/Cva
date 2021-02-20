package cn.misection.cvac.ast.decl;

import cn.misection.cvac.ast.ASTree;
import cn.misection.cvac.ast.type.AbstractType;
import cn.misection.cvac.ast.type.ICvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Declaration
 * @Description TODO
 * @CreateTime 2021年02月14日 17:58:00
 */
public interface IDeclaration extends ASTree
{
    /**
     * 字面量;
     * @return 字面量;
     */
    String literal();

    /**
     * 类型;
     * @return t;
     */
    ICvaType type();
}
