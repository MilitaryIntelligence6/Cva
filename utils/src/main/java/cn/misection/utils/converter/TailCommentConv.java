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

           "    private Folder current;//当前文件夹\n" +
                   "    private List<Folder> folders;//该文件夹内所有文件夹（嵌套的，以便于依次加载）\n" +
                   "    private List<Node> files;//该文件夹内所有文件"


            ;
    private static String conv(String src)
    {
        String[] split = src.split("\n");
        StringBuilder builder = new StringBuilder();
        for (String s : split)
        {
            String[] sSplit = s.split("//");
            builder.append(String.format("/**\n * %s\n */\n%s\n\n", sSplit[1], sSplit[0].trim()));
        }
        return String.valueOf(builder);
    }


    public static void main(String[] args)
    {
        System.out.println(conv(src));
    }
}
