package cn.misection.cvac.ast.statement;

import cn.misection.cvac.ast.decl.AbstractDeclaration;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDeclStatement
 * @Description TODO
 * @CreateTime 2021年02月23日 19:52:00
 */
public class CvaDeclStatement extends AbstractStatement
{
    private AbstractDeclaration decl;

    private CvaAssignStatement assign;

    protected CvaDeclStatement(int lineNum)
    {
        super(lineNum);
    }

    @Override
    public EnumCvaStatement toEnum()
    {
        return EnumCvaStatement.DECL_STATEMENT;
    }
}
