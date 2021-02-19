package cn.misection.cvac.ast.clas;

import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.method.AbstractMethod;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaClass
 * @Description TODO
 * @CreateTime 2021年02月14日 19:57:00
 */
public final class CvaClass extends AbstractCvaClass
{
    public CvaClass(String name, String parent,
                    List<AbstractDeclaration> fieldList,
                    List<AbstractMethod> methodList)
    {
        super(name, parent, fieldList, methodList);
    }
}
