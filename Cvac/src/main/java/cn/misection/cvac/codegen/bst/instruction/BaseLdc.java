package cn.misection.cvac.codegen.bst.instruction;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName BaseLdc
 * @Description TODO
 * @CreateTime 2021年02月19日 01:06:00
 */
public abstract class BaseLdc<T> extends BaseInstruction
{
    /**
     * 获取值;
     * @return
     */
    public abstract T value();
}
