package cn.misection.utils

/**
 * @ClassName GeneSwitch* @author Military Intelligence 6 root
 * @version 1.0.0* @Description TODO* @CreateTime 2021年02月16日 13:12:00
 */
class GeneSwitch
{
    private static final String src =
            "        else if (s instanceof CvaAssign)\n" +
                    "        {\n" +
                    "            this.visit(((CvaAssign) s));\n" +
                    "        }\n" +
                    "        else if (s instanceof CvaBlock)\n" +
                    "        {\n" +
                    "            this.visit(((CvaBlock) s));\n" +
                    "        }\n" +
                    "        else if (s instanceof CvaIfStatement)\n" +
                    "        {\n" +
                    "            this.visit(((CvaIfStatement) s));\n" +
                    "        }\n" +
                    "        else if (s instanceof CvaWriteOperation)\n" +
                    "        {\n" +
                    "            this.visit(((CvaWriteOperation) s));\n" +
                    "        }\n" +
                    "        else // if (s instanceof Ast.While)\n" +
                    "        {\n" +
                    "            this.visit(((CvaWhileStatement) s));\n" +
                    "        }"
    ;
    private static final String INSTANCE_OF = "instanceof";



    private static String genePsfs(String src, String enumName, String formalArg)
    {
        StringBuilder builder = new StringBuilder();
        StringBuilder switchBuilder = new StringBuilder();

        def srcArray = src.split("\n");
        for (String single : srcArray)
        {
            if (single.contains(INSTANCE_OF))
            {
                String[] singleSplit = single.split();
                def indexOf = 4;
                String className =  singleSplit[indexOf];
                className = className.substring(0, className.length() - 1);
                builder.append(String.format(
                        "/**\n * %s\n */\npublic static final String %s = \"%s\";\n\n",
                        className, className.toUpperCase(), className
                ));

                switchBuilder.append(String.format(
                        "case %s.%s:\n{\nvisit((%s) %s);\nbreak;\n}\n",
                        enumName, className.toUpperCase(), className, formalArg
                ));
            }
        }
        builder.append(String.valueOf(switchBuilder));
        return String.valueOf(builder);
    }

    static void main(String[] args)
    {
//        println genePsfs(src, "CvaExpr", "e");
        println genePsfs(src, "CvaStatement", "s");
    }
}
