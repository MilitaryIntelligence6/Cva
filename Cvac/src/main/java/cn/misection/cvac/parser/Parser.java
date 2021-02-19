package cn.misection.cvac.parser;

import cn.misection.cvac.ast.clas.AbstractCvaClass;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.AbstractDeclaration;
import cn.misection.cvac.ast.decl.CvaDeclaration;
import cn.misection.cvac.ast.entry.CvaEntryClass;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.AbstractMethod;
import cn.misection.cvac.ast.method.CvaMainMethod;
import cn.misection.cvac.ast.method.CvaMethod;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.AbstractType;
import cn.misection.cvac.ast.type.basic.*;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.reference.CvaStringType;
import cn.misection.cvac.constant.LexerConst;
import cn.misection.cvac.constant.WriteILConst;
import cn.misection.cvac.io.IBufferedQueue;
import cn.misection.cvac.lexer.CvaKind;
import cn.misection.cvac.lexer.CvaToken;
import cn.misection.cvac.lexer.Lexer;

import java.util.ArrayList;
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

    private boolean hasEntry;

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
     * @return Exprlist;
     */
    private List<AbstractExpression> parseExprList()
    {
        List<AbstractExpression> expList = new ArrayList<>();
        if (curToken.getKind() == CvaKind.CLOSE_PAREN)
        {
            return expList;
        }
        AbstractExpression tem = parseExpr();
        tem.setLineNum(curToken.getLineNum());
        expList.add(tem);
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
     * @return atom expr;
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
     * @return negateExpr
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
                    parseExprList()
            );
            eatToken(CvaKind.CLOSE_PAREN);
        }
        return expr;
    }

    /**
     * // MulExpr -> ! MulExpr
     * //  -> NegateExpr
     *
     * @return MulExpr
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
     * @return AddSubExpr
     */
    private AbstractExpression parseAddSubExpr()
    {
        AbstractExpression tem = parseMulExpr();
        AbstractExpression expr = tem;
        while (curToken.getKind() == CvaKind.STAR)
        {
            advance();
            tem = parseMulExpr();
            expr = new CvaMulExpr(tem.getLineNum(), expr, tem);
        }
        return expr;
    }

    /**
     * // LtExp -> AddSubExp + AddSubExp
     * //  -> AddSubExp - AddSubExp
     * //  -> AddSubExp
     *
     * @return LessThanExpr
     */
    private AbstractExpression parseLessThanExpr()
    {
        AbstractExpression expr = parseAddSubExpr();
        while (curToken.getKind() == CvaKind.ADD || curToken.getKind() == CvaKind.SUB)
        {
            boolean addFlag = curToken.getKind() == CvaKind.ADD;
            advance();
            AbstractExpression tem = parseAddSubExpr();
            if (addFlag)
            {
                expr = new CvaAddExpr(expr.getLineNum(), expr, tem);
            }
            else
            {
                if (tem instanceof CvaNumberIntExpr)
                {
                    expr = new CvaAddExpr(
                            tem.getLineNum(),
                            expr,
                            new CvaNumberIntExpr(tem.getLineNum(),
                                    -((CvaNumberIntExpr) tem).getValue()));
                }
                else
                {
                    expr = new CvaSubExpr(expr.getLineNum(), expr, tem);
                }
            }
        }
        return expr;
    }

    /**
     * // AndExp -> LtExp < LtExp
     * // -> LtExp
     *
     * @return AndAndExpr;
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
     * @return Single Expr
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
     * @return single Statement;
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
                return handleWriteOp(WriteILConst.CONSOLE_WRITE);
            }
            case WRITE_LINE:
            {
                return handleWriteOp(WriteILConst.CONSOLE_WRITELN);
            }
            case WRITE_FORMAT:
            {
                return handleWriteOp(WriteILConst.CONSOLE_WRITE_FORMAT);
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
     * @return StatementList;
     */
    private List<AbstractStatement> parseStatementList()
    {
        List<AbstractStatement> statementList = new ArrayList<>();
        while (true)
        {
            switch (curToken.getKind())
            {
                case OPEN_CURLY_BRACE:
                case IF_STATEMENT:
                case WHILE_STATEMENT:
                case IDENTIFIER:
                case WRITE:
                    // TODO 不优雅, 想办法改;
                case WRITE_LINE:
                case WRITE_FORMAT:
                {
                    statementList.add(parseStatement());
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
     * @return Type;
     */
    private AbstractType parseType()
    {
        AbstractType type = null;
        // 放map只能反射, 不放了还是;
        switch (curToken.getKind())
        {
            case VOID:
            {
                type = new CvaVoidType();
                break;
            }
            case BYTE:
            {
                type = new CvaByteType();
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
     * @return VarDecl;
     */
    private AbstractDeclaration parseVarDecl()
    {
        this.mark();
        AbstractType type = parseType();
        // maybe a assign statement in method;
        switch (curToken.getKind())
        {
            case ASSIGN:
            {
                this.reset();
                valDeclFlag = false;
                return null;
            }
            case IDENTIFIER:
            {
                String literal = curToken.getLiteral();
                advance();
                switch (curToken.getKind())
                {
                    case SEMI:
                    {
                        this.deMark();
                        valDeclFlag = true;
                        AbstractDeclaration decl = new CvaDeclaration(curToken.getLineNum(), literal, type);
                        eatToken(CvaKind.SEMI);
                        return decl;
                    }
                    // maybe a method in class;
                    case OPEN_PAREN:
                    {
                        valDeclFlag = false;
                        this.reset();
                        return null;
                    }
                    default:
                    {
                        errorLog();
                        return null;
                    }
                }
            }
            default:
            {
                errorLog();
                return null;
            }
        }
    }

    /**
     * // VarDecls -> VarDecl VarDecls
     * //  ->
     *
     * @return
     * @FIXME while拆了;
     */
    private List<AbstractDeclaration> parseVarDeclList()
    {
        List<AbstractDeclaration> declList = new ArrayList<>();
        valDeclFlag = true;
        while (CvaKind.isType(curToken.getKind())
                || curToken.getKind() == CvaKind.IDENTIFIER)
        {
            AbstractDeclaration decl = parseVarDecl();
            if (decl != null)
            {
                declList.add(decl);
            }
            if (!valDeclFlag)
            {
                break;
            }
        }
        return declList;
    }

    /**
     * // FormalList -> Type id FormalRest*
     * //  ->
     * // FormalRest -> , Type id
     *
     * @return
     */
    private List<AbstractDeclaration> parseFormalList()
    {
        List<AbstractDeclaration> declList = new ArrayList<>();
        if (CvaKind.isType(curToken.getKind()))
        {
            // 这里非常坑. 必须要先parser;
            // parse的副作用是推一个token, 所以给new decl传参的时候先后顺序换了会导致意想不到的bug;
            // 保存上一个token的type, 拿取下一个token的literal;
            AbstractType type = parseType();
            declList.add(new CvaDeclaration(curToken.getLineNum(), curToken.getLiteral(), type));
            eatToken(CvaKind.IDENTIFIER);
            while (curToken.getKind() == CvaKind.COMMA)
            {
                advance();
                AbstractType argType = parseType();
                declList.add(new CvaDeclaration(curToken.getLineNum(), curToken.getLiteral(), argType));
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
        List<AbstractDeclaration> localVarDecls = parseVarDeclList();
        List<AbstractStatement> statementList = parseStatementList();

        // FIXME 隐患;
        AbstractExpression retExpr = null;
        if (!(retType instanceof CvaVoidType))
        {
            eatToken(CvaKind.RETURN);
            retExpr = parseExpr();
            eatToken(CvaKind.SEMI);
        }
        eatToken(CvaKind.CLOSE_CURLY_BRACE);

        return new CvaMethod(
                literal,
                retType,
                retExpr,
                formalList,
                localVarDecls,
                statementList);
    }

    private AbstractMethod parseMainMethod()
    {
        AbstractType mainRetType = parseType();
        String literal = curToken.getLiteral();
        eatToken(CvaKind.MAIN);

        eatToken(CvaKind.OPEN_PAREN);
        List<AbstractDeclaration> mainArgs = parseMainArgs();
        eatToken(CvaKind.CLOSE_PAREN);

        eatToken(CvaKind.OPEN_CURLY_BRACE);
        List<AbstractDeclaration> localVarDecls = parseVarDeclList();
        List<AbstractStatement> statementList = parseStatementList();

        AbstractExpression retExpr = null;
        if (!(mainRetType instanceof CvaVoidType))
        {
            eatToken(CvaKind.RETURN);
            retExpr = parseExpr();
            eatToken(CvaKind.SEMI);
        }
        eatToken(CvaKind.CLOSE_CURLY_BRACE);

        return new CvaMainMethod.Builder()
                .putRetType(mainRetType)
                .putRetExpr(retExpr)
                .putMainArgList(mainArgs)
                .putLocalVarList(localVarDecls)
                .putStatementList(statementList)
                .build();
    }

    /**
     * // MethodDecls -> MethodDecl MethodDecls*
     * //  ->
     *
     * @return
     */
    private List<AbstractMethod> parseMethodDeclList()
    {
        List<AbstractMethod> methodList = new ArrayList<>();

        while (CvaKind.isType(curToken.getKind())
                || curToken.getKind() == CvaKind.IDENTIFIER)
        {
            methodList.add(parseMethod());
        }
        return methodList;
    }

    /**
     * // ClassDecl -> class id { VarDecl* MethodDecl* }
     * //  -> class id : id { VarDecl* Method* }
     *
     * @return
     */
    private AbstractCvaClass parseClassDecl()
    {
        eatToken(CvaKind.CLASS_DECL);
        String literal = curToken.getLiteral();
        eatToken(CvaKind.IDENTIFIER);
        String superClass = null;
        if (curToken.getKind() == CvaKind.EXTENDS)
        {
            advance();
            superClass = curToken.getLiteral();
            eatToken(CvaKind.IDENTIFIER);
        }
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        List<AbstractDeclaration> declList = parseVarDeclList();
        List<AbstractMethod> methodList = parseMethodDeclList();
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return new CvaClass(
                literal,
                superClass,
                declList,
                methodList);
    }

    /**
     * // ClassDecls -> ClassDecl ClassDecls*
     * //  ->
     *
     * @return
     */
    private List<AbstractCvaClass> parseClassDeclList()
    {
        List<AbstractCvaClass> classList = new ArrayList<>();
        while (curToken.getKind() == CvaKind.CLASS_DECL)
        {
            classList.add(parseClassDecl());
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
    private CvaEntryClass parseEntryClass()
    {
        if (curToken.getKind() == CvaKind.CLASS_DECL)
        {
            eatToken(CvaKind.CLASS_DECL);
            String entryName = curToken.getLiteral();
            eatToken(CvaKind.IDENTIFIER);
            eatToken(CvaKind.OPEN_CURLY_BRACE);
//            AbstractStatement statement = parseMainMethod();
            AbstractMethod mainMethod = parseMainMethod();

            eatToken(CvaKind.CLOSE_CURLY_BRACE);
//            return new CvaEntryClass(entryName, statement);
            return new CvaEntryClass.Builder()
                    .putName(entryName)
                    .putEntryMethod(mainMethod)
                    .build();
        }
        String mainName = LexerConst.DEFAULT_MAIN_CLASS_NAME;
//        AbstractStatement statement = parseMainMethod();
        AbstractMethod mainMethod = parseMainMethod();
        return new CvaEntryClass.Builder()
                .putName(mainName)
                .putEntryMethod(mainMethod)
                .build();
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
        CvaEntryClass entryClass = parseEntryClass();
        List<AbstractCvaClass> classList = parseClassDeclList();
//        CvaEntry entry = null;
//        List<AbstractClass> classList = new ArrayList<>();
//        while (true)
//        {
//            CvaKind curKind = curToken.getKind();
//            if (curKind == CvaKind.CLASS)
//            {
//                classList.addAll(parseClassDeclList());
//            }
//            else if (CvaKind.isType(curKind))
//            {
//                entry = parseEntry();
//                hasEntry = true;
//            }
//            else if (curKind == CvaKind.EOF)
//            {
//                break;
//            }
//            else
//            {
//                errorLog("EOF or class def or main func def" +
//                                "(cva only supported main func out the class) ",
//                        curKind);
//            }
//        }
//        if (!hasEntry)
//        {
//            for (AbstractClass absClass : classList)
//            {
//                for (AbstractMethod absMethod : absClass.methodList())
//                {
//                    // null 安全的equals;
//                    if (Objects.equals(absMethod.name(), CvaKind.MAIN.getKindLiteral()))
//                    {
//                        entry =
//                    }
//                }
//            }
//        }
        eatEof();
        // find entry;
        return new CvaProgram(entryClass, classList);
    }

    /**
     * @TODO 目前是eat, 以后要传入;
     * 应返回参数List;
     */
    private List<AbstractDeclaration> parseMainArgs()
    {
        // 保持统一用list;
        List<AbstractDeclaration> cmdArgsDeclList = new ArrayList<>();
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
            cmdArgsDeclList.add(
                    new CvaDeclaration(
                            curToken.getLineNum(),
                            curToken.getLiteral(),
                            type));
            eatToken(CvaKind.IDENTIFIER);
        }
        else
        {
            errorLog("String[] in main formal args list",
                    curToken.getKind());
        }
        return cmdArgsDeclList;
    }

    private void parsePackage()
    {
        if (curToken.getKind() == CvaKind.PACKAGE_DECL)
        {
            eatToken(CvaKind.PACKAGE_DECL);
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
            parseCallSentence();
        }
    }

    private void parseCallSentence()
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

    private AbstractStatement handleWriteOp(byte writeMode)
    {
        // 目前 echo expr 实现还稍麻烦, 后面再想法;
        int lineNum = curToken.getLineNum();
        // 一定是write;
        eatToken(curToken.getKind());
        // TODO 解析不带括号的echo;
        eatToken(CvaKind.OPEN_PAREN);
        AbstractExpression expr = parseExpr();
        eatToken(CvaKind.CLOSE_PAREN);
        eatToken(CvaKind.SEMI);
        return new CvaWriteStatement(lineNum, expr, writeMode);
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
        return new CvaAssignStatement(lineNum, literal, expr);
    }

    private AbstractStatement handleOpenCurly()
    {
        eatToken(CvaKind.OPEN_CURLY_BRACE);
        int lineNum = curToken.getLineNum();
        AbstractStatement statement = new CvaBlockStatement(lineNum, parseStatementList());
        eatToken(CvaKind.CLOSE_CURLY_BRACE);
        return statement;
    }
}
