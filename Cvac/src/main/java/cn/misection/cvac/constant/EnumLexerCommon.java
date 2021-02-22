package cn.misection.cvac.constant;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumLexerCommon
 * @Description TODO
 * @CreateTime 2021年02月23日 00:12:00
 */
public enum EnumLexerCommon
{
    /**
     * lexer常量;
     */
    EOF(Character.highSurrogate(0)),

    NEW_LINE('\n'),

    MAIN_CLASS_NAME("Application"),

    MAIN_METHOD_NAME("main");
    ;

    private char chLiteral;

    private String stringLiteral;

    EnumLexerCommon(char chLiteral)
    {
        this.chLiteral = chLiteral;
    }

    EnumLexerCommon(String stringLiteral)
    {
        this.stringLiteral = stringLiteral;
    }

    public char chr()
    {
        return chLiteral;
    }

    public String string()
    {
        return stringLiteral;
    }
}
