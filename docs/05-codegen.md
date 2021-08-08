05.从0实现JVM语言之目标平台代码生成-CodeGenerator
===

## [源码github仓库, 如果这个系列对您有帮助, 希望获得您的一个star!](https://github.com/MilitaryIntelligence6/Cva)

## [本节相关代码生成package地址](https://github.com/MilitaryIntelligence6/Cva/tree/master/Cvac/src/main/java/cn/misection/cvac/codegen)

## [系列导读 00.一个JVM语言的诞生](https://www.cnblogs.com/misection/p/14429145.html)


## 阶段性的告别
```text
    非常感谢您能看到这里, 您的陪伴是我这几天坚持写作整合的动力! 但是所有的故事都有画上句号
的那一天, 我们这个系列也不例外, 今天就是一个给大家的阶段性停更, 因为我们代码优化部分以及一些
还没实现但是应该实现的部分(数组, printf等等)还会继续实现, 还有代码中有的编译优化可能也要隔
一段时间才能献上了, 主要原因是目前由于个人要准备春招实习了, 要暂时跟大家说一声告别, 今天这篇
结束以后可能会停更一段时间, 春招完后会继续更新这个系列以及其他系列
   
    之后这个系列还会频率更低地更新, 未来我打算出一个手写TCP/IP协议簇系列, 再出一个手写简单的
操作系统系列, 手写JVM系列, 如果时间足够, 应该还会出手写Tomcat系列, 哈哈这些太多了, 但是以前
没有深入了解很遗憾, 在这之前应有一个Android系列, Android应该就是写个博客园/github客户端, 安
卓真的是我一直想求而不得, 学校选课移动开发(Android), 我连选三个学期, 前两个学期直接课程冲了, 
第三个学期我能选的那个时间课程只有5个人选, 被撤销了, 我已经哭了, 学分已经修满了,以后大概率是
不会再选这课的, 因为时间该实习了 , 后面Android系列打算用Kotlin实现, 一些很前卫的巨头就是Kotlin 
first(据说字节就是), Kotlin的函数式, 协程以及空安全是更先进更舒服的, 当然个人也简单了解过一点
flutter, 所以到时候倾向于用Kotlin或者Flutter去完成这个项目

    更新的系列可能与复习同步, 当做复习练手, 但也可能暂时不会更新了, 要看我个人的复习进度, 我觉得
大概率是鸽, 不过大家放心, 过了春招这段时间我会继续坚持分享个人一些有趣的, 比较精心打磨, 完成度较好
的项目的

    非常感谢一些老读者的陪伴(虽然应该只有个位数的几位朋友), 是你们的坚持阅读才让我坚持写完了这个系列,
 也督促我不能每天都起码更个新, 不水大家

    也非常感谢您能忍受我一直以来自己都觉得丑的文笔和叙述, 哈哈, 您有什么宝贵意见都可以留言,
我会尽我所能改正或者提供支持
    
    所以项目就将时停止更新了, 让我们有缘再会!
```

```text
致亲爱的读者:

    个人的文字组织和写文章的功底属实一般, 写的也比较赶时间, 所以系列文章的文字可能比较粗糙,
难免有词不达意或者写的很迷惑抽象的地方 

    如果您看了有疑问或者觉得我写的实在乱七八糟, 这个很抱歉, 确实是我的问题, 您如果有不懂的地方
的地方或者发现我的错误(文字错误, 逻辑错误或者知识点错误都有可能), 可以直接留言, 我看到都会回复您!
```
## **系列食用方法建议**
```text
    由于时间原因, 目前测试并不完善, 所以推荐如下方式根据您的目的进行阅读

    如果您是学习用, 建议您先将整个项目clone到本地, 然后把感兴趣的章节删除, 自己重写对照着重写
    书写完每一步测试一下能否正常运行(在指定的路径去读取源码测试能否编译成功并在命令行执行

    java Application(类名)

尝试能否输出期望结果, 我没有研究Junit对编译器输出class文件进行测试, 所以目前可能需要您手动测试)

    按照以上步骤, 等您将所有模块重写一遍, 大概也对这个系列的脉络有深刻理解了! 如果您重头开始重写, 
往往可能由于出现某些低级错误导致长时间debug才找得到错误, 所以对于初学者, 推荐采用自己补写替换模块的
方式

    对于希望贡献代码的朋友或者对Cva感兴趣的朋友, 欢迎贡献您的源码与见解, 或者对于该系列一些错误/
bug愿意提出指正的朋友, 您可以留言或者在github发issue, 我看到后一定及时处理!

```

本节提纲
---

1. 引言-编译器前端的尾声

2. BST(Backend Abstract Syntax Tree)

3. Translator 

4. JVM指令

    4.1. 我们使用的指令介绍
    
    4.2. JVM汇编示例详解
    
5. Jasmin 汇编器生成 .class File

## 引言-编译器前端的尾声

在该步之前还应有编译期优化, 但是由于时间原因没有完善, debug时也一直没有开优化, 所以这部分以后有缘再
补充

代码生成是编译器前端的最后一步了, Javac编译器的编译工作到这一步, 便是将Java源码编译成JVM .class
字节码文件, 而我们这个阶段也是如此, 其实当我们拿到抽象语法树, 可以玩一点花的, 可以将Cva字节码编译到
 Java, Csharp, C语言甚至js等等

这是因为我们拿到的抽象语法树已经是一棵可执行的树了, 我们得到的源码文本转换得到的Program POJO是一个
有灵魂的POJO, 他能做的事就是你写代码时所表达的那些事, 当然, 我们也可以为其开发一个解释器, 执行这课
语法树(当这个解释器的构造与计算机类似, 拓展了许多与操作系统类似的功能如垃圾回收/JIT时, 其也可以叫做
虚拟机), 当然, 这里我们并不教大家如何去做这些工作, 如果你有兴趣可以尝试做一下, 我们这里就做一些大家
喜闻乐见的, 常规的编译器做法, 生成可执行的指令
  
如果是直接面向机器的编译器, 那么这个时候往往会生成指定平台的汇编(8086汇编, x86汇编)再进行接下来一步
的生成, 我们这里并不是直接生成JVM字节码(JVM平台 .class文件), 而是先生成JVM平台中间语言(Intermediate
 Language)JVM汇编指令, 再由汇编器(由中间语言/汇编语言生成机器码, 这个过程基本就是映射关系, 所以汇编指
 令也叫助记符), 我们这里其实也是面向机器, 不过这台机器是一个特殊的JVM虚拟机, JVM虚拟机其实也有着自己的
 一套汇编指令集及规范  

当然, 在编译器后端, 有着更为丰富精彩的世界, 比如运行时的编译, JIT, AOT等等, 还有着无底洞般的优化, 希望
以后有机会能给大家展现这些东西

同时, 这一节由于时间原因, 和多次重构的历史原因, 代码结构相对有一点乱, 个人将梳理讲解值得一些注意的部分


## BST(Backend Abstract Syntax Tree) 
   
cn.misection.cva.ast 是表示我们前端Cva源程序的抽象语法树, 但是这个东西表示的毕竟是我们自己定义的
Cva程序, 而不是我们的目标平台JVM可以识别的程序, 或者说, 跟我们目标有差异, 我们不可能直接将这棵语法树
用来在JVM平台执行或者编译到JVM上(当然我们可以针对这种程序结构开发解释器, 让其在我们的解释器/虚拟机上运行)

后端抽象语法树, 说是后端语法树, 其实过程还是属于前端, 只不过这里更接近虚拟机后端, 而且我们的
语法树由前端的树形被我们翻译成了后端的线性指令形式, 语法结构剧变, 所以我们需要一个 新的BST作为从AST
到指令的中间表示. BST主要关注点在Statement和Expression方面 

## Translator

说道 Translator, 顾名思义, 大家应该也能理解它的作用, 这是一个翻译官, 能将我们前端的ast翻译成后端的bst,
将树状的Cva程序翻译成线性的JVM指令, 他接受一个前端传入的CvaProgram, 生成一个后端的TargetProgram, 然
后交由IntermLangGenerator写入.il中间语言文件, 最后再交由Cvac 编译器的main方法静态调用jasmin的主方法
生成我们的.class字节码, 我们的编译器工作就完成啦!

其实现是impl后端的visitor, 这里使用到了(伪)访问者模式, 是一个设计的不成功的访问者模式, 大家看看就好, 
我以后有更好的方案会重构他


## JVM指令

由于JVM是基于栈式计算机概念的虚拟机, 以JVM为目标平台的代码生成较为简单, 不必考虑寄存器分配等问题; 同时JVM
还有着丰富的指令 

我们的语言由于支持的类型很有限, 支持的基本类型只有`int`和`boolean`(其实这俩都是int), 其他类型暂时没有过多
处理, 以及涉及到基本类型之间互相转换的操作指令可以不用考虑,我们支持的比较也比较少, 因此跳转指令可以只考虑部分,
 由于不存在一些特殊类型或者操作符, 比如`interface` `abstract` `instanceof`等等复杂的指令 

此外, JVM规范中有: 

Java Virtual Machine Specification - 2.3.4 The `boolean` type
> Although the Java Virtual Machine defines a boolean type, it only provides
very limited support for it. There are no Java Virtual Machine instructions solely
dedicated to operations on boolean values. Instead, expressions in the Java
programming language that operate on boolean values are compiled to use values
of the Java Virtual Machine int data type.

其实后端是没有boolean的, 直接用int 型 0 1 表示即可, 这也是为啥C语言只有0和非0的原因了, 
感觉我们似乎越来越接近世界的真相了, 哈哈

### 我们使用的指令介绍
按照JVM规范, JVM支持共计大约150个指令, 我们用到的指令仅仅是相当小的一个子集, 先给出我们使用的指令

```text
aload
areturn
astore
getfield
goto <label>

iinc

iadd
isub
imul
idiv
iand
ior
ixor
irem
ishl
ishr
iushr

if_icmplt <label>
iload
invokespecial
invokevirtual
ireturn
istore
ldc
new
putfield

// 未来需要支持的
anewarray(数组操作)
```

简单地解释一下这些指令

我们可以把一条指令分成两部分看,  比如 iadd, 其实在JVM指令的语义中, 这条指令反映了两个信息 i 和add,
 i 其实就是int的意思, add 是指将栈顶的两个操作数相加, 所以这条指令的意思就是将栈顶的两个 int 型操作
 数相加, 理解了这条指令, 其他指令也就不难了, 他们都只是这样一个个简单的容易理解的映射关系
 
 其他的数据类型如byte, 在虚拟机指令层面就是b(作为操作数操作层面), 都在下面的EnumOperandType中
 当然, 这些指令是怎么得到的呢, 可以选择看书, 如果没有书籍或者课程资源怎么办呢, 除了去网上捣鼓电子书
 之外, 我们也可以利用现成的JDK提供给我们的工具javap
 
 授人以鱼不如授人以渔, 我们这里提供一个理解底层操作的思路
 
 比如说, 你想知道long型的左移操作底层是使用哪条指令
 可以写一个LongLS.java
 ```java
/**
 * LongLS.java
 */
class LongLS {
    public static void main(String[] args) {
        // 也可以放在方法中;
        // 一次多放几条, 能事半功倍查看原理
        long aLong = 1;
        long anotherLong = aLong << 1;
    }
}
```
然后在命令行执行  
```shell script
javac LongLS.java
```
然后 
```shell script
javap -c LongLS.class
```
可以得到

**PS: 如果上述JVM指令您感到比较生涩, 您可以跳到文章最后根据我们的示例代码完全详解熟悉JVM汇编指令**
```text
Compiled from "LongLS.java"
class LongLS {
  LongLS();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: lconst_1
       1: lstore_1
       2: lload_1
       3: iconst_1
       4: lshl
       5: lstore_3
       6: return
}
```

如果希望得到更详细的信息, 可以使用-v参数
```shell script
javap -v LongLS.class
```
得到
```text
Classfile /***/batTest/javap/demo/LongLS.class
  Last modified 2021-3-4; size 271 bytes
  MD5 checksum cc920428a4ba6c860e039dfd79252c53
  Compiled from "LongLS.java"
class LongLS
  minor version: 0
  major version: 52
  flags: ACC_SUPER
Constant pool:
   #1 = Methodref          #3.#12         // java/lang/Object."<init>":()V
   #2 = Class              #13            // LongLS
   #3 = Class              #14            // java/lang/Object
   #4 = Utf8               <init>
   #5 = Utf8               ()V
   #6 = Utf8               Code
   #7 = Utf8               LineNumberTable
   #8 = Utf8               main
   #9 = Utf8               ([Ljava/lang/String;)V
  #10 = Utf8               SourceFile
  #11 = Utf8               LongLS.java
  #12 = NameAndType        #4:#5          // "<init>":()V
  #13 = Utf8               LongLS
  #14 = Utf8               java/lang/Object {
  LongLS();
    descriptor: ()V
    flags:
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 1: 0

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=3, locals=5, args_size=1
         0: lconst_1
         1: lstore_1
         2: lload_1
         3: iconst_1
         4: lshl
         5: lstore_3
         6: return
      LineNumberTable:
        line 6: 0
        line 7: 2
        line 8: 6
}
SourceFile: "LongLS.java"
```

这样我们就得到了long型左移操作指令应该是 `lshl`, long型进行操作时的助记符应该为 `l`

同理, 我们希望获得基本类型在方法签名中的表示时, 或者希望深入研究查看某个方法的具体细节,
 看某些操作是否是原子性的时候, 看有些操作是否线程安全的时候, 也可以使用这个办法去反汇编
 class文件

 下面便是使用桥接模式桥接操作数和操作符的两个枚举(当然, 我们这个桥接模式并没有用实体类将其组合起来(因为
 这些命令是不会有变数的的, 创建对象会浪费资源), 直接在translator中压入命令队列(用的是list))

```java
package cn.misection.cvac.codegen.bst.instructor.operand;

import cn.misection.cvac.codegen.bst.instructor.IInstructor;
import cn.misection.cvac.codegen.bst.instructor.Instructable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumTargetOperand
 * @Description 桥接模式底层操作数;
 * @CreateTime 2021年02月21日 22:23:00
 */
public enum EnumOperandType implements IInstructor, Instructable {
    /**
     * 底层操作数类型;
     */
    VOID(""),

    BYTE("b"),

    SHORT("s"),

    CHAR("c"),

    INT("i"),

    LONG("l"),

    FLOAT("f"),

    DOUBLE("d"),

    REFERENCE("a"),
    ;

    private final String typeInst;

    EnumOperandType(String typeInst) {
        this.typeInst = typeInst;
    }


    @Override
    public String toInst() {
        return typeInst;
    }
}


```

```java
package cn.misection.cvac.codegen.bst.instructor.operand;

import cn.misection.cvac.codegen.bst.instructor.IInstructor;
import cn.misection.cvac.codegen.bst.instructor.Instructable;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumOperator
 * @Description 桥接模式底层操作符;
 * @CreateTime 2021年02月21日 22:24:00
 */
public enum EnumOperator implements IInstructor, Instructable {
    /**
     * 底层操作符;
     */

    ADD("add"),

    SUB("sub"),

    MUL("mul"),

    DIV("div"),

    /**
     * 求余;
     */
    /*
     * neg 其实不应该出现, 其是一元的;
     */
    BIT_NEG("neg"),

    REM("rem"),

    BIT_AND("and"),

    BIT_OR("or"),

    BIT_XOR("xor"),

    LEFT_SHIFT("shl"),

    RIGHT_SHIFT("shr"),

    /**
     * 无符号右移;
     */
    UNSIGNED_RIGHT_SHIFT("ushr"),

    RETURN("return"),
    ;
    
    private final String opInst;

    EnumOperator(String opInst) {
        this.opInst = opInst;
    }

    @Override
    public String toInst() {
        return opInst;
    }
}

```

他们都实现接口 Instructable, 表示能将该元素转换成JVM汇编指令
```java
@FunctionalInterface
public interface Instructable {
    /**
     * 获得该类型指令;
     * @return instruct;
     */
    String toInst();
}
```

同时他们实现 IInstructor 接口
这个接口是为了能在假的访问者模式(这个访问者模式一个失败的访问者模式)有一个统一的传入父接口

我们使用到的指令是相当精简的. 但是, 以下两条扩展"指令"值得我们注意.

```text
label
```

这条"指令"的存在, 是交由下文提到的`Jasmin`处理

```text
// 不是写入il文件的write, 而是标准输出流打印指令;
write
```

这条"指令"是我们纯粹为了编程简单而扩展的. 考虑到我们的语言的特点, 将他们作为指令对待, 能够大大简化我们编程处理的复杂度, 而且也不会造成任何的副作用, 因为之前几个阶段的分析已经保证了源程序的合法. 这样用不会有问题, 因为后面我们会将扩展的指令翻译成jvm指令. 

## Code Translation

有了上文的"完善"的指令集(对于我们这个程序来说), 我们接下来的工作便是将树状的AST转换成线性的指令. 这里的转换主要关注的方法中的真正"干活"的代码, 就是方法体内部的工作代码. 下面通过一个例子展示具体工作. 

```java
class Recursor {
    int compute(int num) {
        int total;
        if ( num < 1) {
            total = 1;
        }
        else {
            total = num * (this.compute(num - 1));
        }
        return total;
    }
}

/**
 * This is the entry point of the program
 */
int main(string[] args) {
    // fib(10);
    println new Recursor().compute(10);   
    return 0;
}
```

### JVM汇编示例详解
观察以上一段代码, 我们主要关注`compute`方法编译出的指令, 而且给出了较详细的注释. 

```nasm
.method public Compute(I)I ; 方法签名传入参int, 返回int;
.limit stack 4096 ; 栈调用深度, 由于目前还没有实现该算法, 因此编译结果给出默认值 4096
.limit locals 4 ; 本地变量的编号一共4个, 后面的iload 1 往往是 iload var1而不是常量1
    ; num < 1 对于if语句中的判别式进行计算
    iload 1     ; 从本地变量表中加载变量1的值(num)到栈顶
    ldc 1       ; ldc 即 load const将整型数字1压入栈
                ; 事实上, 我们需要完成的一个TODO就是优化这些常数的加载
                ; JVM对于常量的加载
                ; 取值 -1~5 采用 iconst 指令, -1是iconst_m1, 其他则如iconst_1;
                ; 取值 -128~127 采用 bipush 指令(byte取值区间, 下同
                ; byte num >= Math.pow(2, 8) && num < Math.pow(2, 8));
                ; 取值 -32768~32767 采用 sipush指令;
                ; 取值 -2147483648~2147483647 采用 ldc 指令;

    if_icmplt Label_2 ;比较两个值, 如果第一个值(num)小于整数1, 跳转至Label_2
    ldc 0       ; 将整数0压入栈(用于表示比较结果为false)
    goto Label_3 ; 否则跳转到label_3
Label_2:
    ldc 1       ; 将整数1压入栈(用于表示比较结果为真)
Label_3:        ; 判别式计算完成
    ldc 1
    if_icmplt Label_0 ; 对于求值真假进行计算
    ldc 1       ; 将整数1压入栈
    istore 2    ; 将栈上的数字存入本地变量2
    goto Label_1
Label_0:
    iload 1     ; 从本地变量表中加载变量1的值 (num)
    aload 0     ; 从本地变量表中加载变量0的值 (this)
    iload 1
    ldc 1
    isub
    invokevirtual FibCalcer/Compute(I)I ; 调用实例方法(在指令参数处指出了方法的从属及签名)
    imul        ; 栈顶两个int型相乘, 并将结果压入栈顶
    istore 2
Label_1:
    iload 2     ; 从本地变量表中加赞变量2的值
    ireturn     ; 从方法返回. 
.end method

; main 方法
.class public Application
.super java/lang/Object
.method public static main([Ljava/lang/String;)V ; main方法返回V, void, Cva中的int只是致敬C语言;
.limit stack 4096
.limit locals 2
    ldc "fib(10) is " ; 加载字符串常量;
    getstatic java/lang/System/out Ljava/io/PrintStream; ; 打印的System.out 放到栈顶
    swap ; 由于打印必须要被打印者在栈顶, PrintStream在其下, 所以交换, 以后考虑优化;
    invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V ; 调用虚方法
    ; 对于 private 方法和构造方法<init> 都是invokespecial调用
    ; invokevirtual 调用实例方法, 包括父类方法, 抽象类的抽象方法实现;
    ; invokeinterfacre 调用接口的抽象方法
    ; invokestatic 调用类方法
    ; invokedynamic Java7之后才有的动态方法调用指令
    new Recursor ; 创建对象;
    dup ; dup指令为复制操作数栈顶值，并将其压入栈顶，也就是说此时操作数栈上有连续相同的两个对象地址;
        ; 这是因为一会有出栈操作, 保留一个副本;
    invokespecial Recursor/<init>()V
    ldc 10
    invokevirtual Recursor/compute(I)I
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(I)V
    return ; 相当于void return, 这个指令只能用于void
           ; 如果要返回 int, 像上面ireturn
           ; 如果是引用类型 则是areturn;
.end method
```

## Jasmin 汇编器生成 .class File

最后的工作就是从字符形式的指令, 由汇编器转换成二进制形式的.class文件, 用于jvm的运行,
这个汇编器是一个现成的工具, 这个工具其实也是一个命令行应用, 如果使用脚本去粘合我们将面临
性能问题和不少通信上的问题, 我们的方法是在cvac的main方法中静态调用该汇编器的main方法
直接传入src文件路径作为其命令行参数
[Jasmin](http://jasmin.sourceforge.net/). 

您也可以在命令行使用之
```shell script
java -jar jasmin.jar fileName
```
```text
// jasmin 使用参数
usage: jasmin [-d <outpath>] [-g] [-e <encoding>] <
file> [<file> ...]
   or: jasmin -version
   or: jasmin -help
```

至此, 代码生成阶段就告一段落, 我们的系列也画上了一个并不圆满的句号, 
后续春招忙完后我可能将继续更新该系列, 感谢您的阅读与关注!
