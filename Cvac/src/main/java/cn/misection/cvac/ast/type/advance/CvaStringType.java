package cn.misection.cvac.ast.type.advance;

import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaString
 * @Description TODO
 * @CreateTime 2021年02月14日 19:46:00
 */
public final class CvaStringType extends AbstractAdvanceType {
    public static final String TYPE_NAME = "@string";

    private static final EnumCvaType ENUM_TYPE = EnumCvaType.STRING;

    public CvaStringType() {
    }

    @Override
    public EnumCvaType toEnum() {
        return ENUM_TYPE;
    }

    @Override
    public String toString() {
        return TYPE_NAME;
    }
}
