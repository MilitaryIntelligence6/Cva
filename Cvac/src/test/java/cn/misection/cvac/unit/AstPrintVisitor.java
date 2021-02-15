package cn.misection.cvac.unit;

import cn.misection.cvac.ast.IVisitor;
import cn.misection.cvac.ast.clas.*;
import cn.misection.cvac.ast.decl.*;
import cn.misection.cvac.ast.entry.*;
import cn.misection.cvac.ast.expr.*;
import cn.misection.cvac.ast.method.*;
import cn.misection.cvac.ast.program.*;
import cn.misection.cvac.ast.statement.*;
import cn.misection.cvac.ast.type.*;

/**
 * Created by Mengxu on 2017/1/12.
 */
public class AstPrintVisitor implements IVisitor
{
    private int indentLevel = 4;

    private void indent()
    {
        indentLevel += 4;
    }

    private void unIndent()
    {
        indentLevel -= 4;
    }

    private void printSpaces()
    {
        // indentLevel, space数目;
        for (int i = indentLevel; i > 0; i--)
        {
            System.out.print(" ");
        }
    }

    @Override
    public void visit(CvaBoolean t)
    {
        System.out.print("boolean");
    }

    @Override
    public void visit(CvaClassType t)
    {
        System.out.print(t.getLiteral());
    }

    @Override
    public void visit(CvaInt t)
    {
        System.out.print("int");
    }

    @Override
    public void visit(CvaDeclaration d)
    {
        this.visit(d.getType());
        System.out.print(" " + d.getLiteral());
    }

    @Override
    public void visit(CvaAddExpr e)
    {
        this.visit(e.getLeft());
        System.out.print(" + ");
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaAndAndExpr e)
    {
        this.visit(e.getLeft());
        System.out.print(" && ");
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaCallExpr e)
    {
        this.visit(e.getExpr());
        System.out.print("." + e.getLiteral() + "(");
        for (int i = 0; i < e.getArgs().size(); i++)
        {
            if (i != 0)
            {
                System.out.print(",");
            }
            this.visit(e.getArgs().get(i));
        }
        System.out.print(")");
    }

    @Override
    public void visit(CvaFalseExpr e)
    {
        System.out.print("false");
    }

    @Override
    public void visit(CvaIdentifier e)
    {
        System.out.print(e.getLiteral());
    }

    @Override
    public void visit(CvaLTExpr e)
    {
        this.visit(e.getLeft());
        System.out.print(" < ");
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaNewExpr e)
    {
        System.out.print("new " + e.getLiteral() + "()");
    }

    @Override
    public void visit(CvaNegateExpr e)
    {
        if (e.getExpr() instanceof CvaIdentifier)
        {
            System.out.print("!");
            this.visit(e.getExpr());
        }
        else
        {
            System.out.print("!(");
            this.visit(e.getExpr());
            System.out.print(")");
        }
    }

    @Override
    public void visit(CvaNumberInt e)
    {
        System.out.print(e.getValue());
    }

    @Override
    public void visit(CvaSubExpr e)
    {
        this.visit(e.getLeft());
        System.out.print(" - ");
        this.visit(e.getRight());
    }

    @Override
    public void visit(CvaThisExpr e)
    {
        System.out.print("this");
    }

    @Override
    public void visit(CvaMuliExpr e)
    {
        this.visit(e.getLeft());
        System.out.print(" * ");
        if (e.getRight() instanceof CvaAddExpr || e.getRight() instanceof CvaSubExpr)
        {
            System.out.print("(");
            this.visit(e.getRight());
            System.out.print(")");
        } else this.visit(e.getRight());

    }

    @Override
    public void visit(CvaTrueExpr e)
    {
        System.out.print("true");
    }

    @Override
    public void visit(CvaAssign s)
    {
        this.printSpaces();
        System.out.print(s.getLiteral() + " = ");
        this.visit(s.getExpr());
        System.out.print(";");
        System.out.println();
    }

    @Override
    public void visit(CvaBlock s)
    {
        this.printSpaces();
        System.out.println("{");
        this.indent();
        for (AbstractStatement stm : s.getStatementList())
        {
            this.visit(stm);
        }
        this.unIndent();
        this.printSpaces();
        System.out.println("}");
    }

    @Override
    public void visit(CvaIfStatement s)
    {
        this.printSpaces();
        System.out.print("if (");
        this.visit(s.getCondition());
        System.out.print(")");
        System.out.println();
        this.indent();
        this.visit(s.getThenStatement());
        this.unIndent();
        this.printSpaces();
        System.out.println("else");
        this.indent();
        this.visit(s.getElseStatement());
        this.unIndent();
    }

    @Override
    public void visit(CvaWriteOperation s)
    {
        this.printSpaces();
        System.out.print("print(");
        this.visit(s.getExpr());
        System.out.print(");");
        System.out.println();
    }

    @Override
    public void visit(CvaWhileStatement s)
    {
        this.printSpaces();
        System.out.print("while (");
        this.visit(s.getCondition());
        System.out.print(")");
        System.out.println();
        this.visit(s.getBody());
    }

    @Override
    public void visit(CvaMethod m)
    {
        this.printSpaces();
        this.visit(m.getRetType());
        System.out.print(" " + m.getLiteral() + "(");
        // formalList是argList;
        for (int i = 0; i < m.getFormalList().size(); i++)
        {
            if (i != 0)
            {
                System.out.print(", ");
            }
            this.visit(m.getFormalList().get(i));
        }
        System.out.println(")");
        this.printSpaces();
        System.out.println("{");
        this.indent();
        for (AbstractDeclaration dec : m.getLocalList())
        {
            this.printSpaces();
            this.visit(dec);
            System.out.print(";");
            System.out.println();
        }
        for (AbstractStatement stm : m.getStatementList())
        {
            this.visit(stm);
        }
        this.printSpaces();
        System.out.print("return ");
        this.visit(m.getRetExpr());
        System.out.print(";");
        System.out.println();
        this.unIndent();
        printSpaces();
        System.out.println("}");
    }

    @Override
    public void visit(CvaClass cvaClass)
    {
        System.out.print("class " + cvaClass.getLiteral());
        if (cvaClass.getParent() != null)
        {
            System.out.printf(" : %s", cvaClass.getParent());
        }
        System.out.println();
        System.out.println("{");
        for (AbstractDeclaration dec : cvaClass.getFieldList())
        {
            printSpaces();
            this.visit(dec);
            System.out.println(";");
        }
        for (AbstractMethod method : cvaClass.getMethodList())
        {
            this.visit(method);
        }
        System.out.println("}");
    }

    @Override
    public void visit(CvaEntry c)
    {
        System.out.println("class " + c.getLiteral());
        System.out.println("{");
        System.out.println("    void main()");
        System.out.println("    {");
        this.indent();
        this.visit(c.getStatement());
        this.unIndent();
        System.out.println("    }");
        System.out.println("}");
    }

    @Override
    public void visit(CvaProgram cvaProgram)
    {
        this.visit(cvaProgram.getEntry());
        for (AbstractClass abstractClass : cvaProgram.getClassList())
        {
            this.visit(abstractClass);
        }
    }
}
