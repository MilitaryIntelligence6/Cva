package cn.misection.cvac.parser;

import cn.misection.cvac.ast.clas.AbstractClass;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.decl.CvaDeclaration;
import cn.misection.cvac.ast.entry.CvaEntry;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.AbstractMethod;
import cn.misection.cvac.ast.method.CvaMethod;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;
import cn.misection.cvac.constant.TokenConstPool;
import cn.misection.cvac.lexer.CvaKind;
import cn.misection.cvac.lexer.CvaToken;
import cn.misection.cvac.lexer.IBufferedQueue;
import cn.misection.cvac.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author MI6 root
 */
public final class Parser
{
    private final Lexer lexer;

    private CvaToken curToken;

    /**
     * for vardecl cn.misection.cvac.parser;
     */
    private boolean valDeclFlag;

    private boolean markingFlag;

    private final Queue<CvaToken> markedTokenQueue;


    public Parser(IBufferedQueue queueStream)
    {
        lexer = new Lexer(queueStream);
        curToken = lexer.nextToken();
        markingFlag = false;
        markedTokenQueue = new LinkedList<>();
    }

    public CvaProgram parse()
    {
        return parseProgram();
    }

    /**
     * utility methods;
     */
    private void advance()
    {
        if (markingFlag)
        {
            curToken = lexer.nextToken();
            markedTokenQueue.offer(curToken);
        }
        else if (!markedTokenQueue.isEmpty())
        {
            curToken = markedTokenQueue.poll();
        }
        else
        {
            curToken = lexer.nextToken();
        }
    }

    /**
     * start recording the tokens;
     */
    private void mark()
    {
        markingFlag = true;
        markedTokenQueue.offer(curToken);
    }

    /**
     * stop recording the tokens and clear recorded
     */
    private void deMark()
    {
        markingFlag = false;
        markedTokenQueue.clear();
    }

    /**
     * reset current token and stop recording
     */
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
            errorLog(String.valueOf(kind),
                    curToken.getKind());
        }
    }

    private void eatEof()
    {
        if (curToken.getKind() != CvaKind.EOF)
        {
            errorLog("end of file",
                    curToken);
        }
    }

    private void errorLog()
    {
        System.err.printf("Syntax error at line %s compilation aborting...\n%n",
                curToken != null ? curToken.getLineNum() : "unknown");
        System.exit(1);
    }

    private void errorLog(String expected, CvaToken got)
    {
        System.err.printf("Line %d: Expects: %s, but got: %s which literal is %s%n",
                curToken.getLineNum(), expected, got.getKind(), got.getLiteral());
        System.exit(1);
    }

    private void errorLog(String expected, CvaKind got)
    {
        System.err.printf("Line %d: Expects: %s, but got: %s%n",
                curToken.getLineNum(), expected, got);
        System.exit(1);
    }


    private void errorLog(String expected, String got)
    {
        System.err.printf("Line %d: Expects: %s, but got: %s%n",
                curToken.getLineNum(), expected, got);
        System.exit(1);
    }

    /**
     * // parse methods
     * <p>
     * // ExpList -> Exp ExpRest*
     * //         ->
     * // ExpRest -> , Exp
     *
     * @return
     */
    private LinkedList<AbstractExpression> parseExpList()
    {
        LinkedList<AbstractExpression> expList = new LinkedList<>();
        if (curToken.getKind() == CvaKind.CLOSE_PAREN)
        {
            return expList;
        }
        AbstractExpression tem = parseExpr();
        tem.setLineNum(curToken.getLineNum());
        expList.addLast(tem);
        while (curToken.getKind() == CvaKind.COMMA)
        {
            advance();
            tem = parseExpr();
            tem.setLineNum(curToken.getLineNum());
            expList.add(tem);
        }
        return expList;
    }

    /**
     * 原子操作层解析;
     * // AtomExpr -> (exp)
     * //  -> Integer Literal
     * //  -> true
     * //  -> false
     * //  -> this
     * //  -> id
     * //  -> new id()
     *
     * @return
     */
    private AbstractExpression parseAtomExpr()
    {
        AbstractExpression expr;
        switch (curToken.getKind())
        {
            case OPEN_PAREN:
            {
                advance();
                expr = parseExpr();
                expr.setLineNum(curToken.getLineNum());
                //advance();
                eatToken(CvaKind.CLOSE_PAREN);
                return expr;
            }
            case NUMBER:
            {
                expr = new CvaNumberIntExpr(curToken.getLineNum(), Integer.parseInt(curToken.getLiteral()));
                advance();
                return expr;
            }
            case STRING:
            {
                expr = new CvaStringExpr(curToken.getLineNum(), curToken.getLiteral());
                advance();
                return expr;
            }
            case TRUE:
            {
                expr = new CvaTrueExpr(curToken.getLineNum());
                advance();
                return expr;
            }
            case FALSE:
            {
                expr = new CvaFalseExpr(curToken.getLineNum());
                advance();
                return expr;
            }
            case THIS:
            {
                expr = new CvaThisExpr(curToken.getLineNum());
                advance();
                return expr;
            }
            case IDENTIFIER:
            {
                expr = new CvaIdentifierExpr(curToken.getLineNum(), curToken.getLiteral());
                advance();
                return expr;
            }
            case NEW:
            {
                advance();
                expr = new CvaNewExpr(curToken.getLineNum(), curToken.getLiteral());
                advance();
                eatToken(CvaKind.OPEN_PAREN);
                eatToken(CvaKind.CLOSE_PAREN);
                return expr;
            }
            default:
            {
                errorLog();
            }
        }
        return null;
    }

    /**
     * // NotExp -> AtomExp
     * //  -> AtomExp.id(expList)
     *
     * @return
     */
    private AbstractExpression parseNegateExpr()
    {
        AbstractExpression expr = parseAtomExpr();
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

    /**
     * // MulExpr -> ! MulExpr
     * //  -> NegateExpr
     *
     * @return
     */
    private AbstractExpression parseMulExpr()
    {
        int i = 0;
        while (curToken.getKind() == CvaKind.NEGATE)
        {
            advance();
            i++;
        }
        AbstractExpression expr = parseNegateExpr();
        AbstractExpression tem = new CvaNegateExpr(
                expr.getLineNum(), expr);
        return i % 2 == 0 ? expr : tem;
    }

    /**
     * // AddSubExp -> TimesExp * TimesExp
     * //  -> TimesExp
     *
     * @return
     */
    private AbstractExpression parseAddSubExpr()
    {
        AbstractExpression tem = parseMulExpr();
        AbstractExpression expr = tem;
        while (curToken.getKind() == CvaKind.STAR)
        {
            advance();
            tem = parseMulExpr();
            expr = new CvaMuliExpr(tem.getLineNum(), expr, tem);
        }
        return expr;
    }

    /**
     * // LtExp -> AddSubExp + AddSubExp
     * //  -> AddSubExp - AddSubExp
     * //  -> AddSubExp
     *
     * @return
     */
    private AbstractExpression parseLessThanExpr()
    {
        AbstractExpression expr = parseAddSubExpr();
        while (curToken.getKind() == CvaKind.ADD || curToken.getKind() == CvaKind.SUB)
        {
            boolean addFlag = curToken.getKind() == CvaKind.ADD;
            advance();
            AbstractExpression tem = parseAddSubExpr();
            expr = addFlag ?
                    new CvaAddExpr(expr.getLineNum(), expr, tem)
                    : tem instanceof CvaNumberIntExpr
                    ? new CvaAddExpr(tem.getLineNum(),
                    expr,
                    new CvaNumberIntExpr(tem.getLineNum(),
                            -((CvaNumberIntExpr) tem).getValue()))
                    : new CvaSubExpr(expr.getLineNum(), expr, tem);
        }
        return expr;
    }

    /**
     * // AndExp -> LtExp < LtExp
     * // -> LtExp
     *
     * @return
     */
    private AbstractExpression parseAndAndExpr()
    {
        AbstractExpression expr = parseLessThanExpr();
        while (curToken.getKind() == CvaKind.LESS_THAN)
        {
            advance();
            AbstractExpression tem = parseLessThanExpr();
            expr = new CvaLessThanExpr(expr.getLineNum(), expr, tem);
        }
        return expr;
    }

    /**
     * // Exp -> AndExp && AndExp
     * //  -> AndExp
     *
     * @return
     */
    private AbstractExpression parseExpr()
    {
        AbstractExpression expr = parseAndAndExpr();
        while (curToken.getKind() == CvaKind.AND_AND)
        {
            advance();
            AbstractExpression tem = parseAndAndExpr();
            expr = new CvaAndAndExpr(expr.getLineNum(), expr, tem);
        }
        return expr;
    }

    /**
     * // Statement -> { Statement* }
     * //  -> if (Exp) Statement else Statement
     * //  -> while (Exp) Statement
     * //  -> print(Exp);
     * //  -> id = Exp;
     *
     * @return
     */
    private AbstractStatement parseStatement()
    {
        switch (curToken.getKind())
        {
            case OPEN_CURLY_BRACE:
            {
                return handleOpenCurly();
            }
            case IF_STATEMENT:
            {
                return handleIf();
            }
            case WHILE_STATEMENT:
            {
                return handleWhile();
            }
            case WRITE:
            {
                return handleWriteOp();
            }
            case IDENTIFIER:
            {
                return handleIdentifier();
            }
            default:
            {
                errorLog();
            }
        }
        // 做成抛错;
        return null;
    }

    /**
     * // Statements -> Statement Statements
     * //  ->
     *
     * @return
     */
    private LinkedList<AbstractStatement> parseStatementList()
    {
        LinkedList<AbstractStatement> statementList = new LinkedList<>();
        while (true)
        {
            switch (curToken.getKind())
            {
                case OPEN_CURLY_BRACE:
                case IF_STATEMENT:
                case WHILE_STATEMENT:
                case IDENTIFIER:
                case WRITE:
                {
                    statementList.addLast(parseStatement());
                    continue;
                }
                default:
                {
                    break;
                }
            }
            // 走到这里就会break掉;
            break;
        }
        return statementList;
    }

    /**
     * // Type -> int
     * //  -> boolean
     * //  -> id
     *
     * @return
     */
    private AbstractType parseType()
    {
        AbstractType type = null;
        // 放map只能反射, 不放了还是;
        switch (curToken.getKind())
        {
            case BYTE:
            {
                type = new CvaBtyeType();
                break;
            }
            case CHAR:
            {
                type = new CvaCharType();
                break;
            }
            case SHORT:
            {
                type = new CvaShortType();
                break;
            }
            case INT:
            {
                type = new CvaIntType();
                break;
            }
            case LONG:
            {
                type = new CvaLongType();
            }
            case FLOAT:
            {
                type = new CvaFloatType();
                break;
            }
            case DOUBLE:
            {
                type = new CvaDoubleType();
                break;
            }
            case BOOLEAN:
            {
                type = new CvaBooleanType();
                break;
            }
            case STRING:
            {
                type = new CvaStringType();
                break;
            }
            case IDENTIFIER:
            {
                // 应该是type;
                type = new CvaClassType(curToken.getLiteral());
                break;
            }
            default:
            {
                errorLog("type",
                        curToken);
                // 不需要break打断虚拟机了已经;
            }
        }
        // 因为有advance所以不能直接return;
        advance();
        return type;
    }

    /**
     * // VarDecl -> Type id;
     *
     * @return
     */
    private AbstractDeclaration parseVarDecl()
    {
        this.mark();
        AbstractType type = parseType();
        // maybe a assign statement in method;
        if (curToken.getKind() == CvaKind.ASSIGN)
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
                this.deMark();
                valDeclFlag = true;
                AbstractDeclaration dec = new CvaDeclaration(curToken.getLineNum(), literal, type);
                eatToken(CvaKind.SEMI);
                return dec;
            }
            // maybe a method in class;
            else if (curToken.getKind() == CvaKind.OPEN_PAREN)
            {
                valDeclFlag = false;
                this.reset();
                return null;
            }
            else
            {
                errorLog();
                return null;
            }
        }
        else
        {
            errorLog();
            return null;
        }
    }

    /**
     * // VarDecls -> VarDecl VarDecls
     * //  ->
     *
     * @return
     */
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

    /**
     * // FormalList -> Type id FormalRest*
     * //  ->
     * // FormalRest -> , Type id
     *
     * @return
     */
    private LinkedList<AbstractDeclaration> parseFormalList()
    {
        LinkedList<AbstractDeclaration> declList = new LinkedList<>();
        if (CvaKind.isType(curToken.getKind()))
        {
            // 这里非常坑. 必须要先parser;
            // parse的副作用是推一个token, 所以给new decl传参的时候先后顺序换了会导致意想不到的bug;
            // 保存上一个token的type, 拿取下一个token的literal;
            AbstractType type = parseType();
            declList.addLast(new CvaDeclaration(curToken.getLineNum(), curToken.getLiteral(), type));
            eatToken(CvaKind.IDENTIFIER);
            while (curToken.getKind() == CvaKind.COMMA)
            {
                advance();
                AbstractType argType = parseType();
                declList.addLast(new CvaDeclaration(curToken.getLineNum(), curToken.getLiteral(), argType));
                eatToken(CvaKind.IDENTIFIER);
            }
        }
        else
        {
            errorLog("type in func formal args list",
                    curToken.getKind());
        }
        return declList;
    }

    /**
     * // Method -> Type id (FormalList)
     * //          {VarDec* Statement* return Exp; }
     *
     * @return
     */
    private AbstractMethod parseMethod()
    {
        // 第一个是返回值;
        AbstractType retType = parseType();
        // 解析函数名;
        String literal = curToken.getLiteral();
        // 吃掉函数名和开小括号;
        eatToken(CvaKind.IDENTIFIER);
        eatToken(CvaKind.OPEN_PAREN);
        // 解析形参List;
        List<AbstractDeclaration> formalList = parseFormalList();
        // 解析完毕吃掉小括号;
        eatToken(CvaKind.CLOSE_PAREN);
        // 吃掉大括号;
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        List<AbstractDeclaration> varDecs = parseVarDecls();
        List<AbstractStatement> statementList = parseStatementList();
        eatToken(CvaKind.RETURN);
        AbstractExpression retExp = parseExpr();
        eatToken(CvaKind.SEMI);
        eatToken(CvaKind.CLOSE_CURLY_BRACE);

        return new CvaMethod(
                literal,
                retType,
                retExp,
                formalList,
                varDecs,
                statementList);
    }

    /**
     * // MethodDecls -> MethodDecl MethodDecls*
     * //  ->
     *
     * @return
     */
    private LinkedList<AbstractMethod> parseMethodDecls()
    {
        LinkedList<AbstractMethod> methodList = new LinkedList<>();
        while (curToken.getKind() == CvaKind.IDENTIFIER ||
                curToken.getKind() == CvaKind.INT ||
                curToken.getKind() == CvaKind.BOOLEAN)
        {
            methodList.addLast(parseMethod());
        }

        return methodList;
    }

    /**
     * // ClassDecl -> class id { VarDecl* MethodDecl* }
     * //  -> class id : id { VarDecl* Method* }
     *
     * @return
     */
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
        LinkedList<AbstractDeclaration> decs = parseVarDecls();
        LinkedList<AbstractMethod> methods = parseMethodDecls();
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return new CvaClass(literal, superClass, decs, methods);
    }

    /**
     * // ClassDecls -> ClassDecl ClassDecls*
     * //  ->
     *
     * @return
     */
    private LinkedList<AbstractClass> parseClassDecls()
    {
        LinkedList<AbstractClass> classList = new LinkedList<>();
        while (curToken.getKind() == CvaKind.CLASS)
        {
            classList.addLast(parseClassDecl());
        }
        return classList;
    }

    /**
     * // MainClass -> class id
     * //    {
     * //        void main()
     * //        {
     * //            Statement
     * //        }
     * //    }
     *
     * @return
     */
    private CvaEntry parseEntry()
    {
        switch (curToken.getKind())
        {
            case CLASS:
            {
                eatToken(CvaKind.CLASS);
                String entryName = curToken.getLiteral();
                eatToken(CvaKind.IDENTIFIER);
                eatToken(CvaKind.OPEN_CURLY_BRACE);
                AbstractStatement statement = parseMainMethod();
                eatToken(CvaKind.CLOSE_CURLY_BRACE);
                return new CvaEntry(entryName, statement);
            }
            default:
            {
                String mainName = TokenConstPool.DEFAULT_MAIN_NAME;
                AbstractStatement statement = parseMainMethod();
                return new CvaEntry(mainName, statement);
            }
        }
    }

    /**
     * Program -> MainClass ClassDecl*
     *
     * @return
     */
    private CvaProgram parseProgram()
    {
        parsePackage();
        parseCallStatement();
        CvaEntry entry = parseEntry();
        List<AbstractClass> classList = parseClassDecls();
        eatEof();
        return new CvaProgram(entry, classList);
    }

    /**
     * @TODO 目前是eat, 以后要检查返回值;
     */
    private CvaKind parseMainRetType()
    {
        // kind 能保存之前的kind;
        CvaKind curKind = curToken.getKind();
        if (CvaKind.isType(curKind))
        {
            eatToken(curKind);
        }
        else
        {
            errorLog("type in main func decl",
                    curKind);
        }
        return curKind;
    }

    private AbstractStatement parseMainMethod()
    {
        // TODO 现在没用到. 以后要检查;
        CvaKind mainRet = parseMainRetType();

        eatToken(CvaKind.MAIN);
        eatToken(CvaKind.OPEN_PAREN);

        parseMainArgs();

        eatToken(CvaKind.CLOSE_PAREN);
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        // 拓展到可以吃多个statement;
        AbstractStatement statement = parseStatement();
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return statement;
    }

    /**
     * @TODO 目前是eat, 以后要传入;
     * 应返回参数List;
     */
    private AbstractDeclaration parseMainArgs()
    {
        AbstractDeclaration cmdArgsDecl = null;
        if (CvaKind.isType(curToken.getKind()))
        {
            // 这里非常坑. 必须要先parser;
            // parse的副作用是推一个token, 所以给new decl传参的时候先后顺序换了会导致意想不到的bug;
            AbstractType type = parseType();
            if (!(type instanceof CvaStringType))
            {
                errorLog("Sting[] args in main func",
                        String.valueOf(type));
            }
            eatToken(CvaKind.OPEN_BRACKETS);
            eatToken(CvaKind.CLOSE_BRACKETS);
            cmdArgsDecl = new CvaDeclaration(curToken.getLineNum(), curToken.getLiteral(), type);
            eatToken(CvaKind.IDENTIFIER);
        }
        else
        {
            errorLog("String[] in main formal args list",
                    curToken.getKind());
        }
        return cmdArgsDecl;
    }

    private void parsePackage()
    {
        if (curToken.getKind() == CvaKind.PACKAGE)
        {
            eatToken(CvaKind.PACKAGE);
            CvaKind memKind = curToken.getKind();
            eatToken(CvaKind.IDENTIFIER);
            while (true)
            {
                switch (curToken.getKind())
                {
                    case DOT:
                    {
                        if (memKind != CvaKind.IDENTIFIER)
                        {
                            errorLog();
                        }
                        memKind = CvaKind.DOT;
                        eatToken(CvaKind.DOT);
                        continue;
                    }
                    case IDENTIFIER:
                    {
                        if (memKind != CvaKind.DOT)
                        {
                            errorLog();
                        }
                        memKind = CvaKind.IDENTIFIER;
                        eatToken(CvaKind.IDENTIFIER);
                        continue;
                    }
                    case SEMI:
                    {
                        eatToken(CvaKind.SEMI);
                        break;
                    }
                    default:
                    {
                        errorLog("pkg name or dot or star",
                                curToken);
                        break;
                    }
                }
                break;
            }
        }
    }

    private void parseCallStatement()
    {
        // call 是多条, 所以在这里用;
        while (curToken.getKind() == CvaKind.CALL)
        {
            parseSingleCall();
        }
    }

    private void parseSingleCall()
    {
        eatToken(CvaKind.CALL);
        // 规定至少一个pkg., 因为本包内不需要call;
        // 第一个必为 id;
        CvaKind memKind = curToken.getKind();
        eatToken(CvaKind.IDENTIFIER);
        while (true)
        {
            switch (curToken.getKind())
            {
                case DOT:
                {
                    if (memKind != CvaKind.IDENTIFIER)
                    {
                        errorLog();
                    }
                    memKind = CvaKind.DOT;
                    eatToken(CvaKind.DOT);
                    continue;
                }
                case IDENTIFIER:
                {
                    if (memKind != CvaKind.DOT)
                    {
                        errorLog();
                    }
                    memKind = CvaKind.IDENTIFIER;
                    eatToken(CvaKind.IDENTIFIER);
                    continue;
                }
                case STAR:
                {
                    if (memKind != CvaKind.DOT)
                    {
                        errorLog();
                    }
                    eatToken(CvaKind.STAR);
                    eatToken(CvaKind.SEMI);
                    break;
                }
                case SEMI:
                {
                    eatToken(CvaKind.SEMI);
                    break;
                }
                default:
                {
                    errorLog("pkg name or dot or star",
                            curToken);
                    break;
                }
            }
            break;
        }
    }

    private AbstractStatement handleWriteOp()
    {
        // 目前 echo expr 实现还稍麻烦, 后面再想法;
        int lineNum = curToken.getLineNum();
        eatToken(CvaKind.WRITE);
        // 解析多种write;
//        switch (curToken.getKind())
//        {
//
//        }
        eatToken(CvaKind.OPEN_PAREN);
        AbstractExpression expr = parseExpr();
        eatToken(CvaKind.CLOSE_PAREN);
        eatToken(CvaKind.SEMI);
        return new CvaWriteOperation(lineNum, expr);
    }

    /**
     * @return
     * @TODO 嵌套判定有无else之类;
     */
    private AbstractStatement handleIf()
    {
        int lineNum = curToken.getLineNum();
        eatToken(CvaKind.IF_STATEMENT);
        eatToken(CvaKind.OPEN_PAREN);
        AbstractExpression condition = parseExpr();
        eatToken(CvaKind.CLOSE_PAREN);
        AbstractStatement thenStm = parseStatement();
        if (curToken.getKind() == CvaKind.ELSE_STATEMENT)
        {
            AbstractStatement elseStm = handleElse();
            return new CvaIfStatement(lineNum, condition, thenStm, elseStm);
        }
        return new CvaIfStatement(lineNum, condition, thenStm);
    }

    private AbstractStatement handleElse()
    {
        eatToken(CvaKind.ELSE_STATEMENT);
        return parseStatement();
    }

    private AbstractStatement handleWhile()
    {
        int lineNum = curToken.getLineNum();
        eatToken(CvaKind.WHILE_STATEMENT);
        eatToken(CvaKind.OPEN_PAREN);
        AbstractExpression condition = parseExpr();
        eatToken(CvaKind.CLOSE_PAREN);
        AbstractStatement body = parseStatement();
        return new CvaWhileStatement(lineNum, condition, body);
    }

    private AbstractStatement handleIdentifier()
    {
        String literal = curToken.getLiteral();
        int lineNum = curToken.getLineNum();
        eatToken(CvaKind.IDENTIFIER);
        eatToken(CvaKind.ASSIGN);
        AbstractExpression expr = parseExpr();
        eatToken(CvaKind.SEMI);
        return new CvaAssign(lineNum, literal, expr);
    }

    private AbstractStatement handleOpenCurly()
    {
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        int lineNum = curToken.getLineNum();
        AbstractStatement statement = new CvaBlock(lineNum, parseStatementList());
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return statement;
    }
}
