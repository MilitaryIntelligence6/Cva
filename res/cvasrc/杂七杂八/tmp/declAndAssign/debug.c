// This is the entry point of the program

pkg cn.misection.cva.test;

call cva.native.io.*;
call cva.std.console.*;
/**
 * 原生string导入;
 *  由于是java StringBuffer读的, 所以支持中文注释;
 */
call cva.lang.type.String;

class Increment
{
    int incre()
    {
        int i;
        i = 0;
        while (10 > i)
        {
            println i;
            i++;
        }
        println "i += 2";
        i += 2;
        println i;

        println "i -= 2";
        i -= 2;
        println i;

        println "i *= 2";
        i *= 2;
        println i;

        println "i /= 2";
        i /= 2;
        println i;

//        println "i ~= 2";
//        i += 2;
//        println i;

        println "i &= 2";
        i &= 8;
        println i;

        println "i |= 2";
        i |= 2;
        println i;

        println "i ^= 2";
        i ^= 2;
        println i;

        println "i >>= 2";
        i >>= 2;
        println i;

        println "i <<= 2";
        i <<= 2;
        println i;

        println "i >>>= 2";
        i >>>= 2;
        println i;

        while(i < 15)
        {
            i++;
        }
        println "i % 4";
        i %= 4;
        println i;
        return i;
    }
}
