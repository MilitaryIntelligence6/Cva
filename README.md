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
### Cva 是一个JVM语言, 未来的目标是兼容C, 支持C编译到JVM虚拟机上;

#### 目前要准备春招实习, 所以项目暂停更新一段时间;
- 由于时间原因, 目前没有做系统的测试;

+ 目前是 Java 的子集，支持简单的面向对象
+ 词法和语法前端手写;
+ 面向 JVM 生成字节码
+ 已实现的编译优化：简易的常量折叠、不可达代码删除，基于到达定义分析的常量/拷贝传播，基于活性分析的死代码删除优化

---
### TODO
- Cva生态的兼容C的超级Cva;
- CvaNIO原生库;
- 实现Java的Pkg, 进行Pkg编译, 选取pkg替代Java的package关键字, call取代Java的import关键字;
- CvaDK, 实现HashMap
- CvaVM Cva虚拟机;
- CvaIDE 

- 把所ast type加后缀type不然要混淆; 

