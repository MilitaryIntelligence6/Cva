00.一个JVM语言的诞生
===

由于方才才获悉博客园文章默认不放在首页的, 原创文章主要通过随笔显示, 所以将文章迁移到随笔;

这篇帖子将后续更新, 欢迎关注! 这段时间要忙着春招实习, 所以项目更新会慢一点, 
语言组织也会比较随意, 毕竟时间有限, 没办法太过雕琢琢磨

## [源码github仓库, 如果这个系列对您有帮助, 希望获得您的一个star!](https://github.com/MilitaryIntelligence6/Cva)

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

    如果您是学习用, 建议您先将整个项目clone到本地, 然后把感兴趣的章节删除/移动, 自己对照着重写
模块, 书写完每一步测试一下能否正常运行(在指定的路径去读取源码测试能否编译成功并在命令行执行

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
0. 写在前面

1. 文章目录

2. 先修技能/工具

3. Cva文法-grammar推导式

4. 说明

5. Cva程序样例

## 写在前面

从屏幕前的你搜索到这篇博文开始, 我们就要开始一篇实现自己的JVM语言系列了, 目前国外有大佬做了
一个Enkel语言, 还有翻译教程, 不过恕我直言教程太跳了, 很多细节语焉不详, 也可能是翻译的原因, 
所以我希望写一个国人的自制JVM语言系列教程, 在这之间我已经完成了github项目, 语言已经能实现很多
基本的Java的特性(方法, 循环, 简单的面向对象与继承), 你可以直接使用之, 在此基础上构建你的项目,
或者大家可以一起提交修改, 让Cva成为一个更棒的语言我们将其名字暂定为Cva, 开始也有想过比较霸气的alloy,
但是看到这好像已经是一个语言了, 当然, 目前我们的语言这么菜, 也不好意思叫Alloy(其实叫Cva都是我脸皮厚,
哈哈 

为什么叫Cva, 其实我一开始的设想是这个语言的最终目的是兼容C(但愿吧, 毕竟C的类型系统和很多糟粕还是没必要学着), 
目前是相当小相当菜的一个子集, 毕竟C的语法还是相当复杂, 现在毕业找工作的压力比较大, 所以这个项目
真正对C兼容还是有很长的路要走的

不过人嘛, 梦想总是要有的, 写一个编译器有一个自己的语言何不是很多程序员年少的梦想? 为了写这个语言,
 笔者从去年11月开始尝试了很多方法, 有github上其他同学的大作业, 有读龙书虎书鲸书, <两周自制脚本语言>,
 有网易云Coding 迪斯尼的自底向上, 前前后后推翻了很多方案, 改了很多版本, (说实话自底向上真的太抽象了,
 相当劝退), 最终完成这个项目却不过区区十来天, 主要是过年那段时间, 每天废寝忘食地写代码提交,
 经常半夜构思想的睡觉都睡不着, 经过长时间的积累
 
 最终算是勉强完成了一个差强人意的版本把, 在这里由于春招临近的缘故, 笔者不得不收心去准备春招实习面试, 
 所以把这个项目的教程写完后可能就暂时停止更新, 至于再见的时间? 我也不清楚, 可能是春招结束放荡的大四
 (但是万一要提前去公司实习干活呢?), 所以有点遥遥无期的意味
 
 不过这些文章会将一个编译器从0到1的实现过程, 尽可能简单易懂地展现给大家, 当你读完这个系列的文章, 
 理解了编译器(其实本文基本都是前端, 后端基本都被JVM做完了, 后面我可能会写一个自制JVM系列, 
 不过在这之前还是先推出自制操作系统和实现TCP/IP协议栈系列吧)加上我前期调教好的代码,相信即使你是小白, 
 也能快速定制自己的需求, 快速书写出一个自己的语言!
 
 
## 文章目录

---
### [00.一个JVM语言的诞生](https://www.cnblogs.com/misection/p/14429145.html)
### [01.从0实现一个JVM语言之架构总览](https://www.cnblogs.com/misection/p/14429158.html)
### [02.从0实现一个JVM语言之词法分析器-Lexer](https://www.cnblogs.com/misection/p/14458592.html)
### [03.从0实现一个JVM语言系列之语法分析器-Parser](https://www.cnblogs.com/misection/p/14461275.html)
### [04.从0实现一个JVM语言系列之语义分析-Semantic](https://www.cnblogs.com/misection/p/14469791.html)
### [05.从0实现一个JVM语言之目标平台代码生成-CodeGenerator](https://www.cnblogs.com/misection/p/14483009.html)


 
## 先修技能/工具

1. 比较熟练的JavaSE基础即可

2. JDK1.8及以上版本, 源码中有大量利用了Java8函数式特性的地方

3. jasmin汇编器, 在github仓库的lib目录下
 
## Cva文法

Grammar 推导式

```text
Program
   -> pkg com.company.proj; // 包名目前是可有可无的. 没啥用
   -> call com.company.somepkg; // 导包目前也是摆设, 可有可无 
   -> EntryClass(入口类, 拥有main方法, 位置是任意的)
   | EntryMethod
   | ClassDeclList

EntryClass
   -> class Entry(默认为Application) 
    { 
        retType main() 
        { 
            StatementList 
        }
    }

ClassDeclList
   -> ClassDecl
   | ClassDecl ClassDeclList

ClassDecl
   -> class Identifier 
    { 
        VarDeclList MethodDeclList 
    }
   | class Identifier : Identifier 
    { 
        VarDeclList MethodDeclList 
    }

VarDeclList
   -> VarDecl
   | VarDecl VarDeclList
   |

VarDecl -> Type Identifier;

MethodDeclList
   -> MethodDecl
   | MethodDecl MethodDeclList
   |

MethodDecl -> Type Identifier (FormalList)
             {
                 VarDeclList StatementList return Expr;
             }

FormalList
   -> Type Identifier
   // formal就是形参, args;
   | Type Identifier, FormalList
   |

Type -> int
     -> boolean
     -> string
     ... 其他的目前还不支持
     -> Identifier

StatementList
   -> Statement
   | Statement StatementList
   |

Statement
   -> { StatementList }
   // 多条语句构成Block, 要加上大括号限定域;
   | if (Expr) Statement else Statement
   | while (Expr) Statement
   // write 也可以是writeln, writef, writef目前不支持;
   | write(Expr); 
   | Identifier = Expr;

Expr
   -> Expr Op Expr
   | Expr.Identifier(ExprList)
   | IntegerLiteral
   | Identifier
   | this
   | new Identifier()
   | Const ...(包括const int, true, false)
   | !Expr
   | (Expr)

Op -> +
   | -
   | *
   | /
   | %
   | <
   | >
   | &
   | |
   | ^
   | >>
   | <<
   | >>>
   | &&

ExprList
   -> Expr
   | Expr, ExprList
   |

Identifier -> [A-Za-z_][A-Za-z0-9_]*

IntegerLiteral -> [0-9]+

LineComment -> // the total line is comment
```

## 说明

+ 目前Cva支持 `int` `boolean` 两种基本类型, 进阶类型支持 `string` 支持自定义的类类型
+ 在类类型中支持字段和实例方法, 不支持静态方法, 不支持方法重载/重写, 
    不支持访问权限控制(默认为 `public` ), 支持继承(若无显式父类声明, 
    则默认继承自 `Object` 类型), 但不支持显式调用父类方法
+ 支持 `if/if-else` `while` `for` 的分支和循环控制, 支持方法调用)
+ 运算符支持 基本运算 `+` `-` `*` `/` `%` `&&` `=`  `.` `!` `<`  
             自增运算 `++` `--`
             以及位操作运算 `&` `|` `^` `>>` `<<` `>>>` 等其他运算符  
             域限定符 `{` `}` `(` `)` `;``,`    比较运算支持 `<`, 逻辑运算支持 `&&` `!`
+ 支持引导的C家族的 `//` 行注释以及 `/* ... */` 块注释
+ 字段和变量仅支持声明后赋值或者声明时初始化
+ 编译器目前仅支持将所有代码写在同一个文件内部, 需要有一个入口main方法(可以没有main类, 
    仅有main方法, 但编译器会自动生成Application类), main 方法支持空参, 也支持仅有一个
    string[]传入命令行参, 当然目前也没有去处理, 只是个摆设

由以上文法给出的Cva程序样例
---

```java
class Entry
{
    /**
     * This is the entry point of the program;
     */
    int main(string[] args)
    {
        echo "Hello, CvaWorld\n";
        // 打印整形 statement
        println(new FibCalcer().compute(10));
        return 0;
    }
}

class FibCalcer
{
    int compute(int num)
    {
        int total;
        if ( num < 1)
        {
            // 花括号是可以不要的, 但是我们的代码要遵守阿里巴巴Cva规范(滑稽);
            total = 1;
        }
        else
        {
            total = num * (this.compute(num - 1));
        }
        // 目前不支持提前 return, 挺遗憾的;
        return total;
    }
}
```
当然, main方法也有野的写法, 但目前Cva仅支持将main方法这么写
```java
int main(string[] args)
{
    echo "Hello, CvaWorld\n";
    return 0;
}
```
即main类不需要定义, 只需要声明main方法即可, 当然, 这不过是语法糖一颗, 
编译时会自动加上默认主类名Application(这样会出现的名称冲突问题暂时还没解决)
