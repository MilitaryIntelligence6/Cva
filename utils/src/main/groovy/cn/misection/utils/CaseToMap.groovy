package cn.misection.utils

/**
 * @ClassName CaseToMap* @author Military Intelligence 6 root
 * @version 1.0.0* @Description TODO* @CreateTime 2021年02月16日 17:25:00
 */
class CaseToMap
{
    private static final String src =
            "                    case \"boolean\":\n" +
                    "                        return new CvaToken(CvaKind.BOOLEAN, lineNum);\n" +
                    "                    case \"class\":\n" +
                    "                        return new CvaToken(CvaKind.CLASS, lineNum);\n" +
                    "                    case \"else\":\n" +
                    "                        return new CvaToken(CvaKind.ELSE, lineNum);\n" +
                    "                    case \"false\":\n" +
                    "                        return new CvaToken(CvaKind.FALSE, lineNum);\n" +
                    "                    case \"if\":\n" +
                    "                        return new CvaToken(CvaKind.IF, lineNum);\n" +
                    "                    case \"int\":\n" +
                    "                        return new CvaToken(CvaKind.INT, lineNum);\n" +
                    "                    case \"main\":\n" +
                    "                        return new CvaToken(CvaKind.MAIN, lineNum);\n" +
                    "                    case \"new\":\n" +
                    "                        return new CvaToken(CvaKind.NEW, lineNum);\n" +
                    "                    case \"echo\":\n" +
                    "                        return new CvaToken(CvaKind.WRITE, lineNum);\n" +
                    "                    case \"return\":\n" +
                    "                        return new CvaToken(CvaKind.RETURN, lineNum);\n" +
                    "                    case \"this\":\n" +
                    "                        return new CvaToken(CvaKind.THIS, lineNum);\n" +
                    "                    case \"true\":\n" +
                    "                        return new CvaToken(CvaKind.TRUE, lineNum);\n" +
                    "                    case \"void\":\n" +
                    "                        return new CvaToken(CvaKind.VOID, lineNum);\n" +
                    "                    case \"while\":\n" +
                    "                        return new CvaToken(CvaKind.WHILE, lineNum);";

    private static String caseToMap(String src)
    {
        StringBuilder builder = new StringBuilder();
        String[] split = src.split("\n");
        for (int i = 0; i < split.length; i += 2)
        {
            String typeRaw = split[i];
            String cvaKindRaw = split[i + 1];
            String type = typeRaw.split()[1];
            type = type.substring(0, type.length() - 1);
            String cvaKind = cvaKindRaw.split()[2];
            String kind = cvaKind.substring(9, cvaKind.length() - 1);
            builder.append(String.format("this.put(%s, %s);\n", type, kind));
        }
        return String.valueOf(builder);
    }

    static void main(String[] args)
    {
        println caseToMap(src);
    }
}
