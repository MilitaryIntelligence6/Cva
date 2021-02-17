package cn.misection.cvac.lexer;

/**
 * Created by MI6 root 1/6.
 */
public final class CvaToken
{
    /**
     * the kind of the token
     */
    private CvaKind kind;

    /**
     * extra lexeme of the token
     */
    private String literal;

    /**
     * the line number of the token
     */
    private int lineNum;


    public CvaToken(CvaKind kind, int lineNum)
    {
        this.kind = kind;
        this.lineNum = lineNum;
    }

    public CvaToken(CvaKind kind, int lineNum, String literal)
    {
        this.kind = kind;
        this.lineNum = lineNum;
        this.literal = literal;
    }

    @Override
    public String toString()
    {
        return String.format("Token {%s literal: %s : at line %d}",
                this.kind.toString(),
                literal == null ? "null" : this.literal,
                this.lineNum);
    }

    public CvaKind getKind()
    {
        return kind;
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
