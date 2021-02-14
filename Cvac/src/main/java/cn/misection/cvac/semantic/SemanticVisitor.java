package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.Ast;

import java.util.HashSet;
import java.util.LinkedList;


/**
 * Created by Mengxu on 2017/1/13.
 */
public class SemanticVisitor implements cn.misection.cvac.ast.Visitor
{
    private ClassTable classTable;
    private MethodVariableTable methodVarTable;
    private String currentClass;
    private Ast.Type.T type;
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

    private boolean isMatch(Ast.Type.T target, Ast.Type.T cur)
    {
        if (target.toString().equals(cur.toString()))
            return true;
        else if (target instanceof Ast.Type.CvaClass && cur instanceof Ast.Type.CvaClass)
        {
            String tarName = ((Ast.Type.CvaClass) target).literal;
            String curName = ((Ast.Type.CvaClass) cur).literal;
            boolean flag = tarName.equals(curName);
            while (curName != null && !flag)
            {
                curName = classTable.getClassBinding(curName).base;
                flag = tarName.equals(curName);
            }
            return flag;
        } else return false;
    }

    // Type
    @Override
    public void visit(Ast.Type.CvaBoolean t) {}

    @Override
    public void visit(Ast.Type.CvaClass t) {}

    @Override
    public void visit(Ast.Type.Int t) {}

    // Dec
    @Override
    public void visit(Ast.Decl.CvaDeclaration d) {}

    // Exp
    @Override
    public void visit(Ast.Expr.CvaAddExpr e)
    {
        this.visit(e.left);
        Ast.Type.T lefty = this.type;
        this.visit(e.right);
        if (!this.type.toString().equals(lefty.toString()))
            error(e.lineNum, "add expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        else if (!new Ast.Type.Int().toString().equals(this.type.toString()))
            error(e.lineNum, " only integer numbers can be added.");

        this.type = new Ast.Type.Int();
    }

    @Override
    public void visit(Ast.Expr.CvaAndAndExpr e)
    {
        this.visit(e.left);
        Ast.Type.T lefty = this.type;
        this.visit(e.right);
        if (!this.type.toString().equals(lefty.toString()))
            error(e.lineNum, "and expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        else if (!new Ast.Type.CvaBoolean().toString().equals(this.type.toString()))
            error(e.lineNum, " only integer numbers can be added.");

        this.type = new Ast.Type.CvaBoolean();
    }

    @Override
    public void visit(Ast.Expr.CvaCallExpr e)
    {
        this.visit(e.exp);
        Ast.Type.CvaClass expType = null;

        if (this.type instanceof Ast.Type.CvaClass)
        {
            expType = ((Ast.Type.CvaClass) this.type);
            e.type = expType.literal;
        } else
        {
            error(e.lineNum, "only an instance of class can be invoked.");
            this.type = new Ast.Type.T()
            {
                @Override
                public String toString()
                {
                    return "unknown";
                }
            };
            return;
        }

        LinkedList<Ast.Type.T> argsty = new LinkedList<>();
        e.args.forEach(arg ->
        {
            this.visit(arg);
            argsty.addLast(this.type);
        });

        MethodType mty = this.classTable.getMethodType(expType.literal, e.literal);

        if (mty == null)
        {
            error(e.lineNum, "the method you are calling haven't been defined.");
            e.at = argsty;
            e.rt = new Ast.Type.T()
            {
                @Override
                public String toString()
                {
                    return "unknown";
                }
            };
            this.type = e.rt;
            return;
        }

        if (mty.argsType.size() != argsty.size())
            error(e.lineNum, "the count of arguments is not match.");

        for (int i = 0; i < mty.argsType.size(); i++)
            if (!isMatch(((Ast.Decl.CvaDeclaration) mty.argsType.get(i)).type, argsty.get(i)))
                error(e.args.get(i).lineNum, "the parameter " + (i + 1) +
                        " needs a " + ((Ast.Decl.CvaDeclaration) mty.argsType.get(i)).type.toString() +
                        ", but got a " + argsty.get(i).toString());


        e.at = argsty;
        e.rt = mty.retType;
        this.type = mty.retType;
    }

    @Override
    public void visit(Ast.Expr.CvaFalseExpr e)
    {
        this.type = new Ast.Type.CvaBoolean();
    }

    @Override
    public void visit(Ast.Expr.CvaIdentifier e)
    {
        Ast.Type.T type = this.methodVarTable.get(e.literal);
        boolean isField = type == null;
        String className = currentClass;
        while (type == null && className != null)
        {
            type = this.classTable.getFieldType(className, e.literal);
            className = this.classTable.getClassBinding(className).base;
        }

        if (this.curMthLocals.contains(e.literal))
            error(e.lineNum, "you should assign \"" + e.literal + "\" a value before use it.");

        if (type == null)
        {
            error(e.lineNum, "you should declare \"" + e.literal + "\" before use it.");
            e.type = new Ast.Type.T()
            {
                @Override
                public String toString()
                {
                    return "unknown";
                }
            };
            this.type = e.type;
        } else
        {
            e.isField = isField;
            e.type = type;
            this.type = type;
        }
    }

    @Override
    public void visit(Ast.Expr.CvaLTExpr e)
    {
        this.visit(e.left);
        Ast.Type.T lefty = this.type;
        this.visit(e.right);
        if (!this.type.toString().equals(lefty.toString()))
        {
            error(e.lineNum, "compare expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        } else if (!new Ast.Type.Int().toString().equals(this.type.toString()))
            error(e.lineNum, "only integer numbers can be compared.");

        this.type = new Ast.Type.CvaBoolean();
    }

    @Override
    public void visit(Ast.Expr.CvaNewExpr e)
    {
        if (this.classTable.getClassBinding(e.literal) != null)
            this.type = new Ast.Type.CvaClass(e.literal);
        else
        {
            error(e.lineNum, "cannot find the declaration of class \"" + e.literal + "\".");
            this.type = new Ast.Type.T()
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
    public void visit(Ast.Expr.CvaNegateExpr e)
    {
        this.visit(e.expr);
        if (!this.type.toString().equals(new Ast.Type.CvaBoolean().toString()))
            error(e.lineNum, "the exp cannot calculate to a boolean.");

        this.type = new Ast.Type.CvaBoolean();
    }

    @Override
    public void visit(Ast.Expr.CvaNumberInt e)
    {
        this.type = new Ast.Type.Int();
    }

    @Override
    public void visit(Ast.Expr.CvaSubExpr e)
    {
        this.visit(e.left);
        Ast.Type.T lefty = this.type;
        this.visit(e.right);
        if (!this.type.toString().equals(lefty.toString()))
            error(e.lineNum, "sub expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        else if (!new Ast.Type.Int().toString().equals(this.type.toString()))

            error(e.lineNum, " only integer numbers can be subbed.");

        this.type = new Ast.Type.Int();
    }

    @Override
    public void visit(Ast.Expr.CvaThisExpr e)
    {
        this.type = new Ast.Type.CvaClass(currentClass);
    }

    @Override
    public void visit(Ast.Expr.CvaMuliExpr e)
    {
        this.visit(e.left);
        Ast.Type.T lefty = this.type;
        this.visit(e.right);
        if (!this.type.toString().equals(lefty.toString()))
            error(e.lineNum, "times expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        else if (!new Ast.Type.Int().toString().equals(this.type.toString()))
            error(e.lineNum, "only integer numbers can be timed.");

        this.type = new Ast.Type.Int();
    }

    @Override
    public void visit(Ast.Expr.CvaTrueExpr e)
    {
        this.type = new Ast.Type.CvaBoolean();
    }

    @Override
    public void visit(Ast.Stm.CvaAssign s)
    {
        this.visit(s.exp);
        s.type = this.type;

        if (this.curMthLocals.contains(s.id))
            this.curMthLocals.remove(s.id);

        Ast.Expr.CvaIdentifier cvaIdentifier = new Ast.Expr.CvaIdentifier(s.id, s.lineNum);
        this.visit(cvaIdentifier);
        Ast.Type.T idty = this.type;
        //if (!this.type.toString().equals(idty.toString()))
        if (!isMatch(idty, s.type))
            error(s.lineNum, "the type of \"" + s.id + "\" is " + idty.toString() +
                    ", but the type of expression is " + s.type.toString() +
                    ". Assign failed.");

    }

    @Override
    public void visit(Ast.Stm.CvaBlock s)
    {
        s.stms.forEach(this::visit);
    }

    @Override
    public void visit(Ast.Stm.CvaIfStatement s)
    {
        this.visit(s.condition);
        if (!this.type.toString().equals(new Ast.Type.CvaBoolean().toString()))
            error(s.condition.lineNum,
                    "the condition's type should be a boolean.");

        this.visit(s.thenStm);
        this.visit(s.elseStm);
    }

    @Override
    public void visit(Ast.Stm.CvaWriteOperation s)
    {
        this.visit(s.exp);
        if (!this.type.toString().equals(new Ast.Type.Int().toString()))
            error(s.exp.lineNum,
                    "the expression in \"print()\" must be a integer " +
                            "or can be calculate to an integer.");
    }

    @Override
    public void visit(Ast.Stm.CvaWhileStatement s)
    {
        this.visit(s.condition);
        if (!this.type.toString().equals(new Ast.Type.CvaBoolean().toString()))
            error(s.condition.lineNum, "the condition's type should be a boolean.");

        this.visit(s.body);
    }

    @Override
    public void visit(Ast.Method.CvaMethod m)
    {
        this.methodVarTable = new MethodVariableTable();
        this.methodVarTable.put(m.formals, m.locals);
        this.curMthLocals = new HashSet<>();
        m.locals.forEach(local -> this.curMthLocals.add(((Ast.Decl.CvaDeclaration) local).literal));
        m.stms.forEach(this::visit);
        this.visit(m.retExp);
        // if (!this.type.toString().equals(m.retType.toString()))
        if (!isMatch(m.retType, this.type))
            error(m.retExp.lineNum,
                    "the return expression's type is not match the method \"" +
                            m.literal + "\" declared.");

    }

    @Override
    public void visit(Ast.Clas.CvaClass c)
    {
        this.currentClass = c.literal;
        c.methods.forEach(this::visit);
    }

    @Override
    public void visit(Ast.MainClass.CvaEntry c)
    {
        this.currentClass = c.id;
        this.visit(c.stm);
    }

    @Override
    public void visit(Ast.Program.CvaProgram p)
    {
        // put main class to class table
        this.classTable.putClassBinding(((Ast.MainClass.CvaEntry) p.mainClass).id,
                new ClassBinding(null));

        for (Ast.Clas.T c : p.classes)
        {
            Ast.Clas.CvaClass cla = ((Ast.Clas.CvaClass) c);
            this.classTable.putClassBinding(cla.literal, new ClassBinding(cla.parent));

            cla.fields.forEach(field -> this.classTable.putFieldToClass(cla.literal,
                    ((Ast.Decl.CvaDeclaration) field).literal,
                    ((Ast.Decl.CvaDeclaration) field).type));

            cla.methods.forEach(method -> this.classTable.putMethodToClass(cla.literal,
                    ((Ast.Method.CvaMethod) method).literal,
                    new MethodType(((Ast.Method.CvaMethod) method).retType,
                            ((Ast.Method.CvaMethod) method).formals)));
        }

        this.visit(p.mainClass);
        p.classes.forEach(this::visit);
    }
}
