package cn.misection.cvac.lexer;

/**
 * @author MI6 root
 */
public final class CvaToken
{
    /**
     * the kind of the token
     */
    private final EnumCvaToken enumToken;

    /**
     * extra literal of the token
     */
    private String literal;

    /**
     * the line number of the token
     */
    private final int lineNum;


    public CvaToken(EnumCvaToken enumToken, int lineNum)
    {
        this.enumToken = enumToken;
        this.lineNum = lineNum;
    }

    public CvaToken(EnumCvaToken enumToken, int lineNum, String literal)
    {
        this.enumToken = enumToken;
        this.lineNum = lineNum;
        this.literal = literal;
    }

    @Override
    public String toString()
    {
        return String.format("Token {%s literal: %s : at line %d}",
                this.enumToken.toString(),
                literal == null ? "null" : this.literal,
                this.lineNum);
    }

    public EnumCvaToken toEnum()
    {
        return enumToken;
    }

    public String getLiteral()
    {
        return literal;
    }

    public int getLineNum()
    {
        return lineNum;
    }
}
