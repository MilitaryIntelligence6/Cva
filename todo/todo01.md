
```java
// This is the entry point of the program

pkg cn.misection.cva.test;

call cva.native.io.*;
call cva.std.console.*;
/**
 * 原生string导入;
 */
call cva.lang.type.String;

/**
 * block comm;
 */
int main(string[] args)
{
//    println("hello, string");
//    printf(1);
    println(new Test().Compute(10));   // just a print statement
    // return 0;
//    echo("hello, string");
}


class Test
{
    int Compute(int num)
    {
        int total;
        int i;
        if ( num < 1)
        {
            total = 1;
        }
        else if (num < 5)
        {
            total = num * (this.Compute(num-1));
            println(num);
        }
        else
        {
            total = num * (this.Compute(num-1));
        }
        if (num < 7)
        {
            println(999);
//            echo(999);
        }
        i = num;
        while (i < 5)
        {
            println(888);
            echo("hello, string");
//            echo(888);
            i = i + 1;
        }
//        println("hello, string");
        return total;
    }
}
```
会错误的打印成
```text
999
888
888888
888888
888888
888888
8881
999
888
888888
888888
888888
8882
999
888
888888
888888
8883
999
888
888888
8884
999
888
888999
999
3628800
```
888冒出来的地方应该是Hello, String;

println放在其他地方就还好;
