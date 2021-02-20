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
import cn.misection.cvac.ast.type.basic.EnumCvaType;
import cn.misection.cvac.ast.type.reference.CvaClassType;
import cn.misection.cvac.ast.type.advance.CvaStringType;

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
    private ICvaType type;

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

    private boolean isMatch(ICvaType src, ICvaType target)
    {
        if (src.toEnum() == target.toEnum())
        {
            return true;
        }
        if (src instanceof CvaClassType && target instanceof CvaClassType)
        {
            String tarName = ((CvaClassType) src).getName();
            String curName = ((CvaClassType) target).getName();
            boolean flag = tarName.equals(curName);
            while (curName != null && !flag)
            {
                curName = classTable.getClassBinding(curName).parent;
                flag = tarName.equals(curName);
            }
            return flag;
        }
        return false;
    }

    /**
     * type
     * @param type t;
     */
    @Override
    public void visit(EnumCvaType type) {}

    @Override
    public void visit(CvaStringType type) {}

    @Override
    public void visit(CvaClassType type) {}

    /**
     * decl
     * @param decl d;
     */
    @Override
    public void visit(CvaDeclaration decl) {}

    /**
     * expr
     * @param expr e;
     */
    @Override
    public void visit(CvaAddExpr expr)
    {
        visit(expr.getLeft());
        ICvaType leftType = this.type;
        visit(expr.getRight());
        if (type.toEnum() != leftType.toEnum())
        {
            errorLog(expr.getLineNum(),
                    String.format("add expression the type of left is %s, but the type of right is %s",
                            leftType.toString(), this.type.toString()));
        }
        else if (!EnumCvaType.isNumber(type.toEnum()))
        {
            errorLog(expr.getLineNum(), " only numeric type numbers can be added.");
        }
    }

    @Override
    public void visit(CvaAndAndExpr expr)
    {
        visit(expr.getLeft());
        ICvaType leftType = this.type;
        visit(expr.getRight());
        if (type.toEnum() != leftType.toEnum())
        {
            errorLog(expr.getLineNum(),
                    String.format("and expression the type of left is %s, but the type of right is %s", leftType.toString(), this.type.toString()));
        }
        else if (type != EnumCvaType.CVA_BOOLEAN)
        {
            errorLog(expr.getLineNum(), "only boolean can be ");
        }
    }

    @Override
    public void visit(CvaCallExpr expr)
    {
        visit(expr.getExpr());
        CvaClassType expType = null;

        if (this.type instanceof CvaClassType)
        {
            expType = ((CvaClassType) this.type);
            expr.setType(expType.getName());
        }
        else
        {
            errorLog(expr.getLineNum(), "only an instance of class can be invoked.");
            type = EnumCvaType.UNKNOWN;
            return;
        }

        List<ICvaType> argTypeList = new ArrayList<>();
        expr.getArgs().forEach(arg ->
        {
            visit(arg);
            argTypeList.add(this.type);
        });

        MethodType methodType = classTable.getMethodType(expType.getName(), expr.getLiteral());

        if (methodType == null)
        {
            errorLog(expr.getLineNum(), "the method you are calling haven't been defined.");
            expr.setArgTypeList(argTypeList);
            expr.setRetType(EnumCvaType.UNKNOWN);

            this.type = expr.getRetType();
            return;
        }

        if (methodType.getArgsType().size() != argTypeList.size())
        {
            errorLog(expr.getLineNum(), "the count of arguments is not match.");
        }

        for (int i = 0; i < methodType.getArgsType().size(); i++)
        {
            if (!isMatch(((CvaDeclaration) methodType.getArgsType().get(i)).type(), argTypeList.get(i)))
            {
                errorLog(expr.getArgs().get(i).getLineNum(),
                        String.format("the parameter %d needs a %s, but got a %s",
                                i + 1, ((CvaDeclaration) methodType.getArgsType().get(i)).type().toString(), argTypeList.get(i).toString()));
            }
        }

        expr.setArgTypeList(argTypeList);
        expr.setRetType(methodType.getRetType());
        this.type = methodType.getRetType();
    }

    @Override
    public void visit(CvaConstFalseExpr expr)
    {
        this.type = EnumCvaType.CVA_BOOLEAN;
    }

    @Override
    public void visit(CvaIdentifierExpr expr)
    {
        ICvaType varType = this.methodVarTable.get(expr.getLiteral());
        boolean isField = varType == null;
        String className = currentClass;
        while (varType == null && className != null)
        {
            varType = classTable.getFieldType(className, expr.getLiteral());
            className = classTable.getClassBinding(className).parent;
        }

        if (this.curMethodLocalSet.contains(expr.getLiteral()))
        {
            errorLog(expr.getLineNum(),
                    String.format("you should assign \"%s\" a value before use it.",
                    expr.getLiteral()));
        }

        if (varType == null)
        {
            errorLog(expr.getLineNum(),
                    String.format("you should declare \"%s\" before use it.",
                            expr.getLiteral()));
            // 不可达;
            expr.setType(EnumCvaType.UNKNOWN);
            this.type = expr.getType();
        }
        else
        {
            expr.setField(isField);
            expr.setType(varType);
            this.type = varType;
        }
    }

    @Override
    public void visit(CvaLessThanExpr expr)
    {
        visit(expr.getLeft());
        ICvaType leftType = this.type;
        visit(expr.getRight());
//        if (!this.type.toString().equals(leftType.toString()))
        if (type.toEnum() != leftType.toEnum())
        {
            errorLog(expr.getLineNum(),
                    String.format("compare expression the type of left is %s, but the type of right is %s",
                    leftType.toString(), this.type.toString()));
        }
//        else if (type != CvaType.CVA_INT)
        // 相等了随便判一个;
//        else if (!(type instanceof EnumCvaType))
        else if (!EnumCvaType.isNumber(type.toEnum()))
        {
            errorLog(expr.getLineNum(), "only numeric type can be compared.");
        }
        this.type = EnumCvaType.CVA_BOOLEAN;
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
            // 执行不到死代码;
            type = EnumCvaType.UNKNOWN;
        }
    }

    @Override
    public void visit(CvaNegateExpr expr)
    {
        visit(expr.getExpr());
        if (type.toEnum() != EnumCvaType.CVA_BOOLEAN)
        {
            errorLog(expr.getLineNum(), "the exp cannot calculate to a boolean.");
        }
        this.type = EnumCvaType.CVA_BOOLEAN;
    }

    @Override
    public void visit(CvaConstIntExpr expr)
    {
        this.type = EnumCvaType.CVA_INT;
    }

    @Override
    public void visit(CvaConstStringExpr expr)
    {
        this.type = new CvaStringType();
    }

    @Override
    public void visit(CvaSubExpr expr)
    {
        visit(expr.getLeft());
        ICvaType leftType = this.type;
        visit(expr.getRight());
        if (type.toEnum() != leftType.toEnum())
        {
            errorLog(expr.getLineNum(),
                    String.format("sub expression the type of left is %s, but the type of right is %s",
                    leftType.toString(), this.type.toString()));
        }
        else if (!EnumCvaType.isNumber(type.toEnum()))
        {
            errorLog(expr.getLineNum(), " only basic numbers can be subbed.");
        }
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
        ICvaType leftType = this.type;
        visit(expr.getRight());
//        if (!this.type.toString().equals(leftType.toString()))
        if (type.toEnum() != leftType.toEnum())
        {
            errorLog(expr.getLineNum(),
                    String.format("times expression the type of left is %s, but the type of right is %s",
                            leftType.toString(), this.type.toString()));
        }
        else if (!EnumCvaType.isNumber(type.toEnum()))
        {
            errorLog(expr.getLineNum(), "only basic type  can be multiply.");
        }
    }

    @Override
    public void visit(CvaConstTrueExpr expr)
    {
        this.type = EnumCvaType.CVA_BOOLEAN;
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
        ICvaType idType = this.type;
        if (!isMatch(idType, stm.getType()))
        {
            errorLog(stm.getLineNum(), String.format("the type of \"%s\" is %s, but the type of expression is %s. Assign failed.",
                    stm.getLiteral(), idType.toString(), stm.getType().toString()));
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
        if (type != EnumCvaType.CVA_BOOLEAN)
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
        if (EnumCvaType.isNumber(type.toEnum())
                || type.toEnum() == EnumCvaType.CVA_STRING)
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
        if (this.type != EnumCvaType.CVA_BOOLEAN)
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
        if (cvaMethod.getRetType() != EnumCvaType.CVA_VOID)
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
        if (mainMethod.getRetType() != EnumCvaType.CVA_VOID)
        {
            visit(mainMethod.getRetExpr());
            if (!isMatch(mainMethod.getRetType(), type))
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
