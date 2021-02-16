/**
 * @ClassName package-info
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @Description TODO
 * @CreateTime 2021年02月14日 15:15:00
 */
package cn.misection.cvac.lexer;

import java.util.HashMap;

/**
 * 前端常量类;
 */
final class LexerConstPool
{
    public static final char NEW_LINE = '\n';

    public static final char SPACE = ' ';

    private LexerConstPool() {}
}

// 可惜了双检锁单例模式;
//final class CvaTokenMap extends HashMap<String, CvaKind>
//{
//    private static CvaTokenMap instance = null;
//
//    private CvaTokenMap()
//    {
//        init();
//    }
//
//    public static CvaTokenMap getInstance()
//    {
//        if (instance == null)
//        {
//            synchronized (CvaTokenMap.class)
//            {
//                if (instance == null)
//                {
//                    instance = new CvaTokenMap();
//                }
//            }
//        }
//        return instance;
//    }
//
//    private void init()
//    {
//        this.put("call", CvaKind.CALL);
//
//        this.put("class", CvaKind.CLASS);
//
//        this.put("if", CvaKind.IF);
//        this.put("else", CvaKind.ELSE);
//
//        this.put("main", CvaKind.MAIN);
//
//        this.put("new", CvaKind.NEW);
//
//        this.put("echo", CvaKind.WRITE);
//        this.put("printf", CvaKind.WRITE);
//        this.put("println", CvaKind.WRITE);
//
//        this.put("return", CvaKind.RETURN);
//        this.put("this", CvaKind.THIS);
//        this.put("true", CvaKind.TRUE);
//        this.put("false", CvaKind.FALSE);
//
//        this.put("void", CvaKind.VOID);
//        this.put("byte", CvaKind.BYTE);
//        this.put("int", CvaKind.INT);
//        this.put("boolean", CvaKind.BOOLEAN);
//
//        this.put("while", CvaKind.WHILE);
//        // 后面加;
//        this.put("for", CvaKind.FOR);
//    }
//}
//
//
//final class NonPrefixCharMap extends HashMap<Character, CvaKind>
//{
//    private static NonPrefixCharMap instance = null;
//
//    private NonPrefixCharMap()
//    {
//        initMap();
//    }
//
//    public static NonPrefixCharMap getInstance()
//    {
//        if (instance == null)
//        {
//            synchronized (NonPrefixCharMap.class)
//            {
//                if (instance == null)
//                {
//                    instance = new NonPrefixCharMap();
//                }
//            }
//        }
//        return instance;
//    }
//
//    private void initMap()
//    {
//        this.put(';', CvaKind.SEMI);
//        this.put('[', CvaKind.OPEN_BRACKETS);
//        this.put(']', CvaKind.CLOSE_BRACKETS);
//        this.put('(', CvaKind.OPEN_PAREN);
//        this.put(')', CvaKind.CLOSE_PAREN);
//        this.put('{', CvaKind.OPEN_CURLY_BRACE);
//        this.put('}', CvaKind.CLOSE_CURLY_BRACE);
//        this.put('?', CvaKind.QUEST);
//        this.put(':', CvaKind.COLON);
////        this.put('\n', CvaKind.WHITE_SPACE);
////        this.put('\t', CvaKind.WHITE_SPACE);
////        this.put(' ', CvaKind.WHITE_SPACE);
//        this.put(',', CvaKind.COMMA);
//        this.put('.', CvaKind.DOT);
//    }
//}