package cn.misection.cvac.codegen.bst.bentry;

import cn.misection.cvac.codegen.bst.bstatement.BaseStatement;

import java.util.List;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaEntry
 * @Description TODO
 * @CreateTime 2021年02月14日 18:27:00
 */
public final class GenEntry extends BaseEntry
{
    // FIXME
    // TODO 这里是有改动的, stms是list;
    public GenEntry(String literal, List<BaseStatement> statementList)
    {
        super(literal, statementList);
    }
}
