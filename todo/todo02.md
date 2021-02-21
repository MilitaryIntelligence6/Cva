- i++
- for loop;
- 位操作;

+ 方法内 交互使用decl与statement;
+ 目前是必须先声明再用;
- main内定义局部变量;
- 给所有expr上一个type接口, 返回表达式类型, 让写操作好打印;
- 很多地方写instanceof或者getclass这种都不优雅, 最好用一个map或者用byte放常量池表达;

- type改成枚举;  

- instru 改成枚举;

- 把所有的取type操作都改成enumType();
能换的icvatype都换掉;

- 做支持int num = 1;的定义!!;

前端解析语法糖 +=, -=;
- 位操作;
- 原子操作加入new;
- 常量识别目前很蠢, 不识别++;