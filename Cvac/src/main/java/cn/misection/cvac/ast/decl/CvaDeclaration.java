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
    private final String literal;

    private final ICvaType type;

    public CvaDeclaration(int lineNum, String literal, ICvaType type)
    {
        super(lineNum);
        this.literal = literal;
        this.type = type;
    }

    @Override
    public String literal()
    {
        return literal;
    }

    @Override
    public ICvaType type()
    {
        return type;
    }
}
