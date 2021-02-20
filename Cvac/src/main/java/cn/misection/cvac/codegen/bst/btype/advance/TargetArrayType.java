package cn.misection.cvac.codegen.bst.btype.advance;

import cn.misection.cvac.codegen.bst.btype.BaseType;
import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName TargetArrayType
 * @Description TODO
 * @CreateTime 2021年02月20日 22:29:00
 */
public class TargetArrayType extends BaseAdvanceType
{
    private BaseType innerType;

    public static final String TYPE_LITERAL = "@array";

    public static final String NEW_I_ARRAY = "newiarray";

    public static final String NEW_A_ARRAY = "newaarray";

    public TargetArrayType(BaseType innerType)
    {
        this.innerType = innerType;
    }

    @Override
    public String toString()
    {
        return TYPE_LITERAL;
    }

    @Override
    public EnumTargetType toEnum()
    {
        return EnumTargetType.TARGET_ARRAY;
    }

    @Override
    public String instruct()
    {
        // FIXME 条件判断;
        return null;
    }

    @Override
    public String literal()
    {
        // FIXME;
        return null;
    }
}
