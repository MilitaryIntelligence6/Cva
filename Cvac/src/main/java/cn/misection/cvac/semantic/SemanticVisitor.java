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
    private HashSet<String> curMthLocals;

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
    public void visit(CvaBooleanType t) {}

    @Override
    public void visit(CvaClassType t) {}

    @Override
    public void visit(CvaIntType t) {}

    @Override
    public void visit(CvaStringType type) {}

    // Dec
    @Override
    public void visit(CvaDeclaration d) {}

    // Exp
    @Override
    public void visit(CvaAddExpr addExpr)
    {
        visit(addExpr.getLeft());
        AbstractType lefty = this.type;
        visit(addExpr.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(addExpr.getLineNum(),
                    String.format("add expression the type of left is %s, but the type of right is %s",
                            lefty.toString(), this.type.toString()));
        }
//        else if (!new CvaInt().toString().equals(this.type.toString()))
        else if (!(type instanceof CvaIntType))
        {
            errorLog(addExpr.getLineNum(), " only integer numbers can be added.");
        }

        this.type = new CvaIntType();
    }

    @Override
    public void visit(CvaAndAndExpr e)
    {
        visit(e.getLeft());
        AbstractType lefty = this.type;
        visit(e.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(e.getLineNum(),
                    String.format("and expression the type of left is %s, but the type of right is %s", lefty.toString(), this.type.toString()));
        }
//        else if (!new CvaBoolean().toString().equals(this.type.toString()))
        else if (!(type instanceof CvaBooleanType))
        {
            errorLog(e.getLineNum(), " only integer numbers can be added.");
        }

        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaCallExpr e)
    {
        visit(e.getExpr());
        CvaClassType expType = null;

        if (this.type instanceof CvaClassType)
        {
            expType = ((CvaClassType) this.type);
            e.setType(expType.getLiteral());
        }
        else
        {
            errorLog(e.getLineNum(), "only an instance of class can be invoked.");
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
        e.getArgs().forEach(arg ->
        {
            visit(arg);
            argsty.add(this.type);
        });

        MethodType mty = classTable.getMethodType(expType.getLiteral(), e.getLiteral());

        if (mty == null)
        {
            errorLog(e.getLineNum(), "the method you are calling haven't been defined.");
            e.setArgTypeList(argsty);
            e.setRetType(
                    new AbstractType()
                    {
                        @Override
                        public String toString()
                        {
                            return "unknown";
                        }
                    }
            );

            this.type = e.getRetType();
            return;
        }

        if (mty.getArgsType().size() != argsty.size())
        {
            errorLog(e.getLineNum(), "the count of arguments is not match.");
        }

        for (int i = 0; i < mty.getArgsType().size(); i++)
        {
            if (!isMatch(((CvaDeclaration) mty.getArgsType().get(i)).getType(), argsty.get(i)))
            {
                errorLog(e.getArgs().get(i).getLineNum(),
                        String.format("the parameter %d needs a %s, but got a %s",
                                i + 1, ((CvaDeclaration) mty.getArgsType().get(i)).getType().toString(), argsty.get(i).toString()));
            }
        }


        e.setArgTypeList(argsty);
        e.setRetType(mty.getRetType());
        this.type = mty.getRetType();
    }

    @Override
    public void visit(CvaFalseExpr e)
    {
        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaIdentifierExpr e)
    {
        AbstractType type = this.methodVarTable.get(e.getLiteral());
        boolean isField = type == null;
        String className = currentClass;
        while (type == null && className != null)
        {
            type = classTable.getFieldType(className, e.getLiteral());
            className = classTable.getClassBinding(className).parent;
        }

        if (this.curMthLocals.contains(e.getLiteral()))
        {
            errorLog(e.getLineNum(),
                    String.format("you should assign \"%s\" a value before use it.",
                    e.getLiteral()));
        }

        if (type == null)
        {
            errorLog(e.getLineNum(),
                    String.format("you should declare \"%s\" before use it.",
                            e.getLiteral()));
            e.setType(
                    new AbstractType()
                    {
                        @Override
                        public String toString()
                        {
                            return "unknown";
                        }
                    }
            );
            this.type = e.getType();
        }
        else
        {
            e.setField(isField);
            e.setType(type);
            this.type = type;
        }
    }

    @Override
    public void visit(CvaLessThanExpr e)
    {
        visit(e.getLeft());
        AbstractType lefty = this.type;
        visit(e.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(e.getLineNum(),
                    String.format("compare expression the type of left is %s, but the type of right is %s",
                    lefty.toString(), this.type.toString()));
        }
        else if (!(type instanceof CvaIntType))
        {
            errorLog(e.getLineNum(), "only integer numbers can be compared.");
        }
        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaNewExpr e)
    {
        if (classTable.getClassBinding(e.getLiteral()) != null)
        {
            this.type = new CvaClassType(e.getLiteral());
        }
        else
        {
            errorLog(e.getLineNum(),
                    String.format("cannot find the declaration of class \"%s\".",
                            e.getLiteral()));
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
    public void visit(CvaNegateExpr e)
    {
        visit(e.getExpr());
        if (!(type instanceof CvaBooleanType))
        {
            errorLog(e.getLineNum(), "the exp cannot calculate to a boolean.");
        }
        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaNumberIntExpr e)
    {
        this.type = new CvaIntType();
    }

    @Override
    public void visit(CvaStringExpr expr)
    {
        this.type = new CvaStringType();
    }

    @Override
    public void visit(CvaSubExpr e)
    {
        visit(e.getLeft());
        AbstractType lefty = this.type;
        visit(e.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(e.getLineNum(),
                    String.format("sub expression the type of left is %s, but the type of right is %s",
                    lefty.toString(), this.type.toString()));
        }
        else if (!(type instanceof CvaIntType))
        {
            errorLog(e.getLineNum(), " only integer numbers can be subbed.");
        }
        this.type = new CvaIntType();
    }

    @Override
    public void visit(CvaThisExpr e)
    {
        this.type = new CvaClassType(currentClass);
    }

    @Override
    public void visit(CvaMulExpr e)
    {
        visit(e.getLeft());
        AbstractType lefty = this.type;
        visit(e.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            errorLog(e.getLineNum(),
                    String.format("times expression the type of left is %s, but the type of right is %s",
                            lefty.toString(), this.type.toString()));
        }
        else if (!(type instanceof CvaIntType))
        {
            errorLog(e.getLineNum(), "only integer numbers can be multiply.");
        }
        this.type = new CvaIntType();
    }

    @Override
    public void visit(CvaTrueExpr e)
    {
        this.type = new CvaBooleanType();
    }

    @Override
    public void visit(CvaAssignStatement s)
    {
        visit(s.getExpr());
        s.setType(this.type);

        if (this.curMthLocals.contains(s.getLiteral()))
        {
            this.curMthLocals.remove(s.getLiteral());
        }

        CvaIdentifierExpr cvaIdentifierExpr = new CvaIdentifierExpr(s.getLineNum(), s.getLiteral());
        visit(cvaIdentifierExpr);
        AbstractType idty = this.type;
        //if (!this.type.toString().equals(idty.toString()))
        if (!isMatch(idty, s.getType()))
        {
            errorLog(s.getLineNum(), String.format("the type of \"%s\" is %s, but the type of expression is %s. Assign failed.",
                    s.getLiteral(), idty.toString(), s.getType().toString()));
        }

    }

    @Override
    public void visit(CvaBlockStatement s)
    {
        s.getStatementList().forEach(this::visit);
    }

    @Override
    public void visit(CvaIfStatement s)
    {
        visit(s.getCondition());
        if (!(type instanceof CvaBooleanType))
        {
            errorLog(s.getCondition().getLineNum(),
                    "the condition's type should be a boolean.");
        }

        visit(s.getThenStatement());
        if (s.getElseStatement() != null)
        {
            visit(s.getElseStatement());
        }
    }

    @Override
    public void visit(CvaWriteStatement writeOp)
    {
        visit(writeOp.getExpr());
//        if (!this.type.toString().equals(new CvaInt().toString()))
        if (this.type instanceof CvaIntType
                || type instanceof CvaStringType)
        {
            return;
        }
        errorLog(writeOp.getExpr().getLineNum(),
                String.format("the expression in write(\"printf()\" \"echo\" \"println\") " +
                        "must be a string or an integer or can be calculate to an integer."));
    }

    @Override
    public void visit(CvaWhileStatement whileSta)
    {
        visit(whileSta.getCondition());
//        if (!this.type.toString().equals(new CvaBoolean().toString()))
        if (!(this.type instanceof CvaBooleanType))
        {
            errorLog(whileSta.getCondition().getLineNum(),
                    "the condition's type should be a boolean.");
        }

        visit(whileSta.getBody());
    }

    @Override
    public void visit(CvaMethod cvaMethod)
    {
        this.methodVarTable = new MethodVariableTable();
        this.methodVarTable.put(
                cvaMethod.getFormalList(),
                cvaMethod.getLocalList()
        );
        this.curMthLocals = new HashSet<>();
        cvaMethod.getLocalList().forEach(local ->
                this.curMthLocals.add(((CvaDeclaration) local).getLiteral()));
        cvaMethod.getStatementList().forEach(this::visit);
        visit(cvaMethod.getRetExpr());
        // if (!this.type.toString().equals(m.retType.toString()))
        if (!isMatch(cvaMethod.getRetType(), this.type))
        {
            errorLog(cvaMethod.getRetExpr().getLineNum(),
                    String.format("the return expression's type is not match the method \"%s\" declared.", 
                            cvaMethod.getLiteral()));
        }

    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        this.currentClass = cvaClass.getLiteral();
        cvaClass.getMethodList().forEach(this::visit);
    }

    @Override
    public void visit(CvaEntry c)
    {
        this.currentClass = c.getLiteral();
        visit(c.getStatement());
    }

    @Override
    public void visit(CvaProgram cvaProgram)
    {
        // put main class to class table
        classTable.putClassBinding(((CvaEntry) cvaProgram.getEntry()).getLiteral(),
                new ClassBinding(null));

        for (AbstractClass abstractClass : cvaProgram.getClassList())
        {
            CvaClass cla = ((CvaClass) abstractClass);
            classTable.putClassBinding(cla.getLiteral(), new ClassBinding(cla.getParent()));

            cla.getFieldList().forEach(field -> classTable.putFieldToClass(cla.getLiteral(),
                    ((CvaDeclaration) field).getLiteral(),
                    ((CvaDeclaration) field).getType())
            );

            cla.getMethodList().forEach(method -> classTable.putMethodToClass(cla.getLiteral(),
                    ((CvaMethod) method).getLiteral(),
                    new MethodType(
                            ((CvaMethod) method).getRetType(),
                            ((CvaMethod) method).getFormalList()))
            );
        }
        visit(cvaProgram.getEntry());
        cvaProgram.getClassList().forEach(this::visit);
    }
}
