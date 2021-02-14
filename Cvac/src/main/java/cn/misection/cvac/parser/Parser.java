package cn.misection.cvac.parser;

import cn.misection.cvac.ast.Ast;
import cn.misection.cvac.lexer.Lexer;
import cn.misection.cvac.lexer.Token;
import cn.misection.cvac.lexer.Token.Kind;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Mengxu on 2017/1/11.
 */
public class Parser
{
    private Lexer lexer;
    private Token current;

    // for vardecl cn.misection.cvac.parser
    private boolean isValDecl;
    private boolean isMarking;
    private Queue<Token> markedTokens;

    public Parser(InputStream fstream)
    {
        lexer = new Lexer(fstream);
        current = lexer.nextToken();
        isMarking = false;
        markedTokens = new LinkedList<>();
    }

    // utility methods
    private void advance()
    {
        if (isMarking)
        {
            current = lexer.nextToken();
            markedTokens.offer(current);
        } else if (!markedTokens.isEmpty())
            current = markedTokens.poll();
        else current = lexer.nextToken();
    }

    // start recording the tokens
    private void mark()
    {
        isMarking = true;
        markedTokens.offer(current);
    }

    // stop recording the tokens and clear recorded
    private void unMark()
    {
        isMarking = false;
        markedTokens.clear();
    }

    // reset current token and stop recording
    private void reset()
    {
        isMarking = false;
        advance();
    }

    private void eatToken(Kind kind)
    {
        if (kind == current.kind)
            advance();
        else
        {
            System.out.println("Line " + current.lineNum + " :" +
                    "Expects: " + kind.toString() +
                    ", but got: " + current.kind.toString());
            System.exit(1);
        }
    }

    private void error()
    {
        System.out.println("Syntax error at line " +
                (current != null ? current.lineNum + "" : "unknow")
                + " compilation aborting...\n");
        System.exit(1);
    }

    // parse methods

    // ExpList -> Exp ExpRest*
    //         ->
    // ExpRest -> , Exp
    private LinkedList<Ast.Exp.T> parseExpList()
    {
        LinkedList<Ast.Exp.T> explist = new LinkedList<>();
        if (current.kind == Kind.Rparen)
            return explist;
        Ast.Exp.T tem = parseExp();
        tem.lineNum = current.lineNum;
        explist.addLast(tem);
        while (current.kind == Kind.Commer)
        {
            advance();
            tem = parseExp();
            tem.lineNum = current.lineNum;
            explist.add(tem);
        }
        return explist;
    }

    // AtomExp -> (exp)
    //  -> Integer Literal
    //  -> true
    //  -> false
    //  -> this
    //  -> id
    //  -> new id()
    private Ast.Exp.T parseAtomExp()
    {
        Ast.Exp.T exp;
        switch (current.kind)
        {
            case Lparen:
                advance();
                exp = parseExp();
                exp.lineNum = current.lineNum;
                //advance();
                eatToken(Kind.Rparen);
                return exp;
            case NUM:
                exp = new Ast.Exp.Num(Integer.parseInt(current.lexeme),
                        current.lineNum);
                advance();
                return exp;
            case True:
                exp = new Ast.Exp.True(current.lineNum);
                advance();
                return exp;
            case False:
                exp = new Ast.Exp.False(current.lineNum);
                advance();
                return exp;
            case This:
                exp = new Ast.Exp.This(current.lineNum);
                advance();
                return exp;
            case ID:
                exp = new Ast.Exp.Id(current.lexeme, current.lineNum);
                advance();
                return exp;
            case New:
                advance();
                exp = new Ast.Exp.NewObject(current.lexeme, current.lineNum);
                advance();
                eatToken(Kind.Lparen);
                eatToken(Kind.Rparen);
                return exp;
            default:
                error();
                return null;
        }
    }

    // NotExp -> AtomExp
    //  -> AtomExp.id(expList)
    private Ast.Exp.T parseNotExp()
    {
        Ast.Exp.T exp = parseAtomExp();
        while (current.kind == Kind.Dot)
        {
            advance();
            Token id = current;
            eatToken(Kind.ID);
            eatToken(Kind.Lparen);
            exp = new Ast.Exp.Call(exp, id.lexeme, parseExpList(), id.lineNum);
            eatToken(Kind.Rparen);
        }
        return exp;
    }

    // TimesExp -> ! TimesExp
    //  -> NotExp
    private Ast.Exp.T parseTimesExp()
    {
        int i = 0;
        while (current.kind == Kind.Not)
        {
            advance();
            i++;
        }
        Ast.Exp.T exp = parseNotExp();
        Ast.Exp.T tem = new Ast.Exp.Not(exp, exp.lineNum);
        return i % 2 == 0 ? exp : tem;
    }

    // AddSubExp -> TimesExp * TimesExp
    //  -> TimesExp
    private Ast.Exp.T parseAddSubExp()
    {
        Ast.Exp.T tem = parseTimesExp();
        Ast.Exp.T exp = tem;
        while (current.kind == Kind.Times)
        {
            advance();
            tem = parseTimesExp();
            exp = new Ast.Exp.Times(exp, tem, tem.lineNum);
        }
        return exp;
    }

    // LtExp -> AddSubExp + AddSubExp
    //  -> AddSubExp - AddSubExp
    //  -> AddSubExp
    private Ast.Exp.T parseLTExp()
    {
        Ast.Exp.T exp = parseAddSubExp();
        while (current.kind == Kind.Add || current.kind == Kind.Sub)
        {
            boolean isAdd = current.kind == Kind.Add;
            advance();
            Ast.Exp.T tem = parseAddSubExp();
            exp = isAdd ? new Ast.Exp.Add(exp, tem, exp.lineNum)
                    : tem instanceof Ast.Exp.Num ? new Ast.Exp.Add(exp,
                    new Ast.Exp.Num(-((Ast.Exp.Num) tem).num, tem.lineNum), tem.lineNum)
                    : new Ast.Exp.Sub(exp, tem, exp.lineNum);
        }
        return exp;
    }

    // AndExp -> LtExp < LtExp
    // -> LtExp
    private Ast.Exp.T parseAndExp()
    {
        Ast.Exp.T exp = parseLTExp();
        while (current.kind == Kind.LT)
        {
            advance();
            Ast.Exp.T tem = parseLTExp();
            exp = new Ast.Exp.LT(exp, tem, exp.lineNum);
        }
        return exp;
    }

    // Exp -> AndExp && AndExp
    //  -> AndExp
    private Ast.Exp.T parseExp()
    {
        Ast.Exp.T exp = parseAndExp();
        while (current.kind == Kind.And)
        {
            advance();
            Ast.Exp.T tem = parseAndExp();
            exp = new Ast.Exp.And(exp, tem, exp.lineNum);
        }
        return exp;
    }

    // Statement -> { Statement* }
    //  -> if (Exp) Statement else Statement
    //  -> while (Exp) Statement
    //  -> print(Exp);
    //  -> id = Exp;
    private Ast.Stm.T parseStatement()
    {
        Ast.Stm.T stm = null;
        if (current.kind == Kind.Lbrace)
        {
            eatToken(Kind.Lbrace);
            int lineNum = current.lineNum;
            stm = new Ast.Stm.Block(parseStatements(), lineNum);
            eatToken(Kind.Rbrace);
        } else if (current.kind == Kind.If)
        {
            int lineNum = current.lineNum;
            eatToken(Kind.If);
            eatToken(Kind.Lparen);
            Ast.Exp.T condition = parseExp();
            eatToken(Kind.Rparen);
            Ast.Stm.T then_stm = parseStatement();
            eatToken(Kind.Else);
            Ast.Stm.T else_stm = parseStatement();
            stm = new Ast.Stm.If(condition, then_stm, else_stm, lineNum);
        } else if (current.kind == Kind.While)
        {
            int lineNum = current.lineNum;
            eatToken(Kind.While);
            eatToken(Kind.Lparen);
            Ast.Exp.T condition = parseExp();
            eatToken(Kind.Rparen);
            Ast.Stm.T body = parseStatement();
            stm = new Ast.Stm.While(condition, body, lineNum);
        } else if (current.kind == Kind.Print)
        {
            int lineNum = current.lineNum;
            eatToken(Kind.Print);
            eatToken(Kind.Lparen);
            Ast.Exp.T exp = parseExp();
            eatToken(Kind.Rparen);
            eatToken(Kind.Semi);
            stm = new Ast.Stm.Print(exp, lineNum);
        } else if (current.kind == Kind.ID)
        {
            String id = current.lexeme;
            int lineNum = current.lineNum;
            eatToken(Kind.ID);
            eatToken(Kind.Assign);
            Ast.Exp.T exp = parseExp();
            eatToken(Kind.Semi);
            stm = new Ast.Stm.Assign(id, exp, lineNum);
        } else
            error();

        return stm;
    }

    // Statements -> Statement Statements
    //  ->
    private LinkedList<Ast.Stm.T> parseStatements()
    {
        LinkedList<Ast.Stm.T> stms = new LinkedList<>();
        while (current.kind == Kind.Lbrace || current.kind == Kind.If
                || current.kind == Kind.While || current.kind == Kind.ID
                || current.kind == Kind.Print)
            stms.addLast(parseStatement());

        return stms;
    }

    // Type -> int
    //  -> boolean
    //  -> id
    private Ast.Type.T parseType()
    {
        Ast.Type.T type = null;
        if (current.kind == Kind.Boolean)
        {
            type = new Ast.Type.Boolean();
            advance();
        } else if (current.kind == Kind.Int)
        {
            type = new Ast.Type.Int();
            advance();
        } else if (current.kind == Kind.ID)
        {
            type = new Ast.Type.ClassType(current.lexeme);
            advance();
        } else
            error();
        return type;
    }

    // VarDecl -> Type id;
    private Ast.Dec.T parseVarDecl()
    {
        this.mark();
        Ast.Type.T type = parseType();
        if (current.kind == Kind.Assign)  // maybe a assign statement in method
        {
            this.reset();
            isValDecl = false;
            return null;
        } else if (current.kind == Kind.ID)
        {
            String id = current.lexeme;
            advance();
            if (current.kind == Kind.Semi)
            {
                this.unMark();
                isValDecl = true;
                Ast.Dec.T dec = new Ast.Dec.DecSingle(type, id, current.lineNum);
                eatToken(Kind.Semi);
                return dec;
            } else if (current.kind == Kind.Lparen) // maybe a method in class
            {
                isValDecl = false;
                this.reset();
                return null;
            } else
            {
                error();
                return null;
            }
        } else
        {
            error();
            return null;
        }
    }

    // VarDecls -> VarDecl VarDecls
    //  ->
    private LinkedList<Ast.Dec.T> parseVarDecls()
    {
        LinkedList<Ast.Dec.T> decs = new LinkedList<>();
        isValDecl = true;
        while (current.kind == Kind.Int || current.kind == Kind.Boolean
                || current.kind == Kind.ID)
        {
            Ast.Dec.T dec = parseVarDecl();
            if (dec != null) decs.addLast(dec);
            if (!isValDecl) break;
        }
        return decs;
    }

    // FormalList -> Type id FormalRest*
    //  ->
    // FormalRest -> , Type id
    private LinkedList<Ast.Dec.T> parseFormalList()
    {
        LinkedList<Ast.Dec.T> decs = new LinkedList<>();
        if (current.kind == Kind.Int || current.kind == Kind.Boolean
                || current.kind == Kind.ID)
        {
            decs.addLast(new Ast.Dec.DecSingle(parseType(), current.lexeme, current.lineNum));
            eatToken(Kind.ID);
            while (current.kind == Kind.Commer)
            {
                advance();
                decs.addLast(new Ast.Dec.DecSingle(parseType(), current.lexeme, current.lineNum));
                eatToken(Kind.ID);
            }
        }
        return decs;
    }

    // Method -> Type id (FormalList)
    //          {VarDec* Statement* return Exp; }
    private Ast.Method.T parseMethod()
    {
        Ast.Type.T retType = parseType();
        String id = current.lexeme;
        eatToken(Kind.ID);
        eatToken(Kind.Lparen);
        LinkedList<Ast.Dec.T> formalList = parseFormalList();
        eatToken(Kind.Rparen);
        eatToken(Kind.Lbrace);
        LinkedList<Ast.Dec.T> varDecs = parseVarDecls();
        LinkedList<Ast.Stm.T> stms = parseStatements();
        eatToken(Kind.Return);
        Ast.Exp.T retExp = parseExp();
        eatToken(Kind.Semi);
        eatToken(Kind.Rbrace);

        return new Ast.Method.MethodSingle(retType, id, formalList, varDecs, stms, retExp);
    }

    // MethodDecls -> MethodDecl MethodDecls*
    //  ->
    private LinkedList<Ast.Method.T> parseMethodDecls()
    {
        LinkedList<Ast.Method.T> methods = new LinkedList<>();
        while (current.kind == Kind.ID ||
                current.kind == Kind.Int ||
                current.kind == Kind.Boolean)
            methods.addLast(parseMethod());

        return methods;
    }

    // ClassDecl -> class id { VarDecl* MethodDecl* }
    //  -> class id : id { VarDecl* Method* }
    private Ast.Class.T parseClassDecl()
    {
        eatToken(Kind.Class);
        String id = current.lexeme;
        eatToken(Kind.ID);
        String superClass = null;
        if (current.kind == Kind.Colon)
        {
            advance();
            superClass = current.lexeme;
            eatToken(Kind.ID);
        }
        eatToken(Kind.Lbrace);
        LinkedList<Ast.Dec.T> decs = parseVarDecls();
        LinkedList<Ast.Method.T> methods = parseMethodDecls();
        eatToken(Kind.Rbrace);
        return new Ast.Class.ClassSingle(id, superClass, decs, methods);
    }

    // ClassDecls -> ClassDecl ClassDecls*
    //  ->
    private LinkedList<Ast.Class.T> parseClassDecls()
    {
        LinkedList<Ast.Class.T> classes = new LinkedList<>();
        while (current.kind == Kind.Class)
            classes.addLast(parseClassDecl());

        return classes;
    }

    // MainClass -> class id
    //    {
    //        void main()
    //        {
    //            Statement
    //        }
    //    }
    private Ast.MainClass.MainClassSingle parseMainClass()
    {
        eatToken(Kind.Class);
        String id = current.lexeme;
        eatToken(Kind.ID);
        eatToken(Kind.Lbrace);
        eatToken(Kind.Void);
        eatToken(Kind.Main);
        eatToken(Kind.Lparen);
        eatToken(Kind.Rparen);
        eatToken(Kind.Lbrace);
        Ast.Stm.T stm = parseStatement();
        eatToken(Kind.Rbrace);
        eatToken(Kind.Rbrace);
        return new Ast.MainClass.MainClassSingle(id, stm);
    }

    // Program -> MainClass ClassDecl*
    private Ast.Program.ProgramSingle parseProgram()
    {
        Ast.MainClass.MainClassSingle main = parseMainClass();
        LinkedList<Ast.Class.T> classes = parseClassDecls();
        eatToken(Kind.EOF);
        return new Ast.Program.ProgramSingle(main, classes);
    }

    public Ast.Program.T parse()
    {
        return parseProgram();
    }
}
