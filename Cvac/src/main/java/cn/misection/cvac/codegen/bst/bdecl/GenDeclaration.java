package cn.misection.cvac.codegen.bst.bdecl;

import cn.misection.cvac.codegen.bst.btype.BaseType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDeclaration
 * @Description TODO
 * @CreateTime 2021年02月14日 19:54:00
 */
public class GenDeclaration extends BaseDeclaration
{
    private String literal;

    private BaseType type;

    public GenDeclaration( String literal, BaseType type)
    {
        this.literal = literal;
        this.type = type;
    }

    public String getLiteral()
    {
        return literal;
    }

    public BaseType getType()
    {
        return type;
    }
}
