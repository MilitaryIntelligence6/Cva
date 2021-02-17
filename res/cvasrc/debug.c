// This is the entry point of the program

call cva.native.io.*;
call cva.std.console.*;

int main(String[] args)
{
    echo(new Test().Compute(10));   // just a print statement
    // return 0;
}


class Test
{
    int Compute(int num)
    {
        int total;
        if ( num < 1)
        {
            total = 1;
        }
        else
        {
            total = num * (this.Compute(num-1));
        }
        return total;
    }
}