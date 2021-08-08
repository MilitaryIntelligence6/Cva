03.从0实现JVM语言之语法分析器-Parser
===

## 相较于之前有较大更新, 老朋友们可以复盘或者针对bug留言, 我会看到之后答复您!

## [源码github仓库, 如果这个系列对您有帮助, 希望获得您的一个star!](https://github.com/MilitaryIntelligence6/Cva)

## [本节相关语法分析package地址](https://github.com/MilitaryIntelligence6/Cva/tree/master/Cvac/src/main/java/cn/misection/cvac/parser)

## [本节相关前端语法树package地址](https://github.com/MilitaryIntelligence6/Cva/tree/master/Cvac/src/main/java/cn/misection/cvac/ast)

## [系列导读 00.一个JVM语言的诞生](https://www.cnblogs.com/misection/p/14429145.html)

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
1. 抽象语法树介绍

    1.1. 简介
    
    1.2. Cva程序的树形结构示意图
    
    1.3. ast包内详细介绍及继承关系

2. 语法分析实现

    2.1. 递归下降分析算法
    
    2.2. 递归下降算法解析Cva代码
    
    2.3. 语法分析实例简析
    
    2.4. 给出语法错误提示及警告
    
3. 设计模式浅析
    
    3.1. ast中的设计模式浅析

## 抽象语法树介绍

### 简介

前面我们实现了词法分析器, 会发现词法分析器的功能是有限的, 他只能像个复读机或者说
翻译官一样将我们的源代码按照一定的规则切割成Token流, 但是我们的代码其实是树状的结构, 
为了将代码抽象成树状的POJO(我们的前端ast的Program类), 这个类可以延伸出我们代码的
每一个节点, 每一个类, 每一个方法, 每一个方法本地变量, 每一个运算符, 他是以
Program这个节点为root的一棵树, 这棵树叫做抽象语法树(AST:abstract syntax tree),
语法分析器将用户(程序员)写的这段代码按照我们的语法规则进行分层次理解转化成方便后期处理的
一种中间表示, 这个中间表示是层次分明的树形结构, 就是我们的抽象语法树  

我们这里没有画图, 按照树的层次, 我们这里采用自顶向下语法分析, 这课树的各个节点与我们
表示Cva语言文法的树形结构是类似的

编译器前端抽象语法树Package在 cn.misection.ast 中, 其中对于各种节点都有详细的归类
每个package如他们的名字所示, 分别包含着Expr, statement, 所有的节点都实现空接口
cn.misection.ast.IASTreeNode, 表明他们都是抽象语法树的节点, 不同包内会有一个节点接口
继承自 cn.misection.ast.IASTreeNode, 如Expr包中有IExpression.之后会有一个抽象类继承自包内抽象节点, 如 AbstractExpression, 之后所有包内节点都将继承自抽象节点
或者节点接口, 接口内会规定一些方法, 该包中的类必须实现他们, 如toEnum()
 
 引入toEnum()和枚举表示是为了之后我们更优雅地switch case, 否则对于同属一类的节点, 
 我们可能需要反射去获取抽象的表达式到底是加法表达式还是乘法表达式(或者instanceof运算), 
 类名虽然可以表达这些信息, 但这样用并不优雅, 所以我们给每种节点都绑定一个枚举值, 既方便switch,
 又可以取代一些非常简单的不包含信息的类型(如type包中的基本类型基本只是作为别人的属性使用), 这时候
 使用他们时, 用他们的枚举单例其实就够了, 语法分析器最后就是需要拿到一个从Program始, 向类
 方法等发散的前端语法树, 树发散的每个节点就是一个个有了行号信息和字面量(有字面量时)的POJO


语法分析器便是抽象语法树的制造者, 分析器读完Token流之后, 在语言的语法规则指导下, 
我们就能得到语法分析器的结晶-抽象语法树了 

本质来讲, 抽象语法树可以看作是对该语言文法的"类型化"重写, 即对于每个非终结符, 
每个产生式, 给出确切定义, 并赋予不同的属性. 类型实例便成为要输出的抽象语法树的"节点"

### Cva程序的树形结构示意图

依据之前给出的程序文法, 可以给出一个树形表示Cva程序的大致结构
由于博客园的二级分类展开有点松散有点丑, 所以我们放到text中

```text
+ Program
  + Entry(可以是入口类, 也可以直接是入口方法)
    + main方法
      + StatementList
  + ClassList
    + Class
      + VarList
        + Var
        + ...
      + MethodList
        + Method
        + ...
    + ...
```

```text
/**
 * 方法体;
 */
+ Method
  + Return Type
  + Name
  + FormalList
    + Formal
    + ...
  + VarList
    + Var
    + ...
  + StatementList
    + Statement
    + ...
  + Return Expr
```
```text
/**
 * 变量声明;
 */
+ Var
  + Type
  + Identifier
```
  // 当然, Cva 支持在声明时赋初值, 不过目前的实现比较简单, 存在着一定的问题

上文所述的每一个节点(不包含XXXList类型节点), 我们都有给出确切的定义, 并且在节点内携带足够的信息(行号, 类型信息等), 方便后期的语义分析和错误提示信息。

### ast包内详细介绍及继承关系

我们的AST中共包含8种主要的类型如下

```text
+ Program

  程序实体类, 语法树的根节点, 拥有 EntryClass 和 ClassList 两个属性
  + EntryClass   // 主类(程序入口), 如果只有main方法, 会自动添加到Application类中
  + ClassList   // 用户自定义的类

+ EntryClass

  主类实体类, 程序入口方法所在类, 如果不显示声明(直接上main方法), 那么会生成Application作为其默认名 
  拥有 Name StatementList(从属main方法) 两个属性
  + Name        // 类名
  + StatementList   // main方法中的语句 TODO 可以直接做成BlockStatement

+ Class

  类实体类, 表示用户自定义的类, 拥有 Name BaseClass FieldList MethodList 四个属性
  + Name        // 类名
  + ParentClass   // 父类(默认就是java/lang/Object)
  + FieldList   // 字段列表
  + MethodList  // 实例方法列表

+ Method

  方法实体类, 表示用户定义的实例方法
  + Name            // 方法名
  + ReturnType      // 返回类型
  + ParameterList
    /FormalList
    /ArgList        // 方法的参数
  + LocalVarList    // 方法内声明的本地变量
  + StatementList   // 语句
  + ReturnExpr       // 返回表达式 TODO 不要写那么死 以后要支持方法中return;

+ Statement
    + nullobj // 空对象
    + assign  // 赋值
    + Block     // 语句块
    + if    // if    
    ... 等等, 详细可见pkg内
  语句抽象类, 赋值语句, 输出语句, if-else语句,  while语句, for语句等Statement
     语句块直接继承自此类

  // TODO: 插入树形关系图

+ Expression

  表达式抽象类, 加减乘运算, 小于运算, 与运算, 非运算, 方法调用, this表达式, 
    对象创建表达式(new), 常量(数字, true/false), 变量访问直接继承此抽象类
  + LineNumber  //表达式所带行号
  // TODO: 插入树形关系图

+ Variable

  变量/字段实体类
  + Type    // 该变量/字段的类型
  + Identifier      // 变量/字段名

+ Type
    + basic
        + enum基本类型
    + advance
        + string
        + array
        + pointer
    + reference 
        + classType
  类型抽象类, 整型, 浮点型, 布尔型, string, class类类型直接继承自此抽象类

  // TODO: 插入树形关系图
```

## 语法分析实现

语法分析器的任务是读入记号流, 在语言的语法规则指导下生成抽象语法树。

分析算法主要分为自顶向下分析和自底向上分析, 其中自顶向下分析包括递归下降分析算法(预测分析算法)和LL分析算法, 自底向上分析包括LR分析算法。

### 递归下降分析算法

- 简单来讲, 递归下降法就是对于现有的Token(现有的信息, 现在手里捏的牌), 来预测下一个单词应该是什么
比如, 现在程序说了: "你吃",
那么我们预测下一个词应该是"了吗?" 或者"饭了吗?"
即我们希望得到的完整句子是"你吃了吗?" 或者"你吃饭了吗?"
我们可以在得到现有信息的基础上对下一步可能的情况进行手动穷举

- 回到一个Java中的例子, 比如现在我们遇到了int这个词, 我们判断他是int型字面量,
那么之后的情况无外乎 int var; 或者 int var = 0; 即声明或赋值
(这里不考虑强转, 因为强转前面有括号, 我们对其处理一般是在另一个流程中的)
所以我们下一步一般要吃掉我们预期的Token(Parser的eatToken()方法), 
这个被吃掉的Token其实就是我们当前读到的Token, 我们可以先读取他的信息
(比如字面量等保存到现在, 输出为我们抽象语法树的节点(方法, 类, 声明变量),
 待他无用之后, 将其eat掉), 如果有多种情况, 其实就是一个或多个分支的事
 (在eat时, 需要指定需要eat的类型, 如果不符合, 就会报错)

Cvac编译器采用的是递归下降分析算法(预测分析), 该算法的主要优点是

+ 分析高效, 线性时间复杂度
+ 容易实现, 方便手工编码
+ 错误定位和诊断信息准确

很多开源编译器, 商业编译器也采用了该算法, 比如GCC4.0, LLVM等

该算法的基本思想是：

+ 为每个非终结符构造一个分析函数
+ 通过**前看符号**指导产生式规则的选择

### 递归下降算法解析Cva代码

我们选择一个简单的部分来介绍递归下降分析算法在程序中的应用。

如上文所述, 用户自定义的类由两部分构成, 字段列表和实例方法列表(当然, 这个两个列表都可以为空), 那么我们就给出这样一个方法

```java
/**
 *  Class
     -> class Id { VarDecList MethodDecList }
     | class Id : Id { VarDecList MethodDecList }
 */
Class ParseClass() {
    // 其他代码
    fieldList = parseFieldList();
    methodList = parseMethodList();

    return new SomeClass(fieldList, methodList);
}
```

如上述代码所示, 在从记号流解析一个类型实体时, 调用了字段列表和方法列表的解析方法, 并将解析的结果作为一个类型实体的组成部分(此处并未体现类名, 父类等信息).

很显然的, 在 `ParseFieldList`, `ParseMethodList`两个方法内部, 必定含有对于单个字段, 单个方法实体的解析方法的调用, 并将单个的实体结果组织起来, 以返回给外界.

思考一下我们的文法规定的一个方法的构成形式, 返回类型, 方法名, 参数列表, 本地变量列表, 语句列表, 返回语句。这些部分的一个组织是方法, 那么很自然地, 我们又能为此写一个解析方法。

以上就体现了分析算法中*为每个非终结符构造一个分析函数*思想. 但是还有另外一部分思想, *通过**前看符号**指导产生式规则的选择*还未体现。

考虑另外一条文法, 关于"语句"。语句在我们的程序中有五种形式, 由{}组织的语句块, if-else语句, while语句, 赋值语句, 输出语句. 
我们只需要看第一个符号, 就能知道应当选择哪一条产生式来解析, 伪代码描述如下所示.

```java
/**
 *  Statement
       -> { StatementList }
       | if (Expr) Statement else Statement
       | while (Expr) Statement
       | println(Expr);
       | Id = Expr;
 */
Statement parseStatement() {
    switch(firstToken) {
        // 写代码时尽量不要出现如下的魔数, 这里是为了演示
        // 实际上我们是把常量放入枚举中的
        case "{":
            return parseStatementBlock();
        case "if":
            return parseIfElseStatement();
        case "while":
            return parseWhileStatement();
        // ...
        default:
            throw new ParseException(message);
    }
}
```

从伪码中很清晰地体现出来, 我们只需要通过查看一个"前看符号"就能确定要选择哪个方向去解析当前语句。如果解析失败, 那么必定是用户给的源程序出现了问题, 导致我们程序选择了错误的方向, 或者出现了错误, 这就需要用户修改源代码, 然后重新编译。

## 语法分析实例简析

对于文法附带给出的程序样例中的一行语句

```java
total = num * (this.compute(num-1));
```

语法分析完成后的输出的抽象语法树如下示意

其实语法分析给出的语法树就是把源代码又层次打印一遍, 过程比较简单. 如果朋友有问题可以留言, 我看到会回答

// TODO: 树形图

### 给出语法错误提示及警告

在语法分析阶段, 针对源码中可能出现的错误, 会给出错误提示信息, 用于告知用户程序处理到的位置和出现的错误

在自顶向下语法分析部分, 可能出现的错误的仅有一类, 预期是某个符号, 但是得到的却是另外的符号, 这样就会出现错误了

举例如下:

```java
void doSomething(int ) {
  // ...
}
```

容易看出, 在这个方法的参数列表部分, 缺失了参数名, 因此语法分析器将会给出错误提示:

```text
Line 6: Exprects Identifier, but got CLOSE_PAREN.
Syntax error line 6, compilation aborting...
```

给出错误提示之后就直接退出虚拟机, 拒绝编译, 等待用户修改源代码并重新编译

## 设计模式浅析

### ast中的设计模式

1. 空对象模式:  在ast中所有的nullobj包都是空对象模式, 空对象模式能省却我们代码中大量难看的
判空if (obj != null ) 等等
在我们后面遇到空对象时, 也是什么都不做, 把它当做空气(可怜的空对象)

2. 建造者模式: 在前端的method构造时, 使用了建造者模式, 建造者模式能让我们的传参更加清晰, 
不易犯错, 也能一定程度上封装复杂的构造过程, 后期将会把所有构造复杂的POJO全部重构成建造者模式构造

3. 日后会思考将一些构造重构成工厂
