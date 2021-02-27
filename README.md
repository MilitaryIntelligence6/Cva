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
# Cva 是一个JVM语言
![](res/img/logo/Cva-128.png)

#### [博客园教程地址](https://www.cnblogs.com/misection/p/14429145.html)
- 今天才整理好, 一会发布上去
- 未来的目标是兼容C, 支持C编译到JVM虚拟机上

#### 目前要准备春招实习, 所以项目暂停更新一段时间;
- 由于时间原因, 目前没有做系统的测试;

+ 目前文法基本属于Java子集, 支持简单的面向对象
+ 词法和语法前端手写.
+ 面向目标平台 JVM 生成字节码.
+ 已实现的编译优化：简易的常量折叠、不可达代码删除，基于到达定义分析的常量/拷贝传播，基于活性分析的死代码删除优化
- 由于目前还不稳定, 所以

# How to use

#### 您可以 goto release 下载发行版
- cva目前制作了windows exe installer与jar发行版, 目前为试发行, 还是存在着不少的bug.
- 由于时间原因, 目前没有做命令行参数的支持, 这个是todo项目之一.

##### release-jar
- jar版本速度更快, 同时意味着您可以跨平台使用之.
```shell script
java -jar cvac.jar fileName
```
- 会将文件编译当前目录下的指定的文件


#### release-windows installer
![安装包用 inno setup打包](res/img/cvac/installer.png)
- 使用 windows 安装包, 需要.NET framework3.5的支持;
 其他方案如exe4j存在目前难以解决的路径问题.

---
# TODO
- 由于时间原因, 还没有测试每个分支.
- 命令行参数.
- 兼容C的超级Cva.
- CvaNIO原生库.
- 实现Java的Pkg, 进行Pkg编译, 选取pkg替代Java的package关键字, call取代Java的import关键字.
- CvaDK, 实现HashMap.
- CvaVM Cva虚拟机.
- CvaIDE. 

- 把所ast type加后缀type不然要混淆; 

