package cn.misection.cvac.constant;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumIncreaseDirection
 * @Description 自增运算符与自减运算符方向;
 * @CreateTime 2021年02月21日 13:18:00
 */
public enum EnumIncDirection {
    /**
     * 自增运算符的方向;
     */
    INCREMENT(1),

    DECREMENT(-1),
    ;

    private final int direction;

    EnumIncDirection(int direction) {
        this.direction = direction;
    }

    public int direction() {
        return direction;
    }
}
