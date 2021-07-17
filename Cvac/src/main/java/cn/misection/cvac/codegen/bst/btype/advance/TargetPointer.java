package cn.misection.cvac.codegen.bst.btype.advance;

import cn.misection.cvac.codegen.bst.btype.basic.EnumTargetType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName TargetPointer
 * @Description TODO
 * @CreateTime 2021年02月20日 22:44:00
 */
public class TargetPointer extends BaseUnsafe {
    @Override
    public String fullName() {
        // FIXME;
        return null;
    }

    @Override
    public EnumTargetType toEnum() {
        return EnumTargetType.POINTER;
    }

    @Override
    public String toInst() {
        // FIXME;
        return null;
    }
}
