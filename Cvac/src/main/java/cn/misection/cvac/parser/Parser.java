package cn.misection.cvac.parser;

import cn.misection.cvac.ast.Ast;
import cn.misection.cvac.lexer.Lexer;
import cn.misection.cvac.lexer.Token;
import cn.misection.cvac.lexer.Kind;

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
        if (current.kind == Kind.CLOSE_PAREN)
            return explist;
        Ast.Exp.T tem = parseExp();
        tem.lineNum = current.lineNum;
        explist.addLast(tem);
        while (current.kind == Kind.COMMA)
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
            case OPEN_PAREN:
                advance();
                exp = parseExp();
                exp.lineNum = current.lineNum;
                //advance();
                eatToken(Kind.CLOSE_PAREN);
                return exp;
            case NUMBER:
                exp = new Ast.Exp.Num(Integer.parseInt(current.lexeme),
                        current.lineNum);
                advance();
                return exp;
            case TRUE:
                exp = new Ast.Exp.True(current.lineNum);
                advance();
                return exp;
            case FALSE:
                exp = new Ast.Exp.False(current.lineNum);
                advance();
                return exp;
            case THIS:
                exp = new Ast.Exp.This(current.lineNum);
                advance();
                return exp;
            case ID:
                exp = new Ast.Exp.Id(current.lexeme, current.lineNum);
                advance();
                return exp;
            case NEW:
                advance();
                exp = new Ast.Exp.NewObject(current.lexeme, current.lineNum);
                advance();
                eatToken(Kind.OPEN_PAREN);
                eatToken(Kind.CLOSE_PAREN);
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
        while (current.kind == Kind.DOT)
        {
            advance();
            Token id = current;
            eatToken(Kind.ID);
            eatToken(Kind.OPEN_PAREN);
            exp = new Ast.Exp.Call(exp, id.lexeme, parseExpList(), id.lineNum);
            eatToken(Kind.CLOSE_PAREN);
        }
        return exp;
    }

    // TimesExp -> ! TimesExp
    //  -> NotExp
    private Ast.Exp.T parseTimesExp()
    {
        int i = 0;
        while (current.kind == Kind.NEGATE)
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
        while (current.kind == Kind.STAR)
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
        while (current.kind == Kind.ADD || current.kind == Kind.SUB)
        {
            boolean isAdd = current.kind == Kind.ADD;
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
        while (current.kind == Kind.LESS_THAN)
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
        while (current.kind == Kind.AND)
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
        if (current.kind == Kind.OPEN_CURLY_BRACE)
        {
            eatToken(Kind.OPEN_CURLY_BRACE);
            int lineNum = current.lineNum;
            stm = new Ast.Stm.Block(parseStatements(), lineNum);
            eatToken(Kind.CLOSE_BRACE);
        } else if (current.kind == Kind.IF)
        {
            int lineNum = current.lineNum;
            eatToken(Kind.IF);
            eatToken(Kind.OPEN_PAREN);
            Ast.Exp.T condition = parseExp();
            eatToken(Kind.CLOSE_PAREN);
            Ast.Stm.T then_stm = parseStatement();
            eatToken(Kind.ELSE);
            Ast.Stm.T else_stm = parseStatement();
            stm = new Ast.Stm.If(condition, then_stm, else_stm, lineNum);
        } else if (current.kind == Kind.WHILE)
        {
            int lineNum = current.lineNum;
            eatToken(Kind.WHILE);
            eatToken(Kind.OPEN_PAREN);
            Ast.Exp.T condition = parseExp();
            eatToken(Kind.CLOSE_PAREN);
            Ast.Stm.T body = parseStatement();
            stm = new Ast.Stm.While(condition, body, lineNum);
        } else if (current.kind == Kind.WRITE)
        {
            int lineNum = current.lineNum;
            eatToken(Kind.WRITE);
            eatToken(Kind.OPEN_PAREN);
            Ast.Exp.T exp = parseExp();
            eatToken(Kind.CLOSE_PAREN);
            eatToken(Kind.SEMI);
            stm = new Ast.Stm.Print(exp, lineNum);
        } else if (current.kind == Kind.ID)
        {
            String id = current.lexeme;
            int lineNum = current.lineNum;
            eatToken(Kind.ID);
            eatToken(Kind.ASSIGN);
            Ast.Exp.T exp = parseExp();
            eatToken(Kind.SEMI);
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
        while (current.kind == Kind.OPEN_CURLY_BRACE || current.kind == Kind.IF
                || current.kind == Kind.WHILE || current.kind == Kind.ID
                || current.kind == Kind.WRITE)
            stms.addLast(parseStatement());

        return stms;
    }

    // Type -> int
    //  -> boolean
    //  -> id
    private Ast.Type.T parseType()
    {
        Ast.Type.T type = null;
        if (current.kind == Kind.BOOLEAN)
        {
            type = new Ast.Type.Boolean();
            advance();
        } else if (current.kind == Kind.INT)
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
        if (current.kind == Kind.ASSIGN)  // maybe a assign statement in method
        {
            this.reset();
            isValDecl = false;
            return null;
        } else if (current.kind == Kind.ID)
        {
            String id = current.lexeme;
            advance();
            if (current.kind == Kind.SEMI)
            {
                this.unMark();
                isValDecl = true;
                Ast.Dec.T dec = new Ast.Dec.DecSingle(type, id, current.lineNum);
                eatToken(Kind.SEMI);
                return dec;
            } else if (current.kind == Kind.OPEN_PAREN) // maybe a method in class
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
        while (current.kind == Kind.INT || current.kind == Kind.BOOLEAN
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
        if (current.kind == Kind.INT || current.kind == Kind.BOOLEAN
                || current.kind == Kind.ID)
        {
            decs.addLast(new Ast.Dec.DecSingle(parseType(), current.lexeme, current.lineNum));
            eatToken(Kind.ID);
            while (current.kind == Kind.COMMA)
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
        eatToken(Kind.OPEN_PAREN);
        LinkedList<Ast.Dec.T> formalList = parseFormalList();
        eatToken(Kind.CLOSE_PAREN);
        eatToken(Kind.OPEN_CURLY_BRACE);
        LinkedList<Ast.Dec.T> varDecs = parseVarDecls();
        LinkedList<Ast.Stm.T> stms = parseStatements();
        eatToken(Kind.RETURN);
        Ast.Exp.T retExp = parseExp();
        eatToken(Kind.SEMI);
        eatToken(Kind.CLOSE_BRACE);

        return new Ast.Method.MethodSingle(retType, id, formalList, varDecs, stms, retExp);
    }

    // MethodDecls -> MethodDecl MethodDecls*
    //  ->
    private LinkedList<Ast.Method.T> parseMethodDecls()
    {
        LinkedList<Ast.Method.T> methods = new LinkedList<>();
        while (current.kind == Kind.ID ||
                current.kind == Kind.INT ||
                current.kind == Kind.BOOLEAN)
            methods.addLast(parseMethod());

        return methods;
    }

    // ClassDecl -> class id { VarDecl* MethodDecl* }
    //  -> class id : id { VarDecl* Method* }
    private Ast.Class.T parseClassDecl()
    {
        eatToken(Kind.CLASS);
        String id = current.lexeme;
        eatToken(Kind.ID);
        String superClass = null;
        if (current.kind == Kind.COLON)
        {
            advance();
            superClass = current.lexeme;
            eatToken(Kind.ID);
        }
        eatToken(Kind.OPEN_CURLY_BRACE);
        LinkedList<Ast.Dec.T> decs = parseVarDecls();
        LinkedList<Ast.Method.T> methods = parseMethodDecls();
        eatToken(Kind.CLOSE_BRACE);
        return new Ast.Class.ClassSingle(id, superClass, decs, methods);
    }

    // ClassDecls -> ClassDecl ClassDecls*
    //  ->
    private LinkedList<Ast.Class.T> parseClassDecls()
    {
        LinkedList<Ast.Class.T> classes = new LinkedList<>();
        while (current.kind == Kind.CLASS)
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
        eatToken(Kind.CLASS);
        String id = current.lexeme;
        eatToken(Kind.ID);
        eatToken(Kind.OPEN_CURLY_BRACE);
        eatToken(Kind.VOID);
        eatToken(Kind.MAIN);
        eatToken(Kind.OPEN_PAREN);
        eatToken(Kind.CLOSE_PAREN);
        eatToken(Kind.OPEN_CURLY_BRACE);
        Ast.Stm.T stm = parseStatement();
        eatToken(Kind.CLOSE_BRACE);
        eatToken(Kind.CLOSE_BRACE);
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
