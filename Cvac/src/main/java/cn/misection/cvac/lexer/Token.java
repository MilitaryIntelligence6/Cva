package cn.misection.cvac.lexer;

/**
 * Created by Mengxu on 2017/1/6.
 */
public class Token
{
    public enum Kind
    {
        Add, // +
        And, // &&
        Assign, // =
        Boolean, // boolean
        Class, // class
        Colon, // :
        Commer, // ,
        Dot, // .
        Else, // else
        EOF, // End of file
        False, // false
        ID, // Identifier
        If, // if
        Int, // int
        Lbrace, // {
        Lparen, // (
        LT, // <
        Main, // main
        New, // new
        Not, // !
        NUM, // Integer literal
        Print, // print, we just treat it as a key word
        Rbrace, // }
        Return, // return
        Rparen, // )
        Semi, // ;
        Sub, // -
        This, // this
        Times, // *
        True, // true
        Void, // void
        While // while
    }

    public Kind kind; // the kind of the token
    public String lexeme; // extra lexeme of the token
    public int lineNum; // the line number of the token

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
        return "Token_" + this.kind.toString()
                + (lexeme == null ? "" : " : " + this.lexeme)
                + " : at line " + this.lineNum;
    }
}
