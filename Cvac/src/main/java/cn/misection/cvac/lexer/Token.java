package cn.misection.cvac.lexer;

/**
 * Created by Mengxu on 2017/1/6.
 */
public class Token
{
    /**
     * the kind of the token
     */
    public Kind kind;

    /**
     * extra lexeme of the token
     */
    public String lexeme;

    /**
     * the line number of the token
     */
    public int lineNum;


    public Token(Kind kind, int lineNum)
    {
        this.kind = kind;
        this.lineNum = lineNum;
    }

    public Token(Kind kind, int lineNum, String lexeme)
    {
        this.kind = kind;
        this.lineNum = lineNum;
        this.lexeme = lexeme;
    }

    @Override
    public String toString()
    {
        return String.format("Token{%s lexeme: %s : at line %d}",
                this.kind.toString(),
                lexeme == null ? "" : this.lexeme,
                this.lineNum);
    }
}
