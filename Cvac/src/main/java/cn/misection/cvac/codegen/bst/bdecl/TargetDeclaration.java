package cn.misection.cvac.codegen.bst.bdecl;

import cn.misection.cvac.codegen.bst.btype.ITargetType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaDeclaration
 * @Description TODO
 * @CreateTime 2021年02月14日 19:54:00
 */
public final class TargetDeclaration extends BaseDeclaration {
    private final String varName;

    private ITargetType type;

    public TargetDeclaration(String varName, ITargetType type) {
        this.varName = varName;
        this.type = type;
    }

    public String getVarName() {
        return varName;
    }

    public ITargetType getType() {
        return type;
    }
}
