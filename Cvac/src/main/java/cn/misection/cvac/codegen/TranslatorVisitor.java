package cn.misection.cvac.codegen;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.CvaClass;
import cn.misection.cvac.ast.decl.CvaDeclaration;
import cn.misection.cvac.ast.entry.CvaEntryClass;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.CvaMainMethod;
import cn.misection.cvac.ast.method.CvaMethod;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.AbstractType;
import cn.misection.cvac.ast.type.basic.CvaBooleanType;
import cn.misection.cvac.ast.type.basic.CvaIntType;
import cn.misection.cvac.ast.type.reference.AbstractReferenceType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.reference.CvaStringType;
import cn.misection.cvac.codegen.bst.Label;
import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.bentry.GenEntry;
import cn.misection.cvac.codegen.bst.bmethod.GenMethod;
import cn.misection.cvac.codegen.bst.bprogram.GenProgram;
import cn.misection.cvac.codegen.bst.btype.BaseType;
import cn.misection.cvac.codegen.bst.btype.basic.GenIntType;
import cn.misection.cvac.codegen.bst.btype.reference.GenClassType;
import cn.misection.cvac.codegen.bst.btype.reference.GenStringType;
import cn.misection.cvac.codegen.bst.instruction.*;
import cn.misection.cvac.constant.CvaExprClassName;
import cn.misection.cvac.constant.WriteILConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MI6 root;
 * @Description 将编译器前端翻译给后端;
 */
public final class TranslatorVisitor implements IVisitor
{
    private String className;
    private int index;

    private Map<String, Integer> indexTable;

    private BaseType genType;
    private GenDeclaration genDecl;
    private List<BaseInstruction> linearInstrList;
    private GenMethod genMethod;
    private GenClass genClass;
    private GenEntry genEntry;
    private GenProgram genProgram;

    public TranslatorVisitor()
    {
        this.className = null;
        this.indexTable = null;
        this.genType = null;
        this.genDecl = null;
        this.linearInstrList = new ArrayList<>();
        this.genMethod = null;
        this.genEntry = null;
        this.genClass = null;
        this.genProgram = null;
    }

    private void emit(BaseInstruction s)
    {
        linearInstrList.add(s);
    }

    @Override
    public void visit(CvaBooleanType type)
    {
        genType = new GenIntType();
    }

    @Override
    public void visit(CvaClassType type)
    {
        genType = new GenClassType(type.getLiteral());
    }

    @Override
    public void visit(CvaIntType type)
    {
        genType = new GenIntType();
    }

    @Override
    public void visit(CvaStringType type)
    {
        genType = new GenStringType();
    }

    @Override
    public void visit(CvaDeclaration decl)
    {
        visit(decl.type());
        genDecl = new GenDeclaration(
                decl.literal(),
                this.genType);
        if (indexTable != null) // if it is field
        {
            indexTable.put(decl.literal(), index++);
        }
    }

    @Override
    public void visit(CvaAddExpr e)
    {
        visit(e.getLeft());
        visit(e.getRight());
        emit(new IAdd());
    }

    @Override
    public void visit(CvaAndAndExpr expr)
    {
        Label f = new Label();
        Label r = new Label();
        visit(expr.getLeft());
        emit(new Ldc<Integer>(1));
        emit(new Ificmplt(f));
        visit(expr.getRight());
        emit(new Ldc<Integer>(1));
        emit(new Ificmplt(f));
        emit(new Ldc<Integer>(1));
        emit(new Goto(r));
        emit(new LabelJ(f));
        emit(new Ldc<Integer>(0));
        emit(new LabelJ(r));
    }

    @Override
    public void visit(CvaCallExpr e)
    {
        visit(e.getExpr());
        e.getArgs().forEach(this::visit);
        visit(e.getRetType());
        BaseType rt = this.genType;
        List<BaseType> at = new ArrayList<>();
        e.getArgTypeList().forEach(a ->
        {
            visit(a);
            at.add(this.genType);
        });
        emit(new InvokeVirtual(e.getLiteral(), e.getType(), at, rt));
    }

    @Override
    public void visit(CvaFalseExpr e)
    {
        emit(new Ldc<Integer>(0));
    }

    @Override
    public void visit(CvaIdentifierExpr expr)
    {
        if (expr.isField())
        {
            emit(new ALoad(0));
            AbstractType type = expr.getType();
            if (type instanceof CvaClassType)
            {
                emit(new GetField(String.format("%s/%s", this.className, expr.getLiteral()),
                        String.format("L%s;", ((CvaClassType) type).getLiteral())));
            }
            else
            {
                emit(new GetField(String.format("%s/%s", this.className, expr.getLiteral()),
                        "I"));
            }
        }
        else
        {
            int index = this.indexTable.get(expr.getLiteral());
            if (expr.getType() instanceof AbstractReferenceType)
            {
                emit(new ALoad(index));
            }
            else
            {
                emit(new ILoad(index));
            }
        }
    }

    @Override
    public void visit(CvaLessThanExpr e)
    {
        Label t = new Label();
        Label r = new Label();
        visit(e.getLeft());
        visit(e.getRight());
        emit(new Ificmplt(t));
        emit(new Ldc<Integer>(0));
        emit(new Goto(r));
        emit(new LabelJ(t));
        emit(new Ldc<Integer>(1));
        emit(new LabelJ(r));
    }

    @Override
    public void visit(CvaNewExpr e)
    {
        emit(new New(e.getLiteral()));
    }

    @Override
    public void visit(CvaNegateExpr e)
    {
        Label f = new Label();
        Label r = new Label();
        visit(e.getExpr());
        emit(new Ldc<Integer>(1));
        emit(new Ificmplt(f));
        emit(new Ldc<Integer>(1));
        emit(new Goto(r));
        emit(new LabelJ(f));
        emit(new Ldc<Integer>(0));
        emit(new LabelJ(r));
    }

    @Override
    public void visit(CvaNumberIntExpr e)
    {
        emit(new Ldc<Integer>(e.getValue()));
    }

    @Override
    public void visit(CvaStringExpr expr)
    {
        // FIXME;
        emit(new Ldc<String>(String.format("\"%s\"", expr.getLiteral())));
    }

    @Override
    public void visit(CvaSubExpr e)
    {
        visit(e.getLeft());
        visit(e.getRight());
        emit(new ISub());
    }

    @Override
    public void visit(CvaThisExpr e)
    {
        emit(new ALoad(0));
    }

    @Override
    public void visit(CvaMulExpr e)
    {
        visit(e.getLeft());
        visit(e.getRight());
        emit(new IMul());
    }

    @Override
    public void visit(CvaTrueExpr e)
    {
        emit(new Ldc<Integer>(1));
    }

    /**
     * @param assignSta
     * @FIXME 类型添加String是1, 二是要用前面写的switch方法替换;
     * 添加string应该只需要ref type就行;
     */
    @Override
    public void visit(CvaAssignStatement assignSta)
    {
        try
        {
            int index = this.indexTable.get(assignSta.getLiteral());
            visit(assignSta.getExpr());
//            if (assignSta.getType() instanceof CvaClassType)
            if (assignSta.getType() instanceof AbstractReferenceType)
            {
                emit(new AStore(index));
            }
            else
            {
                emit(new IStore(index));
            }
        }
        catch (NullPointerException e)
        {
            emit(new ALoad(0));
            visit(assignSta.getExpr());
            emit(new PutField(String.format("%s/%s", this.className, assignSta.getLiteral()),
                    assignSta.getType() instanceof CvaClassType ?
                            (String.format("L%s;", ((CvaClassType) assignSta.getType()).getLiteral()))
                            : "I"));
        }
    }

    @Override
    public void visit(CvaBlockStatement blockSta)
    {
        blockSta.getStatementList().forEach(this::visit);
    }

    @Override
    public void visit(CvaIfStatement ifSta)
    {
        Label l = new Label();
        Label r = new Label();
        visit(ifSta.getCondition());
        emit(new Ldc<Integer>(1));
        emit(new Ificmplt(l));
        visit(ifSta.getThenStatement());
        emit(new Goto(r));
        emit(new LabelJ(l));
        if (ifSta.getElseStatement() != null)
        {
            visit(ifSta.getElseStatement());
        }
        emit(new LabelJ(r));
    }

    @Override
    public void visit(CvaWriteStatement writeSta)
    {
        byte mode = writeSta.getWriteMode();
        AbstractExpression expr = writeSta.getExpr();
        visit(expr);
        switch (expr.getClass().getSimpleName())
        {
            case CvaExprClassName.CVA_NUMBER_INT_EXPR:
            {
                emit(new WriteInstruction(mode, WriteILConst.WRITE_INT));
                break;
            }
            case CvaExprClassName.CVA_STRING_EXPR:
            {
                emit(new WriteInstruction(mode, WriteILConst.WRITE_STRING));
                break;
            }
            case CvaExprClassName.CVA_IDENTIFIER_EXPR:
            {
                byte type = parseEmitTypeCode(((CvaIdentifierExpr) expr).getType().toString());
                emit(new WriteInstruction(mode, type));
                break;
            }
            case CvaExprClassName.CVA_CALL_EXPR:
            {
                byte type = parseEmitTypeCode(((CvaCallExpr) expr).getRetType().toString());
                emit(new WriteInstruction(mode, type));
                break;
            }
            default:
            {
                // FIXME, 断点打在这里debuge可以保证就是遗漏的情况;
                // 因为检查过, 所以其实可以放心弄, 但是这里有一个情况没打印;
                // 目前可能遇到的情况有 idexpr, callfuncexpr, numberintexpr;
                // 注释掉这个可以应对可能的异常;
//                emit(new WriteInt());
                break;
            }
        }
    }

    private byte parseEmitTypeCode(String typeString)
    {
        switch (typeString)
        {
            case CvaIntType.TYPE_LITERAL:
            {
                return WriteILConst.WRITE_INT;
            }
            case CvaStringType.TYPE_LITERAL:
            {
                return WriteILConst.WRITE_STRING;
            }
            default:
            {
                // todo;
                break;
            }
        }
        return -1;
    }

    @Override
    public void visit(CvaWhileStatement whileSta)
    {
        Label con = new Label();
        Label end = new Label();
        emit(new LabelJ(con));
        visit(whileSta.getCondition());
        emit(new Ldc<Integer>(1));
        emit(new Ificmplt(end));
        visit(whileSta.getBody());
        emit(new Goto(con));
        emit(new LabelJ(end));
    }

    @Override
    public void visit(CvaMethod cvaMethod)
    {
        this.index = 1;
        this.indexTable = new HashMap<>();
        visit(cvaMethod.getRetType());
        BaseType theRetType = this.genType;

        List<GenDeclaration> formalList = new ArrayList<>();
        cvaMethod.getArgumentList().forEach(f ->
        {
            visit(f);
            formalList.add(this.genDecl);
        });
        List<GenDeclaration> localList = new ArrayList<>();
        cvaMethod.getLocalVarList().forEach(l ->
        {
            visit(l);
            localList.add(this.genDecl);
        });
        setLinearInstrList(new ArrayList<>());
        // 方法内的;
        cvaMethod.getStatementList().forEach(this::visit);

        visit(cvaMethod.getRetExpr());
        if (cvaMethod.getRetType() instanceof AbstractReferenceType)
        {
            emit(new AReturn());
        }
        else
        {
            emit(new IReturn());
        }
        genMethod = new GenMethod(
                cvaMethod.name(),
                theRetType,
                this.className,
                formalList,
                localList,
                this.linearInstrList,
                0,
                this.index);
    }

    @Override
    public void visit(CvaMainMethod mainMethod)
    {
        this.index = 1;
        this.indexTable = new HashMap<>();
        visit(mainMethod.getRetType());
        BaseType theRetType = this.genType;

        List<GenDeclaration> formalList = new ArrayList<>();
        mainMethod.getArgumentList().forEach(f ->
        {
            visit(f);
            formalList.add(this.genDecl);
        });
        List<GenDeclaration> localList = new ArrayList<>();
        mainMethod.getLocalVarList().forEach(l ->
        {
            visit(l);
            localList.add(this.genDecl);
        });
        setLinearInstrList(new ArrayList<>());
        // 方法内的;
        mainMethod.getStatementList().forEach(this::visit);

        genMethod = new GenMethod(
                mainMethod.name(),
                theRetType,
                this.className,
                formalList,
                localList,
                this.linearInstrList,
                0,
                this.index);
    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        setClassName(cvaClass.name());
        List<GenDeclaration> fieldList = new ArrayList<>();
        cvaClass.getFieldList().forEach(f ->
        {
            visit(f);
            fieldList.add(this.genDecl);
        });
        List<GenMethod> methodList = new ArrayList<>();
        cvaClass.getMethodList().forEach(m ->
        {
            visit(m);
            methodList.add(this.genMethod);
        });
        genClass = new GenClass(
                cvaClass.name(),
                cvaClass.parent(),
                fieldList,
                methodList
        );
    }

    @Override
    public void visit(CvaEntryClass entryClass)
    {
        visit((CvaMainMethod) entryClass.getMainMethod());
//        genEntry = new GenEntry(entryClass.name(),
//                this.linearInstrList);
//        setLinearInstrList(new ArrayList<>());
//        visit(entryClass.statement());

//        entryClass..forEach(this::visit);
        genEntry = new GenEntry(entryClass.name(),
                this.linearInstrList);
        // 会重复使用, 赋给每个域;
        this.linearInstrList = new ArrayList<>();
    }

    @Override
    public void visit(CvaProgram cvaProgram)
    {
        visit(cvaProgram.getEntryClass());
        List<GenClass> classList = new ArrayList<>();
        cvaProgram.getClassList().forEach(c ->
        {
            visit(c);
            classList.add(this.genClass);
        });
        genProgram = new GenProgram(
                this.genEntry,
                classList);
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public BaseType getGenType()
    {
        return genType;
    }

    public void setGenType(BaseType genType)
    {
        this.genType = genType;
    }

    public GenDeclaration getGenDecl()
    {
        return genDecl;
    }

    public void setGenDecl(GenDeclaration genDecl)
    {
        this.genDecl = genDecl;
    }

    public List<BaseInstruction> getLinearInstrList()
    {
        return linearInstrList;
    }

    public void setLinearInstrList(List<BaseInstruction> linearInstrList)
    {
        this.linearInstrList = linearInstrList;
    }

    public GenMethod getGenMethod()
    {
        return genMethod;
    }

    public void setGenMethod(GenMethod genMethod)
    {
        this.genMethod = genMethod;
    }

    public GenClass getGenClass()
    {
        return genClass;
    }

    public void setGenClass(GenClass genClass)
    {
        this.genClass = genClass;
    }

    public GenEntry getGenEntry()
    {
        return genEntry;
    }

    public void setGenEntry(GenEntry genEntry)
    {
        this.genEntry = genEntry;
    }

    public GenProgram getGenProgram()
    {
        return genProgram;
    }

    public void setGenProgram(GenProgram genProgram)
    {
        this.genProgram = genProgram;
    }
}
