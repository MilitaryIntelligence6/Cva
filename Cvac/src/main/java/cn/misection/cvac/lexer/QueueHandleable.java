package cn.misection.cvac.lexer;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName QueueHandle
 * @Description TODO
 * @CreateTime 2021年02月14日 14:34:00
 */
public interface QueueHandleable
{
    /**
     * 查看;
     * @return 队头;
     */
    int peek();

    /**
     * 查看队伍中某一个, 0开始;
     * @param num 0开始的位置
     * @return 查看的元素;
     */
    int peek(int num);

    /**
     * 头出队一个;
     * @return 头;
     */
    int poll();

    /**
     * 获取队头前 num 个;
     * @param num 个数;
     * @return char构成的string;
     */
    String poll(int num);

    /**
     * 是否空;
     * @return bool;
     */
    boolean isEmpty();
}
