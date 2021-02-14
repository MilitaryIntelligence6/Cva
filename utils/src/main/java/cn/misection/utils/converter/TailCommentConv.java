package cn.misection.utils.converter;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName TailCommentConv
 * @Description TODO
 * @CreateTime 2021年02月14日 01:25:00
 */
final class TailCommentConv
{
    private static final String src =
            "public Kind kind; // the kind of the token\n" +
                    "    public String lexeme; // extra lexeme of the token\n" +
                    "    public int lineNum; // the line number of the token"
            ;

    private static String conv(String src)
    {
        String[] split = src.split("\n");
        StringBuilder builder = new StringBuilder();
        for (String s : split)
        {
            String[] sSplit = s.split("//");
            builder.append(String.format("/**\n *%s\n */\n%s\n\n", sSplit[1], sSplit[0].trim()));
        }
        return String.valueOf(builder);
    }


    public static void main(String[] args)
    {
        System.out.println(conv(src));
    }
}
