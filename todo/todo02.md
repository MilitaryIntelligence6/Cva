##todo实现


- ~~i++~~
- ~~位操作;~~

- ~~给所有expr上一个type接口, 返回表达式类型, 让写操作好打印;~~


- ~~前端解析语法糖 +=, -=;~~
- ~~位操作~~;
- for loop;
- ~~做支持int num = 1;的定义!!;~~

+ ~~方法内 交互使用decl与statement~~;
+ ~~目前是必须先声明再用~~;
- ~~++ 应该从statement变成装饰者expr;~~
- ~~type改成枚举;~~  
- ~~原子操作加入new;~~
- ~~去掉所有的add, sub;~~
 - ~~完成decl statement;~~


- 很多地方写instanceof或者getclass这种都不优雅, 最好用一个map或者用byte放常量池表达;

- instru 改成枚举;

- 把所有的取type操作都改成enumType();
能换的icvatype都换掉;

- main内定义局部变量, 会给main中变量错误编号, 应该是多+1, 明天测试改;

- 常量折叠目前很蠢, 不识别++;
- 优化, 数值小于指定值的都换成iconst_0, 霍夫曼树 Byte.MAX_VALUE bipush;
  sipush;
  
 - 直接在ldc压命令中改, 改成iconst_1, bipush, sipush;
 
 - 实现 == <= >=
 
 - !运算目前有问题;
 
 - */放在一起, 位操作放在一起;
 
 - 下一步, 做支持ClassName cla = new ClassName();, 
 现在在外部new className() 反而在类内部创建一个className属性, 这不对;
 
 - 目前只要声明string就会报错, 后面修复;
 
 - 把所有前端类型type直接换后端;
 
 - 句中return;
 
 - 目前 创建目录会在根目录下, 看下哪里路径出了问题;
 
 + ALLOY语言;