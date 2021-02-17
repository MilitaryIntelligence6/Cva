// This is the entry point of the program

pkg cn.misection.cva.test;

call cva.native.io.*;
call cva.std.console.*;

int main(String[] args)
{
    printf(new Test().Compute(10));   // just a print statement
    // return 0;
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
            echo(num);
        }
        else
        {
            total = num * (this.Compute(num-1));
        }
        if (num < 7)
        {
            println(999);
        }
        i = num;
        while (i < 5)
        {
            echoln(888);
            i = i + 1;
        }
        return total;
    }
}