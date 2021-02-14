package cn.misection.cvac.parser;

import cn.misection.cvac.ast.Ast;
import cn.misection.cvac.lexer.CvaKind;
import cn.misection.cvac.lexer.CvaToken;
import cn.misection.cvac.lexer.Lexer;
import cn.misection.cvac.lexer.IBufferedQueue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Mengxu on 2017/1/11.
 */
public class Parser
{
    private Lexer lexer;
    private CvaToken curToken;

    // for vardecl cn.misection.cvac.parser
    private boolean valDeclFlag;
    private boolean markingFlag;
    private Queue<CvaToken> markedTokens;

    public Parser(IBufferedQueue queueStream)
    {
        lexer = new Lexer(queueStream);
        curToken = lexer.nextToken();
        markingFlag = false;
        markedTokens = new LinkedList<>();
    }

    // utility methods
    private void advance()
    {
        if (markingFlag)
        {
            curToken = lexer.nextToken();
            markedTokens.offer(curToken);
        }
        else if (!markedTokens.isEmpty())
        {
            curToken = markedTokens.poll();
        }
        else
        {
            curToken = lexer.nextToken();
        }
    }

    // start recording the tokens
    private void mark()
    {
        markingFlag = true;
        markedTokens.offer(curToken);
    }

    // stop recording the tokens and clear recorded
    private void unMark()
    {
        markingFlag = false;
        markedTokens.clear();
    }

    // reset current token and stop recording
    private void reset()
    {
        markingFlag = false;
        advance();
    }

    private void eatToken(CvaKind kind)
    {
        // FIXME, 写成 遇到EOF就走, 尾巴上那个-1暂时还没解决;
        if (kind == curToken.getKind())// || kind == CvaKind.EOF)
        {
            advance();
        }
        else
        {
            System.err.printf("Line %d :Expects: %s, but got: %s%n",
                    curToken.getLineNum(),
                    kind.toString(),
                    curToken.getKind().toString());
            System.exit(1);
        }
    }

    private void error()
    {
        System.out.printf("Syntax error at line %s compilation aborting...\n%n",
                curToken != null ? curToken.getLineNum() + "" : "unknow");
        System.exit(1);
    }

    // parse methods

    // ExpList -> Exp ExpRest*
    //         ->
    // ExpRest -> , Exp
    private LinkedList<Ast.Exp.T> parseExpList()
    {
        LinkedList<Ast.Exp.T> explist = new LinkedList<>();
        if (curToken.getKind() == CvaKind.CLOSE_PAREN)
        {
            return explist;
        }
        Ast.Exp.T tem = parseExp();
        tem.lineNum = curToken.getLineNum();
        explist.addLast(tem);
        while (curToken.getKind() == CvaKind.COMMA)
        {
            advance();
            tem = parseExp();
            tem.lineNum = curToken.getLineNum();
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
        switch (curToken.getKind())
        {
            case OPEN_PAREN:
                advance();
                exp = parseExp();
                exp.lineNum = curToken.getLineNum();
                //advance();
                eatToken(CvaKind.CLOSE_PAREN);
                return exp;
            case NUMBER:
                exp = new Ast.Exp.CvaNumberInt(Integer.parseInt(curToken.getLexeme()),
                        curToken.getLineNum());
                advance();
                return exp;
            case TRUE:
                exp = new Ast.Exp.CvaTrueExpr(curToken.getLineNum());
                advance();
                return exp;
            case FALSE:
                exp = new Ast.Exp.False(curToken.getLineNum());
                advance();
                return exp;
            case THIS:
                exp = new Ast.Exp.CvaThisExpr(curToken.getLineNum());
                advance();
                return exp;
            case IDENTIFIER:
                exp = new Ast.Exp.Identifier(curToken.getLexeme(), curToken.getLineNum());
                advance();
                return exp;
            case NEW:
                advance();
                exp = new Ast.Exp.NewObject(curToken.getLexeme(), curToken.getLineNum());
                advance();
                eatToken(CvaKind.OPEN_PAREN);
                eatToken(CvaKind.CLOSE_PAREN);
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
        while (curToken.getKind() == CvaKind.DOT)
        {
            advance();
            CvaToken id = curToken;
            eatToken(CvaKind.IDENTIFIER);
            eatToken(CvaKind.OPEN_PAREN);
            exp = new Ast.Exp.Function(exp, id.getLexeme(), parseExpList(), id.getLineNum());
            eatToken(CvaKind.CLOSE_PAREN);
        }
        return exp;
    }

    // TimesExp -> ! TimesExp
    //  -> NotExp
    private Ast.Exp.T parseTimesExp()
    {
        int i = 0;
        while (curToken.getKind() == CvaKind.NEGATE)
        {
            advance();
            i++;
        }
        Ast.Exp.T exp = parseNotExp();
        Ast.Exp.T tem = new Ast.Exp.CvaNegateExpr(exp, exp.lineNum);
        return i % 2 == 0 ? exp : tem;
    }

    // AddSubExp -> TimesExp * TimesExp
    //  -> TimesExp
    private Ast.Exp.T parseAddSubExp()
    {
        Ast.Exp.T tem = parseTimesExp();
        Ast.Exp.T exp = tem;
        while (curToken.getKind() == CvaKind.STAR)
        {
            advance();
            tem = parseTimesExp();
            exp = new Ast.Exp.CvaMuliExpr(exp, tem, tem.lineNum);
        }
        return exp;
    }

    // LtExp -> AddSubExp + AddSubExp
    //  -> AddSubExp - AddSubExp
    //  -> AddSubExp
    private Ast.Exp.T parseLTExp()
    {
        Ast.Exp.T exp = parseAddSubExp();
        while (curToken.getKind() == CvaKind.ADD || curToken.getKind() == CvaKind.SUB)
        {
            boolean isAdd = curToken.getKind() == CvaKind.ADD;
            advance();
            Ast.Exp.T tem = parseAddSubExp();
            exp = isAdd ? new Ast.Exp.Add(exp, tem, exp.lineNum)
                    : tem instanceof Ast.Exp.CvaNumberInt ? new Ast.Exp.Add(exp,
                    new Ast.Exp.CvaNumberInt(-((Ast.Exp.CvaNumberInt) tem).value, tem.lineNum), tem.lineNum)
                    : new Ast.Exp.CvaSubExpr(exp, tem, exp.lineNum);
        }
        return exp;
    }

    // AndExp -> LtExp < LtExp
    // -> LtExp
    private Ast.Exp.T parseAndExp()
    {
        Ast.Exp.T exp = parseLTExp();
        while (curToken.getKind() == CvaKind.LESS_THAN)
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
        while (curToken.getKind() == CvaKind.AND_AND)
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
        if (curToken.getKind() == CvaKind.OPEN_CURLY_BRACE)
        {
            eatToken(CvaKind.OPEN_CURLY_BRACE);
            int lineNum = curToken.getLineNum();
            stm = new Ast.Stm.CvaBlock(parseStatements(), lineNum);
            eatToken(CvaKind.CLOSE_CURLY_BRACE);
        }
        else if (curToken.getKind() == CvaKind.IF)
        {
            int lineNum = curToken.getLineNum();
            eatToken(CvaKind.IF);
            eatToken(CvaKind.OPEN_PAREN);
            Ast.Exp.T condition = parseExp();
            eatToken(CvaKind.CLOSE_PAREN);
            Ast.Stm.T then_stm = parseStatement();
            eatToken(CvaKind.ELSE);
            Ast.Stm.T else_stm = parseStatement();
            stm = new Ast.Stm.CvaIfStatement(condition, then_stm, else_stm, lineNum);
        }
        else if (curToken.getKind() == CvaKind.WHILE)
        {
            int lineNum = curToken.getLineNum();
            eatToken(CvaKind.WHILE);
            eatToken(CvaKind.OPEN_PAREN);
            Ast.Exp.T condition = parseExp();
            eatToken(CvaKind.CLOSE_PAREN);
            Ast.Stm.T body = parseStatement();
            stm = new Ast.Stm.CvaWhileStatement(condition, body, lineNum);
        }
        else if (curToken.getKind() == CvaKind.WRITE)
        {
            int lineNum = curToken.getLineNum();
            eatToken(CvaKind.WRITE);
            eatToken(CvaKind.OPEN_PAREN);
            Ast.Exp.T exp = parseExp();
            eatToken(CvaKind.CLOSE_PAREN);
            eatToken(CvaKind.SEMI);
            stm = new Ast.Stm.CvaWriteOperation(exp, lineNum);
        }
        else if (curToken.getKind() == CvaKind.IDENTIFIER)
        {
            String id = curToken.getLexeme();
            int lineNum = curToken.getLineNum();
            eatToken(CvaKind.IDENTIFIER);
            eatToken(CvaKind.ASSIGN);
            Ast.Exp.T exp = parseExp();
            eatToken(CvaKind.SEMI);
            stm = new Ast.Stm.CvaAssign(id, exp, lineNum);
        }
        else
        {
            error();
        }

        return stm;
    }

    // Statements -> Statement Statements
    //  ->
    private LinkedList<Ast.Stm.T> parseStatements()
    {
        LinkedList<Ast.Stm.T> stms = new LinkedList<>();
        while (curToken.getKind() == CvaKind.OPEN_CURLY_BRACE || curToken.getKind() == CvaKind.IF
                || curToken.getKind() == CvaKind.WHILE || curToken.getKind() == CvaKind.IDENTIFIER
                || curToken.getKind() == CvaKind.WRITE)
        {
            stms.addLast(parseStatement());
        }

        return stms;
    }

    // Type -> int
    //  -> boolean
    //  -> id
    private Ast.Type.T parseType()
    {
        Ast.Type.T type = null;
        if (curToken.getKind() == CvaKind.BOOLEAN)
        {
            type = new Ast.Type.Boolean();
            advance();
        }
        else if (curToken.getKind() == CvaKind.INT)
        {
            type = new Ast.Type.Int();
            advance();
        }
        else if (curToken.getKind() == CvaKind.IDENTIFIER)
        {
            type = new Ast.Type.ClassType(curToken.getLexeme());
            advance();
        }
        else
        {
            error();
        }
        return type;
    }

    // VarDecl -> Type id;
    private Ast.Dec.T parseVarDecl()
    {
        this.mark();
        Ast.Type.T type = parseType();
        if (curToken.getKind() == CvaKind.ASSIGN)  // maybe a assign statement in method
        {
            this.reset();
            valDeclFlag = false;
            return null;
        }
        else if (curToken.getKind() == CvaKind.IDENTIFIER)
        {
            String id = curToken.getLexeme();
            advance();
            if (curToken.getKind() == CvaKind.SEMI)
            {
                this.unMark();
                valDeclFlag = true;
                Ast.Dec.T dec = new Ast.Dec.DecSingle(type, id, curToken.getLineNum());
                eatToken(CvaKind.SEMI);
                return dec;
            }
            else if (curToken.getKind() == CvaKind.OPEN_PAREN) // maybe a method in class
            {
                valDeclFlag = false;
                this.reset();
                return null;
            }
            else
            {
                error();
                return null;
            }
        }
        else
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
        valDeclFlag = true;
        while (curToken.getKind() == CvaKind.INT || curToken.getKind() == CvaKind.BOOLEAN
                || curToken.getKind() == CvaKind.IDENTIFIER)
        {
            Ast.Dec.T dec = parseVarDecl();
            if (dec != null)
            {
                decs.addLast(dec);
            }
            if (!valDeclFlag)
            {
                break;
            }
        }
        return decs;
    }

    // FormalList -> Type id FormalRest*
    //  ->
    // FormalRest -> , Type id
    private LinkedList<Ast.Dec.T> parseFormalList()
    {
        LinkedList<Ast.Dec.T> decs = new LinkedList<>();
        if (curToken.getKind() == CvaKind.INT || curToken.getKind() == CvaKind.BOOLEAN
                || curToken.getKind() == CvaKind.IDENTIFIER)
        {
            decs.addLast(new Ast.Dec.DecSingle(parseType(), curToken.getLexeme(), curToken.getLineNum()));
            eatToken(CvaKind.IDENTIFIER);
            while (curToken.getKind() == CvaKind.COMMA)
            {
                advance();
                decs.addLast(new Ast.Dec.DecSingle(parseType(), curToken.getLexeme(), curToken.getLineNum()));
                eatToken(CvaKind.IDENTIFIER);
            }
        }
        return decs;
    }

    // Method -> Type id (FormalList)
    //          {VarDec* Statement* return Exp; }
    private Ast.Method.T parseMethod()
    {
        Ast.Type.T retType = parseType();
        String id = curToken.getLexeme();
        eatToken(CvaKind.IDENTIFIER);
        eatToken(CvaKind.OPEN_PAREN);
        LinkedList<Ast.Dec.T> formalList = parseFormalList();
        eatToken(CvaKind.CLOSE_PAREN);
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        LinkedList<Ast.Dec.T> varDecs = parseVarDecls();
        LinkedList<Ast.Stm.T> stms = parseStatements();
        eatToken(CvaKind.RETURN);
        Ast.Exp.T retExp = parseExp();
        eatToken(CvaKind.SEMI);
        eatToken(CvaKind.CLOSE_CURLY_BRACE);

        return new Ast.Method.MethodSingle(retType, id, formalList, varDecs, stms, retExp);
    }

    // MethodDecls -> MethodDecl MethodDecls*
    //  ->
    private LinkedList<Ast.Method.T> parseMethodDecls()
    {
        LinkedList<Ast.Method.T> methods = new LinkedList<>();
        while (curToken.getKind() == CvaKind.IDENTIFIER ||
                curToken.getKind() == CvaKind.INT ||
                curToken.getKind() == CvaKind.BOOLEAN)
        {
            methods.addLast(parseMethod());
        }

        return methods;
    }

    // ClassDecl -> class id { VarDecl* MethodDecl* }
    //  -> class id : id { VarDecl* Method* }
    private Ast.Class.T parseClassDecl()
    {
        eatToken(CvaKind.CLASS);
        String id = curToken.getLexeme();
        eatToken(CvaKind.IDENTIFIER);
        String superClass = null;
        if (curToken.getKind() == CvaKind.COLON)
        {
            advance();
            superClass = curToken.getLexeme();
            eatToken(CvaKind.IDENTIFIER);
        }
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        LinkedList<Ast.Dec.T> decs = parseVarDecls();
        LinkedList<Ast.Method.T> methods = parseMethodDecls();
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return new Ast.Class.ClassSingle(id, superClass, decs, methods);
    }

    // ClassDecls -> ClassDecl ClassDecls*
    //  ->
    private LinkedList<Ast.Class.T> parseClassDecls()
    {
        LinkedList<Ast.Class.T> classes = new LinkedList<>();
        while (curToken.getKind() == CvaKind.CLASS)
        {
            classes.addLast(parseClassDecl());
        }

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
        eatToken(CvaKind.CLASS);
        String id = curToken.getLexeme();
        eatToken(CvaKind.IDENTIFIER);
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        eatToken(CvaKind.VOID);
        eatToken(CvaKind.MAIN);
        eatToken(CvaKind.OPEN_PAREN);
        eatToken(CvaKind.CLOSE_PAREN);
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        Ast.Stm.T stm = parseStatement();
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return new Ast.MainClass.MainClassSingle(id, stm);
    }

    // Program -> MainClass ClassDecl*
    private Ast.Program.ProgramSingle parseProgram()
    {
        Ast.MainClass.MainClassSingle main = parseMainClass();
        LinkedList<Ast.Class.T> classes = parseClassDecls();
        eatToken(CvaKind.EOF);
        return new Ast.Program.ProgramSingle(main, classes);
    }

    public Ast.Program.T parse()
    {
        return parseProgram();
    }
}
