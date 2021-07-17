package cn.misection.cvac.io;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName QueueHandle
 * @Description TODO
 * @CreateTime 2021年02月14日 14:34:00
 */
public interface IBufferedQueue {
    /**
     * 查看;
     *
     * @return 队头;
     */
    char peek();

    /**
     * 查看队伍中某一个, 0开始;
     *
     * @param advance 0开始的位置
     * @return 查看的元素;
     */
    char peek(int advance);

    /**
     * 头出队一个;
     *
     * @return 头;
     */
    char poll();

    /**
     * 获取队头前 num 个;
     *
     * @param num 个数;
     * @return char构成的string;
     */
    String poll(int num);

    /**
     * 是否有下一个;
     *
     * @return
     */
    boolean hasNext();

    /**
     * 是否有后n个;
     *
     * @param advance
     * @return
     */
    boolean hasNext(int advance);

    /**
     * 是否空;
     *
     * @return bool;
     */
    boolean isEmpty();
}
