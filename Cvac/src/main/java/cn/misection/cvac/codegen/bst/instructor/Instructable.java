package cn.misection.cvac.codegen.bst.instructor;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Instructable
 * @Description TODO
 * @CreateTime 2021年02月20日 23:10:00
 */
@FunctionalInterface
public interface Instructable
{
    /**
     * 获得该类型指令;
     * @return instruct;
     */
    String toInst();
}
