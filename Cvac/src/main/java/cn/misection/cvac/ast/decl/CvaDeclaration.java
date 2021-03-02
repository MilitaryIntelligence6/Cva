package cn.misection.cvac.ast.decl;

import cn.misection.cvac.ast.type.ICvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDeclaration
 * @Description TODO
 * @CreateTime 2021年02月14日 19:54:00
 */
public final class CvaDeclaration extends AbstractDeclaration
{
    public CvaDeclaration(int lineNum,
                          String varName,
                          ICvaType type)
    {
        super(lineNum, varName, type);
    }
}
