package cn.misection.cvac.codegen.bst.instruction;

import cn.misection.cvac.codegen.bst.Label;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName IFicmplt
 * @Description TODO
 * @CreateTime 2021年02月16日 00:51:00
 */
public final class IfICmpLt extends BaseInstructor
{
    private Label label;

    public IfICmpLt(Label label)
    {
        this.label = label;
    }

    public Label getLabel()
    {
        return label;
    }

    public void setLabel(Label label)
    {
        this.label = label;
    }
}
