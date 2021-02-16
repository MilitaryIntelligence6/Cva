package cn.misection.cvac.codegen.bst.bstatement;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName Ldc
 * @Description TODO
 * @CreateTime 2021年02月16日 00:59:00
 */
public class Ldc extends BaseStatement
{
    private int integ;

    public Ldc(int integ)
    {
        this.integ = integ;
    }

    public int getInteg()
    {
        return integ;
    }

    public void setInteg(int integ)
    {
        this.integ = integ;
    }
}
