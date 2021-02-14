package cn.misection.cvac.parser;

import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;

import cn.misection.cvac.lexer.CvaKind;
import cn.misection.cvac.lexer.CvaToken;
import cn.misection.cvac.lexer.IBufferedQueue;
import cn.misection.cvac.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;
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
    private LinkedList<AbstractExpression> parseExpList()
    {
        LinkedList<AbstractExpression> explist = new LinkedList<>();
        if (curToken.getKind() == CvaKind.CLOSE_PAREN)
        {
            return explist;
        }
        AbstractExpression tem = parseExp();
        tem.setLineNum(curToken.getLineNum());
        explist.addLast(tem);
        while (curToken.getKind() == CvaKind.COMMA)
        {
            advance();
            tem = parseExp();
            tem.setLineNum(curToken.getLineNum());
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
    private AbstractExpression parseAtomExp()
    {
        AbstractExpression exp;
        switch (curToken.getKind())
        {
            case OPEN_PAREN:
                advance();
                exp = parseExp();
                exp.setLineNum(curToken.getLineNum());
                //advance();
                eatToken(CvaKind.CLOSE_PAREN);
                return exp;
            case NUMBER:
                exp = new CvaNumberInt(curToken.getLineNum(), Integer.parseInt(curToken.getLiteral()));
                advance();
                return exp;
            case TRUE:
                exp = new CvaTrueExpr(curToken.getLineNum());
                advance();
                return exp;
            case FALSE:
                exp = new CvaFalseExpr(curToken.getLineNum());
                advance();
                return exp;
            case THIS:
                exp = new CvaThisExpr(curToken.getLineNum());
                advance();
                return exp;
            case IDENTIFIER:
                exp = new CvaIdentifier(curToken.getLineNum(), curToken.getLiteral());
                advance();
                return exp;
            case NEW:
                advance();
                exp = new CvaNewExpr(curToken.getLineNum(), curToken.getLiteral());
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
    private AbstractExpression parseNotExp()
    {
        AbstractExpression expr = parseAtomExp();
        while (curToken.getKind() == CvaKind.DOT)
        {
            advance();
            CvaToken token = curToken;
            eatToken(CvaKind.IDENTIFIER);
            eatToken(CvaKind.OPEN_PAREN);
            expr = new CvaCallExpr(
                    token.getLineNum(),
                    token.getLiteral(),
                    expr,
                    parseExpList()
            );
            eatToken(CvaKind.CLOSE_PAREN);
        }
        return expr;
    }

    // TimesExp -> ! TimesExp
    //  -> NotExp
    private AbstractExpression parseTimesExp()
    {
        int i = 0;
        while (curToken.getKind() == CvaKind.NEGATE)
        {
            advance();
            i++;
        }
        AbstractExpression exp = parseNotExp();
        AbstractExpression tem = new CvaNegateExpr(
                exp.getLineNum(), exp);
        return i % 2 == 0 ? exp : tem;
    }

    // AddSubExp -> TimesExp * TimesExp
    //  -> TimesExp
    private AbstractExpression parseAddSubExp()
    {
        AbstractExpression tem = parseTimesExp();
        AbstractExpression exp = tem;
        while (curToken.getKind() == CvaKind.STAR)
        {
            advance();
            tem = parseTimesExp();
            exp = new CvaMuliExpr(tem.getLineNum(), exp, tem);
        }
        return exp;
    }

    // LtExp -> AddSubExp + AddSubExp
    //  -> AddSubExp - AddSubExp
    //  -> AddSubExp
    private AbstractExpression parseLTExp()
    {
        AbstractExpression exp = parseAddSubExp();
        while (curToken.getKind() == CvaKind.ADD || curToken.getKind() == CvaKind.SUB)
        {
            boolean addFlag = curToken.getKind() == CvaKind.ADD;
            advance();
            AbstractExpression tem = parseAddSubExp();
            exp = addFlag ? 
                    new CvaAddExpr(exp.getLineNum(), exp, tem)
                    : tem instanceof CvaNumberInt
                    ? new CvaAddExpr(tem.getLineNum(),
                    exp,
                    new CvaNumberInt(tem.getLineNum(),
                            -((CvaNumberInt) tem).getValue()))
                    : new CvaSubExpr(exp.getLineNum(), exp, tem);
        }
        return exp;
    }

    // AndExp -> LtExp < LtExp
    // -> LtExp
    private AbstractExpression parseAndExp()
    {
        AbstractExpression exp = parseLTExp();
        while (curToken.getKind() == CvaKind.LESS_THAN)
        {
            advance();
            AbstractExpression tem = parseLTExp();
            exp = new CvaLTExpr(exp.getLineNum(), exp, tem);
        }
        return exp;
    }

    // Exp -> AndExp && AndExp
    //  -> AndExp
    private AbstractExpression parseExp()
    {
        AbstractExpression exp = parseAndExp();
        while (curToken.getKind() == CvaKind.AND_AND)
        {
            advance();
            AbstractExpression tem = parseAndExp();
            exp = new CvaAndAndExpr(exp.getLineNum(), exp, tem);
        }
        return exp;
    }

    // Statement -> { Statement* }
    //  -> if (Exp) Statement else Statement
    //  -> while (Exp) Statement
    //  -> print(Exp);
    //  -> id = Exp;
    private AbstractStatement parseStatement()
    {
        AbstractStatement stm = null;
        if (curToken.getKind() == CvaKind.OPEN_CURLY_BRACE)
        {
            eatToken(CvaKind.OPEN_CURLY_BRACE);
            int lineNum = curToken.getLineNum();
            stm = new CvaBlock(lineNum, parseStatements());
            eatToken(CvaKind.CLOSE_CURLY_BRACE);
        }
        else if (curToken.getKind() == CvaKind.IF)
        {
            int lineNum = curToken.getLineNum();
            eatToken(CvaKind.IF);
            eatToken(CvaKind.OPEN_PAREN);
            AbstractExpression condition = parseExp();
            eatToken(CvaKind.CLOSE_PAREN);
            AbstractStatement thenStm = parseStatement();
            eatToken(CvaKind.ELSE);
            AbstractStatement elseStm = parseStatement();
            stm = new CvaIfStatement(lineNum, condition, thenStm, elseStm);
        }
        else if (curToken.getKind() == CvaKind.WHILE)
        {
            int lineNum = curToken.getLineNum();
            eatToken(CvaKind.WHILE);
            eatToken(CvaKind.OPEN_PAREN);
            AbstractExpression condition = parseExp();
            eatToken(CvaKind.CLOSE_PAREN);
            AbstractStatement body = parseStatement();
            stm = new CvaWhileStatement(lineNum, condition, body);
        }
        else if (curToken.getKind() == CvaKind.WRITE)
        {
            int lineNum = curToken.getLineNum();
            eatToken(CvaKind.WRITE);
            eatToken(CvaKind.OPEN_PAREN);
            AbstractExpression exp = parseExp();
            eatToken(CvaKind.CLOSE_PAREN);
            eatToken(CvaKind.SEMI);
            stm = new CvaWriteOperation(lineNum, exp);
        }
        else if (curToken.getKind() == CvaKind.IDENTIFIER)
        {
            String literal = curToken.getLiteral();
            int lineNum = curToken.getLineNum();
            eatToken(CvaKind.IDENTIFIER);
            eatToken(CvaKind.ASSIGN);
            AbstractExpression exp = parseExp();
            eatToken(CvaKind.SEMI);
            stm = new CvaAssign(lineNum, literal, exp);
        }
        else
        {
            error();
        }

        return stm;
    }

    // Statements -> Statement Statements
    //  ->
    private LinkedList<AbstractStatement> parseStatements()
    {
        LinkedList<AbstractStatement> stms = new LinkedList<>();
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
    private AbstractType parseType()
    {
        AbstractType type = null;
        if (curToken.getKind() == CvaKind.BOOLEAN)
        {
            type = new CvaBoolean();
            advance();
        }
        else if (curToken.getKind() == CvaKind.INT)
        {
            type = new CvaInt();
            advance();
        }
        else if (curToken.getKind() == CvaKind.IDENTIFIER)
        {
            // 应该是type;
            type = new CvaClassType(curToken.getLiteral());
            advance();
        }
        else
        {
            error();
        }
        return type;
    }

    // VarDecl -> Type id;
    private AbstractDeclaration parseVarDecl()
    {
        this.mark();
        AbstractType type = parseType();
        if (curToken.getKind() == CvaKind.ASSIGN)  // maybe a assign statement in method
        {
            this.reset();
            valDeclFlag = false;
            return null;
        }
        else if (curToken.getKind() == CvaKind.IDENTIFIER)
        {
            String literal = curToken.getLiteral();
            advance();
            if (curToken.getKind() == CvaKind.SEMI)
            {
                this.unMark();
                valDeclFlag = true;
                AbstractDeclaration dec = new CvaDeclaration(curToken.getLineNum(), literal, type);
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
    private LinkedList<AbstractDeclaration> parseVarDecls()
    {
        LinkedList<AbstractDeclaration> decs = new LinkedList<>();
        valDeclFlag = true;
        while (curToken.getKind() == CvaKind.INT || curToken.getKind() == CvaKind.BOOLEAN
                || curToken.getKind() == CvaKind.IDENTIFIER)
        {
            AbstractDeclaration dec = parseVarDecl();
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
    private LinkedList<AbstractDeclaration> parseFormalList()
    {
        LinkedList<AbstractDeclaration> decs = new LinkedList<>();
        if (curToken.getKind() == CvaKind.INT || curToken.getKind() == CvaKind.BOOLEAN
                || curToken.getKind() == CvaKind.IDENTIFIER)
        {
            decs.addLast(new CvaDeclaration(curToken.getLineNum(), curToken.getLiteral(), parseType()));
            eatToken(CvaKind.IDENTIFIER);
            while (curToken.getKind() == CvaKind.COMMA)
            {
                advance();
                decs.addLast(new CvaDeclaration(curToken.getLineNum(), curToken.getLiteral(), parseType()));
                eatToken(CvaKind.IDENTIFIER);
            }
        }
        return decs;
    }

    // Method -> Type id (FormalList)
    //          {VarDec* Statement* return Exp; }
    private AbstractMethod parseMethod()
    {
        AbstractType retType = parseType();
        String literal = curToken.getLiteral();
        eatToken(CvaKind.IDENTIFIER);
        eatToken(CvaKind.OPEN_PAREN);
        LinkedList<AbstractDeclaration> formalList = parseFormalList();
        eatToken(CvaKind.CLOSE_PAREN);
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        LinkedList<AbstractDeclaration> varDecs = parseVarDecls();
        LinkedList<AbstractStatement> stms = parseStatements();
        eatToken(CvaKind.RETURN);
        AbstractExpression retExp = parseExp();
        eatToken(CvaKind.SEMI);
        eatToken(CvaKind.CLOSE_CURLY_BRACE);

        return new CvaMethod(literal,
                retType, retExp, formalList, varDecs, stms);
    }

    // MethodDecls -> MethodDecl MethodDecls*
    //  ->
    private LinkedList<AbstractMethod> parseMethodDecls()
    {
        LinkedList<AbstractMethod> methods = new LinkedList<>();
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
    private AbstractClass parseClassDecl()
    {
        eatToken(CvaKind.CLASS);
        String id = curToken.getLiteral();
        eatToken(CvaKind.IDENTIFIER);
        String superClass = null;
        if (curToken.getKind() == CvaKind.COLON)
        {
            advance();
            superClass = curToken.getLiteral();
            eatToken(CvaKind.IDENTIFIER);
        }
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        LinkedList<AbstractDeclaration> decs = parseVarDecls();
        LinkedList<AbstractMethod> methods = parseMethodDecls();
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return new CvaClass(id, superClass, decs, methods);
    }

    // ClassDecls -> ClassDecl ClassDecls*
    //  ->
    private LinkedList<AbstractClass> parseClassDecls()
    {
        LinkedList<AbstractClass> classes = new LinkedList<>();
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
    private CvaEntry parseMainClass()
    {
        eatToken(CvaKind.CLASS);
        String literal = curToken.getLiteral();
        eatToken(CvaKind.IDENTIFIER);
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        eatToken(CvaKind.VOID);
        eatToken(CvaKind.MAIN);
        eatToken(CvaKind.OPEN_PAREN);
        eatToken(CvaKind.CLOSE_PAREN);
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        AbstractStatement stm = parseStatement();
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return new CvaEntry(literal, stm);
    }

    // Program -> MainClass ClassDecl*
    private CvaProgram parseProgram()
    {
        CvaEntry main = parseMainClass();
        List<AbstractClass> classes = parseClassDecls();
        eatToken(CvaKind.EOF);
        return new CvaProgram(main, classes);
    }

    public CvaProgram parse()
    {
        return parseProgram();
    }
}
