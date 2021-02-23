package cn.misection.cvac.ast.decl;

import cn.misection.cvac.ast.CvaNullable;
import cn.misection.cvac.ast.type.ICvaType;
import cn.misection.cvac.ast.type.basic.EnumCvaType;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName CvaNullDecl
 * @Description TODO
 * @CreateTime 2021年02月23日 14:48:00
 */
public final class CvaNullDecl extends AbstractDeclaration
{
    private static CvaNullDecl instance = null;

    private CvaNullDecl()
    {
        super(0, "nullDecl", EnumCvaType.NULL);
    }

    public static CvaNullDecl getInstance()
    {
        if (instance == null)
        {
            synchronized (CvaNullDecl.class)
            {
                if (instance == null)
                {
                    instance = new CvaNullDecl();
                }
            }
        }
        return instance;
    }

    @Override
    public String literal()
    {
        return "nullDecl";
    }

    @Override
    public ICvaType type()
    {
        return EnumCvaType.NULL;
    }
}
