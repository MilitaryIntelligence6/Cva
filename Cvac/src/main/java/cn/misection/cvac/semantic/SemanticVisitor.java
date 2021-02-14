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
        else if (target instanceof Ast.Type.ClassType && cur instanceof Ast.Type.ClassType)
        {
            String tarName = ((Ast.Type.ClassType) target).id;
            String curName = ((Ast.Type.ClassType) cur).id;
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
    public void visit(Ast.Type.Boolean t) {}

    @Override
    public void visit(Ast.Type.ClassType t) {}

    @Override
    public void visit(Ast.Type.Int t) {}

    // Dec
    @Override
    public void visit(Ast.Dec.DecSingle d) {}

    // Exp
    @Override
    public void visit(Ast.Exp.Add e)
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
    public void visit(Ast.Exp.And e)
    {
        this.visit(e.left);
        Ast.Type.T lefty = this.type;
        this.visit(e.right);
        if (!this.type.toString().equals(lefty.toString()))
            error(e.lineNum, "and expression" +
                    " the type of left is " + lefty.toString() +
                    ", but the type of right is " + this.type.toString());
        else if (!new Ast.Type.Boolean().toString().equals(this.type.toString()))
            error(e.lineNum, " only integer numbers can be added.");

        this.type = new Ast.Type.Boolean();
    }

    @Override
    public void visit(Ast.Exp.Call e)
    {
        this.visit(e.exp);
        Ast.Type.ClassType expType = null;

        if (this.type instanceof Ast.Type.ClassType)
        {
            expType = ((Ast.Type.ClassType) this.type);
            e.type = expType.id;
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

        MethodType mty = this.classTable.getMethodType(expType.id, e.id);

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
            if (!isMatch(((Ast.Dec.DecSingle) mty.argsType.get(i)).type, argsty.get(i)))
                error(e.args.get(i).lineNum, "the parameter " + (i + 1) +
                        " needs a " + ((Ast.Dec.DecSingle) mty.argsType.get(i)).type.toString() +
                        ", but got a " + argsty.get(i).toString());


        e.at = argsty;
        e.rt = mty.retType;
        this.type = mty.retType;
    }

    @Override
    public void visit(Ast.Exp.False e)
    {
        this.type = new Ast.Type.Boolean();
    }

    @Override
    public void visit(Ast.Exp.Id e)
    {
        Ast.Type.T type = this.methodVarTable.get(e.id);
        boolean isField = type == null;
        String className = currentClass;
        while (type == null && className != null)
        {
            type = this.classTable.getFieldType(className, e.id);
            className = this.classTable.getClassBinding(className).base;
        }

        if (this.curMthLocals.contains(e.id))
            error(e.lineNum, "you should assign \"" + e.id + "\" a value before use it.");

        if (type == null)
        {
            error(e.lineNum, "you should declare \"" + e.id + "\" before use it.");
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
    public void visit(Ast.Exp.LT e)
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

        this.type = new Ast.Type.Boolean();
    }

    @Override
    public void visit(Ast.Exp.NewObject e)
    {
        if (this.classTable.getClassBinding(e.id) != null)
            this.type = new Ast.Type.ClassType(e.id);
        else
        {
            error(e.lineNum, "cannot find the declaration of class \"" + e.id + "\".");
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
    public void visit(Ast.Exp.Not e)
    {
        this.visit(e.exp);
        if (!this.type.toString().equals(new Ast.Type.Boolean().toString()))
            error(e.lineNum, "the exp cannot calculate to a boolean.");

        this.type = new Ast.Type.Boolean();
    }

    @Override
    public void visit(Ast.Exp.Num e)
    {
        this.type = new Ast.Type.Int();
    }

    @Override
    public void visit(Ast.Exp.Sub e)
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
    public void visit(Ast.Exp.This e)
    {
        this.type = new Ast.Type.ClassType(currentClass);
    }

    @Override
    public void visit(Ast.Exp.Times e)
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
    public void visit(Ast.Exp.True e)
    {
        this.type = new Ast.Type.Boolean();
    }

    @Override
    public void visit(Ast.Stm.Assign s)
    {
        this.visit(s.exp);
        s.type = this.type;

        if (this.curMthLocals.contains(s.id))
            this.curMthLocals.remove(s.id);

        Ast.Exp.Id id = new Ast.Exp.Id(s.id, s.lineNum);
        this.visit(id);
        Ast.Type.T idty = this.type;
        //if (!this.type.toString().equals(idty.toString()))
        if (!isMatch(idty, s.type))
            error(s.lineNum, "the type of \"" + s.id + "\" is " + idty.toString() +
                    ", but the type of expression is " + s.type.toString() +
                    ". Assign failed.");

    }

    @Override
    public void visit(Ast.Stm.Block s)
    {
        s.stms.forEach(this::visit);
    }

    @Override
    public void visit(Ast.Stm.If s)
    {
        this.visit(s.condition);
        if (!this.type.toString().equals(new Ast.Type.Boolean().toString()))
            error(s.condition.lineNum,
                    "the condition's type should be a boolean.");

        this.visit(s.thenStm);
        this.visit(s.elseStm);
    }

    @Override
    public void visit(Ast.Stm.Write s)
    {
        this.visit(s.exp);
        if (!this.type.toString().equals(new Ast.Type.Int().toString()))
            error(s.exp.lineNum,
                    "the expression in \"print()\" must be a integer " +
                            "or can be calculate to an integer.");
    }

    @Override
    public void visit(Ast.Stm.While s)
    {
        this.visit(s.condition);
        if (!this.type.toString().equals(new Ast.Type.Boolean().toString()))
            error(s.condition.lineNum, "the condition's type should be a boolean.");

        this.visit(s.body);
    }

    @Override
    public void visit(Ast.Method.MethodSingle m)
    {
        this.methodVarTable = new MethodVariableTable();
        this.methodVarTable.put(m.formals, m.locals);
        this.curMthLocals = new HashSet<>();
        m.locals.forEach(local -> this.curMthLocals.add(((Ast.Dec.DecSingle) local).id));
        m.stms.forEach(this::visit);
        this.visit(m.retExp);
        // if (!this.type.toString().equals(m.retType.toString()))
        if (!isMatch(m.retType, this.type))
            error(m.retExp.lineNum,
                    "the return expression's type is not match the method \"" +
                            m.id + "\" declared.");

    }

    @Override
    public void visit(Ast.Class.ClassSingle c)
    {
        this.currentClass = c.id;
        c.methods.forEach(this::visit);
    }

    @Override
    public void visit(Ast.MainClass.MainClassSingle c)
    {
        this.currentClass = c.id;
        this.visit(c.stm);
    }

    @Override
    public void visit(Ast.Program.ProgramSingle p)
    {
        // put main class to class table
        this.classTable.putClassBinding(((Ast.MainClass.MainClassSingle) p.mainClass).id,
                new ClassBinding(null));

        for (Ast.Class.T c : p.classes)
        {
            Ast.Class.ClassSingle cla = ((Ast.Class.ClassSingle) c);
            this.classTable.putClassBinding(cla.id, new ClassBinding(cla.base));

            cla.fields.forEach(field -> this.classTable.putFieldToClass(cla.id,
                    ((Ast.Dec.DecSingle) field).id,
                    ((Ast.Dec.DecSingle) field).type));

            cla.methods.forEach(method -> this.classTable.putMethodToClass(cla.id,
                    ((Ast.Method.MethodSingle) method).id,
                    new MethodType(((Ast.Method.MethodSingle) method).retType,
                            ((Ast.Method.MethodSingle) method).formals)));
        }

        this.visit(p.mainClass);
        p.classes.forEach(this::visit);
    }
}
