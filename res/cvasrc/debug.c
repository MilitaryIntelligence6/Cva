// This is the entry point of the program

pkg cn.misection.cva.test;

call cva.native.io.*;
call cva.std.console.*;
/**
 * 原生string导入;
 *  由于是StringBuffer读的, 所以支持中文注释;
 */
call cva.lang.type.String;



class Increment
{
    string incre()
    {
//        int i = 0;
//        println "while test";
//        while(i < 10)
//        {
//            echo i;
//            echo "\t";
//            i++;
//        }
//        int j = 10;
//        while(j > 0)
//        {
//            echo j;
//            echo "  ";
//            j--;
//        }
//        println "if test";
//        if (j < 5)
//        {
//            echo "  ";
//            echo j;
//        }
//        println "for test";
        int k = 0;
        for (k = 0; k < 10; ++k)
        {
            echo k;
            echo "  ";
        }
//        return k;
        return "haha";
    }
}


int main(string[] args)
{
    echo "hello, world!\n";
//    new Increment().incre();

//    Increment cla = new Increment();

    println new Increment().incre();
//    int num = 888;
//    println num;
    return 0;
}