package cn.misection.cvac.codegen.ast.decl;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDecl
 * @Description TODO
 * @CreateTime 2021年02月14日 17:53:00
 */
public abstract class AbstractDeclaration implements IDeclaration
{
    protected int lineNum;

    public AbstractDeclaration(int lineNum)
    {
        this.lineNum = lineNum;
    }

    public int getLineNum()
    {
        return lineNum;
    }
}
