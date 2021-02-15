package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;

import java.util.HashSet;
import java.util.LinkedList;


/**
 * Created by Mengxu on 2017/1/13.
 * 语义分析;
 */
public class SemanticVisitor implements cn.misection.cvac.ast.Visitor
{
    private ClassTable classTable;
    private MethodVariableTable methodVarTable;
    private String currentClass;
    private AbstractType type;
    private boolean isOk; // the cn.misection.cvac.ast is correct?
    private HashSet<String> curMthLocals; //current method locals

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

    private void error(int lineNum, String msg)
    {
        this.isOk = false;
        System.out.println("Error: Line " + lineNum + " " + msg);
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
                curName = classTable.getClassBinding(curName).base;
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
    public void visit(CvaBoolean t)
    {
    }

    @Override
    public void visit(CvaClassType t)
    {
    }

    @Override
    public void visit(CvaInt t)
    {
    }

    // Dec
    @Override
    public void visit(CvaDeclaration d)
    {
    }

    // Exp
    @Override
    public void visit(CvaAddExpr e)
    {
        this.visit(e.getLeft());
        AbstractType lefty = this.type;
        this.visit(e.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            error(e.getLineNum(), "add expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        }
        else if (!new CvaInt().toString().equals(this.type.toString()))
        {
            error(e.getLineNum(), " only integer numbers can be added.");
        }

        this.type = new CvaInt();
    }

    @Override
    public void visit(CvaAndAndExpr e)
    {
        this.visit(e.getLeft());
        AbstractType lefty = this.type;
        this.visit(e.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            error(e.getLineNum(), "and expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        }
        else if (!new CvaBoolean().toString().equals(this.type.toString()))
        {
            error(e.getLineNum(), " only integer numbers can be added.");
        }

        this.type = new CvaBoolean();
    }

    @Override
    public void visit(CvaCallExpr e)
    {
        this.visit(e.getExpr());
        CvaClassType expType = null;

        if (this.type instanceof CvaClassType)
        {
            expType = ((CvaClassType) this.type);
            e.setType(expType.getLiteral());
        }
        else
        {
            error(e.getLineNum(), "only an instance of class can be invoked.");
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

        LinkedList<AbstractType> argsty = new LinkedList<>();
        e.getArgs().forEach(arg ->
        {
            this.visit(arg);
            argsty.addLast(this.type);
        });

        MethodType mty = this.classTable.getMethodType(expType.getLiteral(), e.getLiteral());

        if (mty == null)
        {
            error(e.getLineNum(), "the method you are calling haven't been defined.");
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
            error(e.getLineNum(), "the count of arguments is not match.");
        }

        for (int i = 0; i < mty.getArgsType().size(); i++)
        {
            if (!isMatch(((CvaDeclaration) mty.getArgsType().get(i)).getType(), argsty.get(i)))
            {
                error(e.getArgs().get(i).getLineNum(), "the parameter " + (i + 1) +
                        " needs a " + ((CvaDeclaration) mty.getArgsType().get(i)).getType().toString() +
                        ", but got a " + argsty.get(i).toString());
            }
        }


        e.setArgTypeList(argsty);
        e.setRetType(mty.getRetType());
        this.type = mty.getRetType();
    }

    @Override
    public void visit(CvaFalseExpr e)
    {
        this.type = new CvaBoolean();
    }

    @Override
    public void visit(CvaIdentifier e)
    {
        AbstractType type = this.methodVarTable.get(e.getLiteral());
        boolean isField = type == null;
        String className = currentClass;
        while (type == null && className != null)
        {
            type = this.classTable.getFieldType(className, e.getLiteral());
            className = this.classTable.getClassBinding(className).base;
        }

        if (this.curMthLocals.contains(e.getLiteral()))
        {
            error(e.getLineNum(), "you should assign \"" + e.getLiteral() + "\" a value before use it.");
        }

        if (type == null)
        {
            error(e.getLineNum(), "you should declare \"" + e.getLiteral() + "\" before use it.");
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
    public void visit(CvaLTExpr e)
    {
        this.visit(e.getLeft());
        AbstractType lefty = this.type;
        this.visit(e.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            error(e.getLineNum(), "compare expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        }
        else if (!new CvaInt().toString().equals(this.type.toString()))
        {
            error(e.getLineNum(), "only integer numbers can be compared.");
        }

        this.type = new CvaBoolean();
    }

    @Override
    public void visit(CvaNewExpr e)
    {
        if (this.classTable.getClassBinding(e.getLiteral()) != null)
        {
            this.type = new CvaClassType(e.getLiteral());
        }
        else
        {
            error(e.getLineNum(), "cannot find the declaration of class \"" + e.getLiteral() + "\".");
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
        this.visit(e.getExpr());
        if (!this.type.toString().equals(new CvaBoolean().toString()))
        {
            error(e.getLineNum(), "the exp cannot calculate to a boolean.");
        }

        this.type = new CvaBoolean();
    }

    @Override
    public void visit(CvaNumberInt e)
    {
        this.type = new CvaInt();
    }

    @Override
    public void visit(CvaSubExpr e)
    {
        this.visit(e.getLeft());
        AbstractType lefty = this.type;
        this.visit(e.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            error(e.getLineNum(), "sub expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        }
        else if (!new CvaInt().toString().equals(this.type.toString()))

        {
            error(e.getLineNum(), " only integer numbers can be subbed.");
        }

        this.type = new CvaInt();
    }

    @Override
    public void visit(CvaThisExpr e)
    {
        this.type = new CvaClassType(currentClass);
    }

    @Override
    public void visit(CvaMuliExpr e)
    {
        this.visit(e.getLeft());
        AbstractType lefty = this.type;
        this.visit(e.getRight());
        if (!this.type.toString().equals(lefty.toString()))
        {
            error(e.getLineNum(), "times expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        }
        else if (!new CvaInt().toString().equals(this.type.toString()))
        {
            error(e.getLineNum(), "only integer numbers can be timed.");
        }

        this.type = new CvaInt();
    }

    @Override
    public void visit(CvaTrueExpr e)
    {
        this.type = new CvaBoolean();
    }

    @Override
    public void visit(CvaAssign s)
    {
        this.visit(s.getExpr());
        s.setType(this.type);

        if (this.curMthLocals.contains(s.getLiteral()))
        {
            this.curMthLocals.remove(s.getLiteral());
        }

        CvaIdentifier cvaIdentifier = new CvaIdentifier(s.getLineNum(), s.getLiteral());
        this.visit(cvaIdentifier);
        AbstractType idty = this.type;
        //if (!this.type.toString().equals(idty.toString()))
        if (!isMatch(idty, s.getType()))
        {
            error(s.getLineNum(), String.format("the type of \"%s\" is %s, but the type of expression is %s. Assign failed.",
                    s.getLiteral(), idty.toString(), s.getType().toString()));
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
        this.visit(s.getCondition());
        if (!this.type.toString().equals(new CvaBoolean().toString()))
        {
            error(s.getCondition().getLineNum(),
                    "the condition's type should be a boolean.");
        }

        this.visit(s.getThenStatement());
        this.visit(s.getElseStatement());
    }

    @Override
    public void visit(CvaWriteOperation s)
    {
        this.visit(s.getExpr());
        if (!this.type.toString().equals(new CvaInt().toString()))
        {
            error(s.getExpr().getLineNum(),
                    String.format("the expression in \"printf()\" must be a integer or can be calculate to an integer."));
        }
    }

    @Override
    public void visit(CvaWhileStatement s)
    {
        this.visit(s.getCondition());
        if (!this.type.toString().equals(new CvaBoolean().toString()))
        {
            error(s.getCondition().getLineNum(), "the condition's type should be a boolean.");
        }

        this.visit(s.getBody());
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
        this.visit(cvaMethod.getRetExpr());
        // if (!this.type.toString().equals(m.retType.toString()))
        if (!isMatch(cvaMethod.getRetType(), this.type))
        {
            error(cvaMethod.getRetExpr().getLineNum(),
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
        this.visit(c.getStatement());
    }

    @Override
    public void visit(CvaProgram cvaProgram)
    {
        // put main class to class table
        this.classTable.putClassBinding(((CvaEntry) cvaProgram.getEntry()).getLiteral(),
                new ClassBinding(null));

        for (AbstractClass abstractClass : cvaProgram.getClassList())
        {
            CvaClass cla = ((CvaClass) abstractClass);
            this.classTable.putClassBinding(cla.getLiteral(), new ClassBinding(cla.getParent()));

            cla.getFieldList().forEach(field -> this.classTable.putFieldToClass(cla.getLiteral(),
                    ((CvaDeclaration) field).getLiteral(),
                    ((CvaDeclaration) field).getType())
            );

            cla.getMethodList().forEach(method -> this.classTable.putMethodToClass(cla.getLiteral(),
                    ((CvaMethod) method).getLiteral(),
                    new MethodType(
                            ((CvaMethod) method).getRetType(),
                            ((CvaMethod) method).getFormalList()))
            );
        }
        this.visit(cvaProgram.getEntry());
        cvaProgram.getClassList().forEach(this::visit);
    }
}
