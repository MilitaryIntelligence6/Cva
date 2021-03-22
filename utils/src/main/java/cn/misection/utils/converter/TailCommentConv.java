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
            "    private String srcAddress; //源地址\n" +
                    "    private String destAddress; //目标地址\n" +
                    "    private int srcPort; //源端口\n" +
                    "    private int destPort; //目标端口\n" +
                    "    private byte[] header; //头部字节数组\n" +
                    "    private byte[] payload; //数据字节数组\n" +
                    "    private PacketType packetType; //包类型\n" +
                    "    private LocalDateTime timestamp; //时间戳"
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
