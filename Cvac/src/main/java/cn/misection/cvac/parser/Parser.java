package cn.misection.cvac.parser;

import cn.misection.cvac.ast.clas.AbstractClass;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.decl.CvaDeclaration;
import cn.misection.cvac.ast.entry.CvaEntry;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.AbstractMethod;
import cn.misection.cvac.ast.method.CvaMethod;
import cn.misection.cvac.ast.program.AbstractProgram;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.AbstractType;
import cn.misection.cvac.ast.type.CvaBoolean;
import cn.misection.cvac.ast.type.CvaClassType;
import cn.misection.cvac.ast.type.CvaInt;
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
//        if (kind == curToken.getKind())// || kind == CvaKind.EOF)
        if (kind == curToken.getKind())
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
    private List<AbstractExpression> parseExpList()
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
                exp = new CvaNumberInt(Integer.parseInt(curToken.getLiteral()),
                        curToken.getLineNum());
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
                exp = new CvaIdentifier(curToken.getLineNum(),
                        curToken.getLiteral());
                advance();
                return exp;
            case NEW:
                advance();
                exp = new CvaNewExpr(curToken.getLineNum(),
                        curToken.getLiteral());
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
        AbstractExpression callExpr = parseAtomExp();
        while (curToken.getKind() == CvaKind.DOT)
        {
            advance();
            CvaToken token = curToken;
            eatToken(CvaKind.IDENTIFIER);
            eatToken(CvaKind.OPEN_PAREN);
            callExpr = new CvaCallExpr(token.getLineNum(),
                    token.getLiteral(),
                    callExpr,
                    parseExpList()
            );
            eatToken(CvaKind.CLOSE_PAREN);
        }
        return callExpr;
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
        AbstractExpression tem = new CvaNegateExpr(exp.getLineNum(),
                exp);
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
            boolean isAdd = curToken.getKind() == CvaKind.ADD;
            advance();
            AbstractExpression tem = parseAddSubExp();
            // 把写的抽象的三元重构成 if else;
            if (isAdd)
            {
                exp = new CvaAddExpr(exp.getLineNum(), exp, tem);
            }
            else
            {
                if (tem instanceof CvaNumberInt)
                {
                    exp = new CvaAddExpr(
                            tem.getLineNum(),
                            exp,
                            new CvaNumberInt(
                                    tem.getLineNum(),
                                    -((CvaNumberInt) tem).getValue()
                            ));
                }
                else
                {
                    exp = new CvaSubExpr(exp.getLineNum(), exp, tem);
                }
            }
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
    private List<AbstractStatement> parseStatements()
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
        switch (curToken.getKind())
        {
            case BOOLEAN:
                type = new CvaBoolean();
                advance();
                break;
            case INT:
                type = new CvaInt();
                advance();
                break;
            case IDENTIFIER:
                type = new CvaClassType(curToken.getLiteral());
                advance();
                break;
            default:
                error();
                break;
        }
        return type;
    }

    // VarDecl -> Type id;
    private AbstractDeclaration parseVarDecl()
    {
        this.mark();
        AbstractType type = parseType();
        switch (curToken.getKind())
        {
            case ASSIGN:
// maybe a assign statement in method

                this.reset();
                valDeclFlag = false;
                return null;
            case IDENTIFIER:
                String literal = curToken.getLiteral();
                advance();
                switch (curToken.getKind())
                {
                    case SEMI:
                        this.unMark();
                        valDeclFlag = true;
                        AbstractDeclaration dec = new CvaDeclaration(
                                curToken.getLineNum(), literal, type);
                        eatToken(CvaKind.SEMI);
                        return dec;
                    case OPEN_PAREN:
// maybe a method in class
                        // 如果是开小括号, 则是在定义args模式;
                        valDeclFlag = false;
                        this.reset();
                        return null;
                    default:
                        error();
                        return null;
                }
            default:
                error();
                return null;
        }
    }

    // VarDecls -> VarDecl VarDecls
    //  ->
    private List<AbstractDeclaration> parseVarDecls()
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
    private List<AbstractDeclaration> parseFormalList()
    {
        LinkedList<AbstractDeclaration> decs = new LinkedList<>();
        switch (curToken.getKind())
        {
            case INT:
            case BOOLEAN:
            case IDENTIFIER:
            {
                // !!!FIXME
                // 终于tm知道了, parseType有副作用, 所以new这个decl的时候非常讲究, 必须先parse, 才能得到正确的literal!!
                // 最好是把每一步分离开, 这副作用太恶心了!;
                // 这个有副作用, get和set是没有副作用的!;
                AbstractType type = parseType();
                decs.addLast(new CvaDeclaration(
                        curToken.getLineNum(), curToken.getLiteral(), type));
                eatToken(CvaKind.IDENTIFIER);
                while (curToken.getKind() == CvaKind.COMMA)
                {
                    advance();
                    decs.addLast(new CvaDeclaration(
                            curToken.getLineNum(), curToken.getLiteral(), parseType())
                    );
                    eatToken(CvaKind.IDENTIFIER);
                }
                break;
            }
            default:
            {
                System.err.printf("the type %s no support!\n", curToken.getKind());
                break;
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
        List<AbstractDeclaration> formalList = parseFormalList();
        eatToken(CvaKind.CLOSE_PAREN);
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        List<AbstractDeclaration> varDecs = parseVarDecls();
        List<AbstractStatement> stms = parseStatements();
        eatToken(CvaKind.RETURN);
        AbstractExpression retExp = parseExp();
        eatToken(CvaKind.SEMI);
        eatToken(CvaKind.CLOSE_CURLY_BRACE);

        return new CvaMethod(
                literal,
                retType,
                retExp,
                formalList,
                varDecs,
                stms
        );
    }

    // MethodDecls -> MethodDecl MethodDecls*
    //  ->
    private List<AbstractMethod> parseMethodDecls()
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
        String literal = curToken.getLiteral();
        eatToken(CvaKind.IDENTIFIER);
        String superClass = null;
        if (curToken.getKind() == CvaKind.COLON)
        {
            advance();
            superClass = curToken.getLiteral();
            eatToken(CvaKind.IDENTIFIER);
        }
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        List<AbstractDeclaration> decs = parseVarDecls();
        List<AbstractMethod> methods = parseMethodDecls();
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return new CvaClass(literal, superClass, decs, methods);
    }

    // ClassDecls -> ClassDecl ClassDecls*
    //  ->
    private List<AbstractClass> parseClassDecls()
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

    public AbstractProgram parse()
    {
        return parseProgram();
    }
}
