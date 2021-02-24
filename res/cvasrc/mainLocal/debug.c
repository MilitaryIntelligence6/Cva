// This is the entry point of the program

pkg cn.misection.cva.test;

call cva.native.io.*;
call cva.std.console.*;
/**
 * 原生string导入;
 *  由于是java StringBuffer读的, 所以支持中文注释;
 */
call cva.lang.type.String;



int main(string[] args)
{
//    int main = 5;
//    println main;
    println "hello, world"
    return 0;
}
