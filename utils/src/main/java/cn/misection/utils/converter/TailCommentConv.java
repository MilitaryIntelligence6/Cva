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
            "    private ViewPager adPager;  // 广告\n" +
                    "    private View adBannerLay;  // 广告条容器\n" +
                    "    private AdBannerAdapter adBannerAdapter;  // 适配器\n" +
                    "    public static final int MSG_AD_SLID = 002;  // 广告自动滑动\n" +
                    "    private ViewPagerIndicator vpi;  // 小圆点\n" +
                    "    private MHandler handler;  // 事件捕获"
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
