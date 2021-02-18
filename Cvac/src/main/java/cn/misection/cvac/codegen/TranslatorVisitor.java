package cn.misection.cvac.codegen;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.CvaMethod;
import cn.misection.cvac.ast.program.CvaProgram;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;

import cn.misection.cvac.codegen.bst.Label;
import cn.misection.cvac.codegen.bst.bclas.GenClass;
import cn.misection.cvac.codegen.bst.bdecl.GenDeclaration;
import cn.misection.cvac.codegen.bst.bentry.GenEntry;
import cn.misection.cvac.codegen.bst.bmethod.GenMethod;
import cn.misection.cvac.codegen.bst.bprogram.GenProgram;
import cn.misection.cvac.codegen.bst.binstruct.BaseInstruction;
import cn.misection.cvac.codegen.bst.binstruct.*;
import cn.misection.cvac.codegen.bst.btype.BaseType;
import cn.misection.cvac.codegen.bst.btype.GenClassType;
import cn.misection.cvac.codegen.bst.btype.GenInt;

import java.util.*;

/**
 * Created by MI6 root 1/17.
 */
public final class TranslatorVisitor implements IVisitor
{
    private String classId;
    private int index;

    private Map<String, Integer> indexTable;
    
    private BaseType type;
    private GenDeclaration dec;
    private List<BaseInstruction> statementList;
    private GenMethod method;
    private GenClass clazz;
    private GenEntry mainClass;
    private GenProgram program;

    public TranslatorVisitor()
    {
        this.setClassId(null);
        this.indexTable = null;
        this.setType(null);
        this.setDec(null);
        this.setStatementList(new LinkedList<>());
        this.setMethod(null);
        this.setClassId(null);
        this.setMainClass(null);
        this.setClazz(null);
        this.setProgram(null);
    }

    private void emit(BaseInstruction s)
    {
        this.getStatementList().add(s);
    }

    @Override
    public void visit(CvaBoolean t)
    {
        this.setType(new GenInt());
    }

    @Override
    public void visit(CvaClassType t)
    {
        this.setType(new GenClassType(t.getLiteral()));
    }

    @Override
    public void visit(CvaInt t)
    {
        this.setType(new GenInt());
    }

    @Override
    public void visit(CvaDeclaration d)
    {
        this.visit(d.getType());
        this.setDec(new GenDeclaration(
                d.getLiteral(),
                this.getType()));
        if (this.indexTable != null) // if it is field
        {
            this.indexTable.put(d.getLiteral(), index++);
        }
    }

    @Override
    public void visit(CvaAddExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
        emit(new IAdd());
    }

    @Override
    public void visit(CvaAndAndExpr e)
    {
        Label f = new Label();
        Label r = new Label();
        this.visit(e.getLeft());
        emit(new Ldc(1));
        emit(new IFicmplt(f));
        this.visit(e.getRight());
        emit(new Ldc(1));
        emit(new IFicmplt(f));
        emit(new Ldc(1));
        emit(new Goto(r));
        emit(new LabelJ(f));
        emit(new Ldc(0));
        emit(new LabelJ(r));
    }

    @Override
    public void visit(CvaCallExpr e)
    {
        this.visit(e.getExpr());
        e.getArgs().forEach(this::visit);
        this.visit(e.getRetType());
        BaseType rt = this.getType();
        List<BaseType> at = new LinkedList<>();
        e.getArgTypeList().forEach(a ->
        {
            this.visit(a);
            at.add(this.getType());
        });
        emit(new InvokeVirtual(e.getLiteral(), e.getType(), at, rt));
    }

    @Override
    public void visit(CvaFalseExpr e)
    {
        emit(new Ldc(0));
    }

    @Override
    public void visit(CvaIdentifier e)
    {
        if (e.isField())
        {
            emit(new ALoad(0));
            AbstractType type = e.getType();
            emit(new GetField(String.format("%s/%s", this.getClassId(), e.getLiteral()),
                    type instanceof CvaClassType ?
                            (String.format("L%s;", ((CvaClassType) type).getLiteral()))
                            : "I"));
        }
        else
        {
            int index = this.indexTable.get(e.getLiteral());
            if (e.getType() instanceof CvaClassType)
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
        this.visit(e.getLeft());
        this.visit(e.getRight());
        emit(new IFicmplt(t));
        emit(new Ldc(0));
        emit(new Goto(r));
        emit(new LabelJ(t));
        emit(new Ldc(1));
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
        this.visit(e.getExpr());
        emit(new Ldc(1));
        emit(new IFicmplt(f));
        emit(new Ldc(1));
        emit(new Goto(r));
        emit(new LabelJ(f));
        emit(new Ldc(0));
        emit(new LabelJ(r));
    }

    @Override
    public void visit(CvaNumberIntExpr e)
    {
        emit(new Ldc(e.getValue()));
    }

    @Override
    public void visit(CvaSubExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
        emit(new ISub());
    }

    @Override
    public void visit(CvaThisExpr e)
    {
        emit(new ALoad(0));
    }

    @Override
    public void visit(CvaMuliExpr e)
    {
        this.visit(e.getLeft());
        this.visit(e.getRight());
        emit(new IMul());
    }

    @Override
    public void visit(CvaTrueExpr e)
    {
        emit(new Ldc(1));
    }

    @Override
    public void visit(CvaAssign s)
    {
        try
        {
            int index = this.indexTable.get(s.getLiteral());
            this.visit(s.getExpr());
            if (s.getType() instanceof CvaClassType)
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
            this.visit(s.getExpr());
            emit(new PutField(String.format("%s/%s", this.getClassId(), s.getLiteral()),
                    s.getType() instanceof CvaClassType ?
                            (String.format("L%s;", ((CvaClassType) s.getType()).getLiteral()))
                            : "I"));
        }
    }

    @Override
    public void visit(CvaBlock s)
    {
        s.getStatementList().forEach(this::visit);
    }

    @Override
    public void visit(CvaIfStatement s)
    {
        Label l = new Label();
        Label r = new Label();
        this.visit(s.getCondition());
        emit(new Ldc(1));
        emit(new IFicmplt(l));
        this.visit(s.getThenStatement());
        emit(new Goto(r));
        emit(new LabelJ(l));
        if (s.getElseStatement() != null)
        {
            this.visit(s.getElseStatement());
        }
        emit(new LabelJ(r));
    }

    @Override
    public void visit(CvaWriteOperation s)
    {
        this.visit(s.getExpr());
        emit(new WriteInstr());
    }

    @Override
    public void visit(CvaWhileStatement s)
    {
        Label con = new Label();
        Label end = new Label();
        emit(new LabelJ(con));
        this.visit(s.getCondition());
        emit(new Ldc(1));
        emit(new IFicmplt(end));
        this.visit(s.getBody());
        emit(new Goto(con));
        emit(new LabelJ(end));
    }

    @Override
    public void visit(CvaMethod cvaMethod)
    {
        this.index = 1;
        this.indexTable = new HashMap<>();
        this.visit(cvaMethod.getRetType());
        BaseType theRetType = this.getType();

        List<GenDeclaration> formalList = new LinkedList<>();
        cvaMethod.getFormalList().forEach(f ->
        {
            this.visit(f);
            formalList.add(this.getDec());
        });

        List<GenDeclaration> localList = new LinkedList<>();
        cvaMethod.getLocalList().forEach(l ->
        {
            this.visit(l);
            localList.add(this.getDec());
        });
        this.setStatementList(new LinkedList<>());
        cvaMethod.getStatementList().forEach(this::visit);

        this.visit(cvaMethod.getRetExpr());

        if (cvaMethod.getRetType() instanceof CvaClassType)
        {
            emit(new AReturn());
        }
        else
        {
            emit(new IReturn());
        }

        this.setMethod(
                new GenMethod(
                cvaMethod.getLiteral(),
                theRetType,
                this.getClassId(),
                formalList,
                localList,
                this.getStatementList(),
                0,
                this.index));
    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        this.setClassId(cvaClass.getLiteral());
        List<GenDeclaration> fieldList = new LinkedList<>();
        cvaClass.getFieldList().forEach(f ->
        {
            this.visit(f);
            fieldList.add(this.getDec());
        });
        List<GenMethod> methodList = new LinkedList<>();
        cvaClass.getMethodList().forEach(m ->
        {
            this.visit(m);
            methodList.add(this.getMethod());
        });
        this.setClazz(
                new GenClass(
                cvaClass.getLiteral(),
                cvaClass.getParent(),
                fieldList,
                        methodList
                ));
    }

    @Override
    public void visit(CvaEntry c)
    {
        this.visit(c.getStatement());
        this.setMainClass(new GenEntry(c.getLiteral(), this.getStatementList()));
        this.setStatementList(new LinkedList<>());
    }

    @Override
    public void visit(CvaProgram p)
    {
        this.visit(p.getEntry());
        List<GenClass> classList = new LinkedList<>();
        p.getClassList().forEach(c ->
        {
            this.visit(c);
            classList.add(this.getClazz());
        });
        this.setProgram(
                new GenProgram(
                        this.getMainClass(),
                        classList));
    }

    public String getClassId()
    {
        return classId;
    }

    public void setClassId(String classId)
    {
        this.classId = classId;
    }

    public BaseType getType()
    {
        return type;
    }

    public void setType(BaseType type)
    {
        this.type = type;
    }

    public GenDeclaration getDec()
    {
        return dec;
    }

    public void setDec(GenDeclaration dec)
    {
        this.dec = dec;
    }

    public List<BaseInstruction> getStatementList()
    {
        return statementList;
    }

    public void setStatementList(List<BaseInstruction> statementList)
    {
        this.statementList = statementList;
    }

    public GenMethod getMethod()
    {
        return method;
    }

    public void setMethod(GenMethod method)
    {
        this.method = method;
    }

    public GenClass getClazz()
    {
        return clazz;
    }

    public void setClazz(GenClass clazz)
    {
        this.clazz = clazz;
    }

    public GenEntry getMainClass()
    {
        return mainClass;
    }

    public void setMainClass(GenEntry mainClass)
    {
        this.mainClass = mainClass;
    }

    public GenProgram getProgram()
    {
        return program;
    }

    public void setProgram(GenProgram program)
    {
        this.program = program;
    }
}
