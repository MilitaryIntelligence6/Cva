package cn.misection.cvac.lexer;

/**
 * Created by Mengxu on 2017/1/6.
 */
public class CvaToken
{
    /**
     * the kind of the token
     */
    private CvaKind kind;

    /**
     * extra lexeme of the token
     */
    private String lexeme;

    /**
     * the line number of the token
     */
    private int lineNum;


    public CvaToken(CvaKind kind, int lineNum)
    {
        this.kind = kind;
        this.lineNum = lineNum;
    }

    public CvaToken(CvaKind kind, int lineNum, String lexeme)
    {
        this.kind = kind;
        this.lineNum = lineNum;
        this.lexeme = lexeme;
    }

    @Override
    public String toString()
    {
        return String.format("Token {%s lexeme: %s : at line %d}",
                this.kind.toString(),
                lexeme == null ? "null" : this.lexeme,
                this.lineNum);
    }

    public CvaKind getKind()
    {
        return kind;
    }

    public String getLexeme()
    {
        return lexeme;
    }

    public int getLineNum()
    {
        return lineNum;
    }
}
