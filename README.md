# Cva
![](https://img.shields.io/badge/language-Java-yellow.svg)
![](https://img.shields.io/badge/category-compilerLearning-blue.svg)
[![](https://img.shields.io/badge/blog-@misection-red.svg)](https://www.cnblogs.com/misection/)
![](http://progressed.io/bar/91?title=done)

```text
   █████████  █████   █████   █████████  
  ███░░░░░███░░███   ░░███   ███░░░░░███ 
 ███     ░░░  ░███    ░███  ░███    ░███ 
░███          ░███    ░███  ░███████████ 
░███          ░░███   ███   ░███░░░░░███ 
░░███     ███  ░░░█████░    ░███    ░███ 
 ░░█████████     ░░███      █████   █████
  ░░░░░░░░░       ░░░      ░░░░░   ░░░░░ 
```

---
## Cva 是一个JVM语言, 未来希望兼容C99标准, 达到C-to-JVM的效果
![](res/img/logo/Cva-128.png)

## 教程已经暂时完结撒花啦, 感谢一路有你陪伴
虽然说完结, 但个人觉得教程写的其实修修补补, 勉强能差强人意吧, 有很多其实语焉不详的地方, 比如io包
(个人认为是属于与主题关系不大的细节问题), 比如前端的IVisitor以及语义分析器的实现, 很多地方都很
不成熟, 如果我的表述对您造成了困惑或者有我实在写得不周到, 写得烂写的语焉不详的地方, 或者在调试过
程中遇到了什么问题, 欢迎发issue或者留言, 感谢您的阅读!

目前代码风格是当时比较喜欢的比较对称的Csharp大括号换行风格, 但是目前主流还是比较紧凑的不换行风格, 
您可以自己重新使用一键整理修改之!

## [博客园阵亡, 知乎专栏链接](https://www.zhihu.com/column/c_1350940230007017473)
## [博客园教程链接](https://www.cnblogs.com/misection/p/14429145.html)
## 文章目录

### [00.一个JVM语言的诞生](https://www.cnblogs.com/misection/p/14429145.html)
### [01.从0实现一个JVM语言之架构总览](https://www.cnblogs.com/misection/p/14429158.html)
### [02.从0实现一个JVM语言之词法分析器-Lexer](https://www.cnblogs.com/misection/p/14458592.html)
### [03.从0实现一个JVM语言系列之语法分析器-Parser](https://www.cnblogs.com/misection/p/14461275.html)
### [04.从0实现一个JVM语言系列之语义分析-Semantic](https://www.cnblogs.com/misection/p/14469791.html)
### [05.从0实现一个JVM语言之目标平台代码生成-CodeGenerator](https://www.cnblogs.com/misection/p/14483009.html)
 

- **如果项目对您有帮助, 请点一点star, 谢谢!**
- 未来的目标是兼容C, 支持C编译到JVM虚拟机上

#### 目前要准备春招实习, 所以项目暂停更新一段时间;
+ 由于时间原因, 目前没有做系统的测试;

+ 2020-03-16, 电脑坏了, 今天才部署好新的, 最近时间比较紧, 本来打算将所有
lambda表达式添加上类型, 以前用的时候没有类型就觉得很别扭, 现在惊人地发现可
以给其添加类型信息


+ 目前文法基本属于Java子集, 支持简单的面向对象
+ 词法和语法前端手写
+ 面向目标平台 JVM 生成字节码
+ 已实现的编译优化：简易的常量折叠、不可达代码删除，基于到达定义分析的常量/拷贝传播，基于活性分析的死代码删除优化
+ 由于目前还不稳定, 所以出现的bug欢迎issue或者贡献您的代码

# How to use

#### 您可以 goto release 下载发行版
- cva目前制作了windows exe installer与jar发行版, 目前为试发行, 还是存在着不少的bug
- 由于时间原因, 目前没有做命令行参数的支持, 这个是todo项目之一

##### release-jar
- jar版本速度更快, 同时意味着您可以跨平台使用之
```shell script
java -jar cvac.jar fileName
```
- 会将文件编译当前目录下的指定的文件
![](res/img/cvac/cmd.png)

#### release-windows installer
![安装包用 inno setup打包](res/img/cvac/installer.png)
- 使用 windows 安装包, 需要.NET framework3.5的支持
 其他方案如exe4j存在目前难以解决的路径问题
- 同时, 在使用cvac的同时建议您指定CvaDK的安装路径/bin为环境变量, 方便在命令行随时使用, 或者到bin目录下去编译cva文件
> PS: 目前 CvaDK使用的是Microsoft的IKVM, 希望以后能开发出CvaVM整套工具

#### 您也可以clone or 下载源码, 自行编译, 作者的编译环境是
> JDK  1.8 以上  
> 由于大量使用了Java8的函数式特性, 所以请您务必使用JDK8以上版本  
> Maven 3  
> 使用Maven管理项目  
#### 依赖
- 依赖仅仅有Junit进行测试, 但是目前由于时间原因, 还没有有规模地组织测试

# Cva语法

- Cva的语法目前与Java类似, 但是允许将main方法放在类外部, 其他

#### 示例文件请见 ./sample/

#### 由于项目目前还没有achieve, 所以一些特性还不支持, 只给出了一些简单的sample;

# 项目结构
- go 的使用是本来打算用go很棒棒的标准库的flag作为命令行参数接收以及启动胶水 
后来go和Java进程间通信始终出现着意想不到的bug且性能较差, 遂作罢   
后来想用Csharp作为启动器, Csharp调用Java比较简单, 发现可以直接用ikvm打包...
就直接用ikvmc编译了jar并用inno setup制作了发行版
- 项目中的C语言是cva源码, 因为C勉强有点代码提示, 所以目前Cva IDE 还没开发出来,
不会拒绝非.cva后缀的文件

```text
├─src
│  ├─main
│  │  ├─go // 暂时被这个小项目遗弃的go们
│  │  │  └─golang
│  │  │      ├─handler
│  │  │      └─main
│  │  ├─java
│  │  │  ├─cn
│  │  │  │  └─misection
│  │  │  │      └─cvac
│  │  │  │          ├─ast // 前端抽象语法树
│  │  │  │          │  ├─clas // Cva类
│  │  │  │          │  ├─decl // Cva 声明
│  │  │  │          │  │  └─nullobj // 所有nullobj package都是空对象模式的空对象 下同
│  │  │  │          │  ├─entry // Cva 主类, Cva程序的入口
│  │  │  │          │  ├─expr // Cva 表达式
│  │  │  │          │  │  ├─nonterminal // 非终结符
│  │  │  │          │  │  │  ├─binary // 二元表达式
│  │  │  │          │  │  │  ├─ternary // 三元表达式
│  │  │  │          │  │  │  └─unary // 一元表达式
│  │  │  │          │  │  ├─nullobj // 空对象
│  │  │  │          │  │  └─terminator // 终结符
│  │  │  │          │  ├─method // Cva 方法
│  │  │  │          │  ├─program // Cva 语法树根节点
│  │  │  │          │  ├─statement // Cva声明
│  │  │  │          │  │  └─nullobj 
│  │  │  │          │  └─type // Cva类型
│  │  │  │          │      ├─advance // 进阶类型, String, array && 指针, 目前只实现了string
│  │  │  │          │      ├─basic // 基础类型, Java基本类型
│  │  │  │          │      └─reference // 引用类型, 其实string和array也是
│  │  │  │          ├─codegen
│  │  │  │          │  └─bst // backend syntax tree 目标平台(JVM)抽象语法树
│  │  │  │          │      ├─bclas
│  │  │  │          │      ├─bdecl
│  │  │  │          │      ├─bentry
│  │  │  │          │      ├─bmethod
│  │  │  │          │      ├─bprogram
│  │  │  │          │      ├─btype
│  │  │  │          │      │  ├─advance
│  │  │  │          │      │  ├─basic
│  │  │  │          │      │  └─reference
│  │  │  │          │      └─instructor
│  │  │  │          │          └─write
│  │  │  │          ├─config // 主要是条件编译的宏常量等
│  │  │  │          ├─constant // 常数池, TODO 将所有常数池以及package-info的常量用枚举重构
│  │  │  │          ├─io // 文件流读写
│  │  │  │          ├─lexer // 词法分析
│  │  │  │          ├─optimize // 编译优化
│  │  │  │          ├─parser // 语法分析
│  │  │  │          ├─pkg // Cva包管理
│  │  │  │          └─semantic // 语义分析
│  │  │  └─META-INF // 打包jar入口
│  │  └─resources
│  └─test // 单元测试
│      └─java
│          └─cn
│              └─misection
│                  └─cvac
│                      ├─ast
│                      ├─codegen
│                      │  └─bst
│                      │      └─instructor
│                      ├─lexer
│                      │  └─report
│                      └─unit
└─target
    // ... .class 文件...

```
#### 


---
# TODO
- ~~i++~~
- ~~位操作;~~
- ~~给所有expr上一个type接口, 返回表达式类型, 让写操作好打印;~~
- ~~前端解析语法糖 +=, -=;~~
- ~~位操作~~;
- ~~for loop;~~
- ~~做支持int num = 1;的定义!!;~~
+ ~~方法内 交互使用decl与statement~~;
+ ~~目前是必须先声明再用~~;
- ~~++ 应该从statement变成装饰者expr;~~
- ~~type改成枚举;~~  
- ~~原子操作加入new;~~
- ~~去掉所有的add, sub;~~
- ~~完成decl statement;~~
 
- 由于时间原因, 还没有测试每个分支
- 做好 terminal app 的命令行参数
- 把所有的不声明类型的 lambda 添加类型声明信息
- 兼容C99的超级Cva
- CvaNIO原生库
- 实现Java的Pkg, 进行Pkg编译, 选取pkg替代Java的package关键字, call取代Java的import关键字
- CvaDK, 实现HashMap
- CvaVM Cva虚拟机
- CvaIDE 
- printf 需要创建Object数组传入;
- array anewarray;
- 还是有不优雅的instanceof或者getclass, 最好用一个map或者枚举switch;
- instru 改成枚举;
- 区分++i与i++;
- main内定义局部变量, 会给main中变量错误编号(给变量多+1), 把args编号了但没用;
- 编译优化对新的特性提供支持;
- 优化, 数值小于指定值的都换成iconst, 霍夫曼树 Byte.MAX_VALUE bipush;
  sipush;
- 直接在ldc压命令中改, 改成iconst_1, bipush, sipush;
- 实现 == <= >=
- !运算目前有问题;
- parser 中*/逻辑顺序应该在一起放在一起, 位操作也应该放在一起; 
- 下一步, 做支持ClassName cla = new ClassName();, 
现在在其他类中new ClassName() 反而在类内部创建一个className属性, 这有问题; 
- 目前声明string会报错, 后面修复; 
- 句中return, 即return不必非在句末;
- 目前 创建目录会在根目录下创建classes和i目录, 看下哪里路径出了问题; 



