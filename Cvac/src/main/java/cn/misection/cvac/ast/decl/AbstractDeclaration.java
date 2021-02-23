package cn.misection.cvac.ast.decl;

import cn.misection.cvac.ast.CvaNullable;
import cn.misection.cvac.ast.type.ICvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDecl
 * @Description TODO
 * @CreateTime 2021年02月14日 17:53:00
 */
public abstract class AbstractDeclaration implements IDeclaration, CvaNullable
{
    protected int lineNum;

    protected String literal;

    protected ICvaType type;

    public AbstractDeclaration(int lineNum,
                               String literal,
                               ICvaType type)
    {
        this.lineNum = lineNum;
        this.literal = literal;
        this.type = type;
    }

    public int getLineNum()
    {
        return lineNum;
    }

    @Override
    public boolean isNull()
    {
        return false;
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
