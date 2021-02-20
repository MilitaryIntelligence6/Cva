package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;
import cn.misection.cvac.ast.type.basic.CvaBooleanType;
import cn.misection.cvac.ast.type.basic.CvaIntType;
import cn.misection.cvac.ast.type.basic.CvaVoidType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.reference.CvaStringType;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by MI6 root 1/13.
 * 语义分析;
 */
public final class SemanticVisitor implements IVisitor
{
    private ClassTable classTable;
    private MethodVariableTable methodVarTable;
    private String currentClass;
    private AbstractType type;

    /**
     * // the cn.misection.cvac.ast is correct?;
     */
    private boolean isOk;

    /**
     * //current method locals;
     */
    private HashSet<String> curMethodLocalSet;

    public SemanticVisitor()
    {
        this.classTable = new ClassTable();
        this.methodVarTable = new MethodVariableTable();
        this.currentClass = null;
        this.type = null;
        this.isOk = true;
    }

    public boolean isOK()
    {
        return this.isOk;
    }

    private void errorLog(int lineNum, String msg)
    {
        this.isOk = false;
        System.err.printf("Error: Line %d %s%n", lineNum, msg);
    }

    private boolean isMatch(AbstractType target, AbstractType cur)
    {
        if (target.toString().equals(cur.toString()))
        {
            return true;
        }
        else if (target instanceof CvaClassType && cur instanceof CvaClassType)
        {
            String tarName = ((CvaClassType) target).getLiteral();
            String curName = ((CvaClassType) cur).getLiteral();
            boolean flag = tarName.equals(curName);
            while (curName != null && !flag)
            {
                curName = classTable.getClassBinding(curName).parent;
                flag = tarName.equals(curName);
            }
            return flag;
        }
        else
        {
            return false;
        }
    }

    // Type
    @Override
    public void visit(CvaBooleanType type) {}

    @Override
    public void visit(CvaClassType type) {}

    @Override
    public void visit(CvaIntType type) {}

    @Override
    public void visit(CvaStringType type) {}

    // Dec
    @Override
    public void visit(CvaDeclaration decl) {}

    // Exp
    @Override
    public void visit(CvaAddExpr expr)
    {
        visit(expr.getLeft());
        AbstractType lefty = this.type;
        visit(expr.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(expr.getLineNum(),
                    String.format("add expression the type of left is %s, but the type of right is %s",
                            lefty.toString(), this.type.toString()));
        }
//        else if (!new CvaInt().toString().equals(this.type.toString()))
        else if (!(type instanceof CvaIntType))
        {
            errorLog(expr.getLineNum(), " only integer numbers can be added.");
        }

        this.type = new CvaIntType();
    }

    @Override
    public void visit(CvaAndAndExpr expr)
    {
        visit(expr.getLeft());
        AbstractType lefty = this.type;
        visit(expr.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(expr.getLineNum(),
                    String.format("and expression the type of left is %s, but the type of right is %s", lefty.toString(), this.type.toString()));
        }
//        else if (!new CvaBoolean().toString().equals(this.type.toString()))
        else if (!(type instanceof CvaBooleanType))
        {
            errorLog(expr.getLineNum(), " only integer numbers can be added.");
        }

        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaCallExpr expr)
    {
        visit(expr.getExpr());
        CvaClassType expType = null;

        if (this.type instanceof CvaClassType)
        {
            expType = ((CvaClassType) this.type);
            expr.setType(expType.getLiteral());
        }
        else
        {
            errorLog(expr.getLineNum(), "only an instance of class can be invoked.");
            this.type = new AbstractType()
            {
                @Override
                public String toString()
                {
                    return "unknown";
                }
            };
            return;
        }

        List<AbstractType> argsty = new ArrayList<>();
        expr.getArgs().forEach(arg ->
        {
            visit(arg);
            argsty.add(this.type);
        });

        MethodType mty = classTable.getMethodType(expType.getLiteral(), expr.getLiteral());

        if (mty == null)
        {
            errorLog(expr.getLineNum(), "the method you are calling haven't been defined.");
            expr.setArgTypeList(argsty);
            expr.setRetType(
                    new AbstractType()
                    {
                        @Override
                        public String toString()
                        {
                            return "unknown";
                        }
                    }
            );

            this.type = expr.getRetType();
            return;
        }

        if (mty.getArgsType().size() != argsty.size())
        {
            errorLog(expr.getLineNum(), "the count of arguments is not match.");
        }

        for (int i = 0; i < mty.getArgsType().size(); i++)
        {
            if (!isMatch(((CvaDeclaration) mty.getArgsType().get(i)).type(), argsty.get(i)))
            {
                errorLog(expr.getArgs().get(i).getLineNum(),
                        String.format("the parameter %d needs a %s, but got a %s",
                                i + 1, ((CvaDeclaration) mty.getArgsType().get(i)).type().toString(), argsty.get(i).toString()));
            }
        }


        expr.setArgTypeList(argsty);
        expr.setRetType(mty.getRetType());
        this.type = mty.getRetType();
    }

    @Override
    public void visit(CvaFalseExpr expr)
    {
        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaIdentifierExpr expr)
    {
        AbstractType type = this.methodVarTable.get(expr.getLiteral());
        boolean isField = type == null;
        String className = currentClass;
        while (type == null && className != null)
        {
            type = classTable.getFieldType(className, expr.getLiteral());
            className = classTable.getClassBinding(className).parent;
        }

        if (this.curMethodLocalSet.contains(expr.getLiteral()))
        {
            errorLog(expr.getLineNum(),
                    String.format("you should assign \"%s\" a value before use it.",
                    expr.getLiteral()));
        }

        if (type == null)
        {
            errorLog(expr.getLineNum(),
                    String.format("you should declare \"%s\" before use it.",
                            expr.getLiteral()));
            expr.setType(
                    new AbstractType()
                    {
                        @Override
                        public String toString()
                        {
                            return "unknown";
                        }
                    }
            );
            this.type = expr.getType();
        }
        else
        {
            expr.setField(isField);
            expr.setType(type);
            this.type = type;
        }
    }

    @Override
    public void visit(CvaLessThanExpr expr)
    {
        visit(expr.getLeft());
        AbstractType lefty = this.type;
        visit(expr.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(expr.getLineNum(),
                    String.format("compare expression the type of left is %s, but the type of right is %s",
                    lefty.toString(), this.type.toString()));
        }
        else if (!(type instanceof CvaIntType))
        {
            errorLog(expr.getLineNum(), "only integer numbers can be compared.");
        }
        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaNewExpr expr)
    {
        if (classTable.getClassBinding(expr.getLiteral()) != null)
        {
            this.type = new CvaClassType(expr.getLiteral());
        }
        else
        {
            errorLog(expr.getLineNum(),
                    String.format("cannot find the declaration of class \"%s\".",
                            expr.getLiteral()));
            this.type = new AbstractType()
            {
                @Override
                public String toString()
                {
                    return "unknown class";
                }
            };
        }
    }

    @Override
    public void visit(CvaNegateExpr expr)
    {
        visit(expr.getExpr());
        if (!(type instanceof CvaBooleanType))
        {
            errorLog(expr.getLineNum(), "the exp cannot calculate to a boolean.");
        }
        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaNumberIntExpr expr)
    {
        this.type = new CvaIntType();
    }

    @Override
    public void visit(CvaStringExpr expr)
    {
        this.type = new CvaStringType();
    }

    @Override
    public void visit(CvaSubExpr expr)
    {
        visit(expr.getLeft());
        AbstractType lefty = this.type;
        visit(expr.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(expr.getLineNum(),
                    String.format("sub expression the type of left is %s, but the type of right is %s",
                    lefty.toString(), this.type.toString()));
        }
        else if (!(type instanceof CvaIntType))
        {
            errorLog(expr.getLineNum(), " only integer numbers can be subbed.");
        }
        this.type = new CvaIntType();
    }

    @Override
    public void visit(CvaThisExpr expr)
    {
        this.type = new CvaClassType(currentClass);
    }

    @Override
    public void visit(CvaMulExpr expr)
    {
        visit(expr.getLeft());
        AbstractType lefty = this.type;
        visit(expr.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(expr.getLineNum(),
                    String.format("times expression the type of left is %s, but the type of right is %s",
                            lefty.toString(), this.type.toString()));
        }
        else if (!(type instanceof CvaIntType))
        {
            errorLog(expr.getLineNum(), "only integer numbers can be multiply.");
        }
        this.type = new CvaIntType();
    }

    @Override
    public void visit(CvaTrueExpr expr)
    {
        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaAssignStatement stm)
    {
        visit(stm.getExpr());
        stm.setType(this.type);

        if (this.curMethodLocalSet.contains(stm.getLiteral()))
        {
            this.curMethodLocalSet.remove(stm.getLiteral());
        }

        CvaIdentifierExpr cvaIdentifierExpr = new CvaIdentifierExpr(stm.getLineNum(), stm.getLiteral());
        visit(cvaIdentifierExpr);
        AbstractType idty = this.type;
        //if (!this.type.toString().equals(idty.toString()))
        if (!isMatch(idty, stm.getType()))
        {
            errorLog(stm.getLineNum(), String.format("the type of \"%s\" is %s, but the type of expression is %s. Assign failed.",
                    stm.getLiteral(), idty.toString(), stm.getType().toString()));
        }

    }

    @Override
    public void visit(CvaBlockStatement stm)
    {
        stm.getStatementList().forEach(this::visit);
    }

    @Override
    public void visit(CvaIfStatement stm)
    {
        visit(stm.getCondition());
        if (!(type instanceof CvaBooleanType))
        {
            errorLog(stm.getCondition().getLineNum(),
                    "the condition's type should be a boolean.");
        }

        visit(stm.getThenStatement());
        if (stm.getElseStatement() != null)
        {
            visit(stm.getElseStatement());
        }
    }

    @Override
    public void visit(CvaWriteStatement stm)
    {
        visit(stm.getExpr());
//        if (!this.type.toString().equals(new CvaInt().toString()))
        if (this.type instanceof CvaIntType
                || type instanceof CvaStringType)
        {
            return;
        }
        errorLog(stm.getExpr().getLineNum(),
                String.format("the expression in write(\"printf()\" \"echo\" \"println\") " +
                        "must be a string or an integer or can be calculate to an integer."));
    }

    @Override
    public void visit(CvaWhileStatement stm)
    {
        visit(stm.getCondition());
//        if (!this.type.toString().equals(new CvaBoolean().toString()))
        if (!(this.type instanceof CvaBooleanType))
        {
            errorLog(stm.getCondition().getLineNum(),
                    "the condition's type should be a boolean.");
        }

        visit(stm.getBody());
    }

    @Override
    public void visit(CvaMethod cvaMethod)
    {
        this.methodVarTable = new MethodVariableTable();
        this.methodVarTable.putVarList(
                cvaMethod.getArgumentList(),
                cvaMethod.getLocalVarList()
        );
        this.curMethodLocalSet = new HashSet<>();
        cvaMethod.getLocalVarList().forEach(local ->
                this.curMethodLocalSet.add(((CvaDeclaration) local).literal()));
        cvaMethod.getStatementList().forEach(this::visit);
        visit(cvaMethod.getRetExpr());
        // if (!this.type.toString().equals(m.retType.toString()))
        if (!(cvaMethod.getRetType() instanceof CvaVoidType))
        {
            if (!isMatch(cvaMethod.getRetType(), this.type))
            {
                errorLog(cvaMethod.getRetExpr().getLineNum(),
                        String.format("the return expression's type is not match the method \"%s\" declared.",
                                cvaMethod.name()));
            }
        }
    }

    @Override
    public void visit(CvaMainMethod mainMethod)
    {
        this.methodVarTable = new MethodVariableTable();
        this.methodVarTable.putVarList(
                mainMethod.getArgumentList(),
                mainMethod.getLocalVarList()
        );
        this.curMethodLocalSet = new HashSet<>();
        mainMethod.getLocalVarList().forEach(local ->
                this.curMethodLocalSet.add(((CvaDeclaration) local).literal()));
        mainMethod.getStatementList().forEach(this::visit);
        if (!(mainMethod.getRetType() instanceof CvaVoidType))
        {
            visit(mainMethod.getRetExpr());
            // if (!this.type.toString().equals(m.retType.toString()))
            if (!isMatch(mainMethod.getRetType(), this.type))
            {
                errorLog(mainMethod.getRetExpr().getLineNum(),
                        String.format("the return expression's type is not match the method \"%s\" declared.",
                                mainMethod.name()));
            }
        }
    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        this.currentClass = cvaClass.name();
        cvaClass.getMethodList().forEach(this::visit);
    }

    @Override
    public void visit(CvaEntryClass entryClass)
    {
        this.currentClass = entryClass.name();
//        visit(entryClass.getStatement());
        visit((CvaMainMethod) entryClass.getMainMethod());
    }

    @Override
    public void visit(CvaProgram program)
    {
        // put main class to class table
        classTable.putClassBinding(((CvaEntryClass) program.getEntryClass()).name(),
                new ClassBinding(null));

        for (AbstractCvaClass abstractCvaClass : program.getClassList())
        {
            CvaClass cla = ((CvaClass) abstractCvaClass);
            classTable.putClassBinding(cla.name(), new ClassBinding(cla.parent()));

            cla.getFieldList().forEach(field -> classTable.putFieldToClass(cla.name(),
                    ((CvaDeclaration) field).literal(),
                    ((CvaDeclaration) field).type())
            );

            cla.getMethodList().forEach(method -> classTable.putMethodToClass(cla.name(),
                    ((CvaMethod) method).name(),
                    new MethodType(
                            ((CvaMethod) method).getRetType(),
                            ((CvaMethod) method).getArgumentList()))
            );
        }
        visit(program.getEntryClass());
        program.getClassList().forEach(this::visit);
    }
}
