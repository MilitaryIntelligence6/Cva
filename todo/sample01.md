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
// 这是一个 super test;
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
//            echo("hello, string");
//            echo(888);
            i = i + 1;
        }
        println("hello, string");
        return total;
    }
}
```

---
- result
```text
999
888
888
888
888
888
hello, string
1
999
888
888
888
888
hello, string
2
999
888
888
888
hello, string
3
999
888
888
hello, string
4
999
888
hello, string
999
hello, string
999
hello, string
hello, string
hello, string
hello, string
hello, string
3628800
```