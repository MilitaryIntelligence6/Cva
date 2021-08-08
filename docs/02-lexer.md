02.从0实现JVM语言之词法分析器-Lexer
===

## 本次有较大幅度更新, 老读者如果对前面的一些bug, 错误有疑问可以复盘或者留言.

## [源码github仓库, 如果这个系列对您有帮助, 请您给我一个小小的star!](https://github.com/MilitaryIntelligence6/Cva)

## [本节相关词法分析package地址](https://github.com/MilitaryIntelligence6/Cva/tree/master/Cvac/src/main/java/cn/misection/cvac/lexer)

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
1. 引言

2. 存储符号定义以及信息的类

3. 词法分析器实现过程走代码详解

4. 样例输出分析


引言
---
编译源码的第一个步骤是进行词法分析, 词法分析器读入源代码的字符流, 
将他们识别成Token流, 如   
`num++;`  
经过词法分析  
`=> identifier + increment + semi (标识符 * 1, 自增 * 1, 分号 * 1)`  
大家可以看到, 语法分析器做的事情非常简单, 就是将源码与Token一一对应, 类似建立一个映射关系,
 我们这里每一个Token都new了一个对象, 这样的做法并不高效
 (个人认为高效的方法是用枚举取代一些字面量固定的值, 主要是符号如`+`, `-`等), 
 但是可以保存行号信息, 以便之后向用户提供足够的报错信息.  
 得到词法分析的结果后(在这里是和语法分析器配合每次读一个Token并前看,
  而不是一次性读完再转交语法分析器处理),词法分析器将其输出交由语法分析器Parser处理

Cva符号简介
---

+ Cva目前支持的运算符有: 
    基本运算 `+` `-` `*` `/` `%` `&&` `=`  `.` `!` `<`  
    自增运算 `++` `--`
    以及位操作运算 `&` `|` `^` `>>` `<<` `>>>` 等其他运算符  
    域限定符 `{` `}` `(` `)` `;``,`    
+ 支持解析语法糖 `+=` `-=` `*=` `/=` `&=` `|=` `^=` ` >>=` `<<=` `>>>=` 等赋值运算符, 
    详可见EnumCvaToken类  
+ 打算在将来支持的有 三目运算符`?` `:`,  `==` `||`
+ 其中
  + `!` `~` `++` `--` 是单目操作符, unary, 目前 `i++` `++i` 还不区分压栈帧和自增顺序
  + `:` `?` 属于三目操作符的一部分
  + 除了一元运算符其他都是双目运算符, binary
  + `.`圆点运算符调用方法, 目前还没有做对象的属性调用
  + `{` `}` `(` `)` 是定界符
+ Cva定义的关键字有: 
`void` `byte` `short` `char` `int` `long` `float` `double` `boolean` `string`
`class` `new` `if` `else` `while` `true` `false` `this` `return` 等, 还有部分保留字, 是希望在未来实现的
  + `void` `byte` `short` `char` `int` `long` `float` `double` `boolean` `string` 是类型声明, 
  + `class` 声明一个类型时使用的前导关键字
  + `extends` 是继承声明, 目前的继承很弱鸡, 很多功能没有实现
  + `new` 申请内存分配给新对象, 或者数组, 目前数组还不支持
  + `if` `else` `while` `for` 分支/循环结构关键字, 目前还不支持switch case
  + `true` `false` 上文所述 `boolean` 类型的两字面量
  + `this` 当前对象实例指针
  + `return` 返回语句的前导关键字, 目前只做了最后一句返回, 后期希望实现方法中的返回
+ 特别说明: `Identifier` `ConstInt` `comment` `space` `\n` `\r` `EOF` `println/echo`
  + `Identifier` 标识符, 将类型/变量名等处理成一个标识符类型的记号, 具体意义取决于所处的位置
  + `ConstInt` 整数常数字面量, 其他类型同
  + `space` `\n` `\r` 分别是空格符 换行符 回车符,  处理源代码文件时将忽略它们
  + `// comment` 行注释
  + `/* comment */` 块注释
  + `EOF` 是源代码的文件结束符
  + `println` && `echo` 输出语句, `printf` 的支持打算在后期的版本更新


存储符号定义以及信息的类
---

按照我们规定的程序文法, 能给出记号的定义  
EnumCvaToken类
```java
package cn.misection.cvac.lexer;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumCvaToken {
    /**
     * +
     */
    ADD,

    /**
     * -
     */
    SUB,

    /**
     * *
     */
    STAR,

    /**
     * /
     */
    DIV,

    /**
     * 求余;
     */
    REM,

    /**
     * &
     */
    BIT_AND,

    /**
     * |
     */
    BIT_OR,

    /**
     * ...更多的在这里略去, 可见源码中
     */
    ;
}
```
CvaToken类
```java
package cn.misection.cvac.lexer;

/**
 * @author MI6 root
 */
public final class CvaToken {
    /**
     * the kind of the token
     */
    private final EnumCvaToken enumToken;

    /**
     * extra literal of the token
     */
    private String literal;

    /**
     * the line number of the token
     */
    private final int lineNum;


    public CvaToken(EnumCvaToken enumToken, int lineNum) {
        this.enumToken = enumToken;
        this.lineNum = lineNum;
    }

    public CvaToken(EnumCvaToken enumToken, int lineNum, String literal) {
        this.enumToken = enumToken;
        this.lineNum = lineNum;
        this.literal = literal;
    }

    @Override
    public String toString() {
        return String.format("Token {%s literal: %s : at line %d}",
                this.enumToken.toString(),
                literal == null ? "null" : this.literal,
                this.lineNum);
    }

    public EnumCvaToken toEnum() {
        return enumToken;
    }

    public String getLiteral() {
        return literal;
    }

    public int getLineNum() {
        return lineNum;
    }
}

```

其中 `enumToken` 字段标记了该记号的类型(即以上的`EnumCvaToken`枚举类), 
若有值, 例如数字或者字符串或者Identifier, 将在 `literal` 字段中给出当前记号的值
此外, 还会在 `lineNum` 字段给出当前记号的行号, 主要是报错环节给出尽可能定位准确的错误信息


词法分析器实现过程走代码详解
---

词法分析部分采用了手动实现的方式, 大致步骤如下, 大家可以参照lexer package中的Lexer类查看过程, 
当然, 这一切还要借助io包内的文件流将文件读入成StringBuffer(姑且把它当做buffer吧)

```java
private CvaToken lex() {
    char ch = stream.poll();
    // skip all kinds of blanks
    ch = handleWhiteSpace(ch);
    switch (ch) {
        case LexerCommon.EOF:
            return new CvaToken(EnumCvaToken.EOF, lineNum);
        case '+':
            return handlePlus();
        case '-':
            return handleMinus();
        case '*':
            return handleStar();
        case '&':
            return handleAnd();
        case '|':
            return handleOr();
        case '=':
            return handleEqual();
        case '<':
            return handleLessThan();
        case '>':
            return handleMoreThan();
        case '^':
            return handleXOr();
        case '~':
            return handleBitNegate();
        case '/':
            return handleSlash();
        case '%':
            return handlePercent();
        case '\'':
            return handleApostrophe();
        case '"':
            return handleDoubleQuotes();
        default:
            return handleNorPrefOrIdOrNum(ch);
    }
}
```

每当外部调用lexer的nextToken()方法(主要是Parser调用), 都会进行如下流程:  

1. 初始化源文件的按字符输入流, 并读入一个字符, 初始化全局行号为1,
 然后循环进行后面的所有步骤
 
2. 跳转到handleWhiteSpace方法检查是否是空白符(空格, 换行符, 回车符), 如果是, 直接尝试读下一个字符, 但如果发生换行, 行号++

3. 读取符号, 会switch之, 如果落在前缀字符的区间内, 前缀字符即某个运算符的前缀, 如 `+` 是 `++`, `+=` 的前缀, 
这样的字符在词法分析器中都有其特殊处理方法, 在这里面, 也包含了对注释的处理, 
会忽略Cva行注释// ... \n 与块注释/*... */  
编译器后端的任务很重, 所以在前端, 我们应尽量确定变量的类型, 删除无关的注释, 空符, 好给后端腾出

4. 同时这里要注意, 我们在Lexer的lex()方法中, 查看当前的字符是否为case块中的字符, 例如 `+`, `-`, `*`, 等等, 如果是, 那就跳转到处理他们的方法中
 ,为什么要跳转方法? 因为他们是前缀字符, 即分析器看到了`+`, 并不能确定这是一个加号, 要和其后面的字符一起看
 后面的字符可能是`+`, 那么该符号应该是`++`, 自增符号, 如果后一个连续的的符号是`=`, 那么这个符号应该是`+=`
 否则, 我们才返回`+`, 其他的同理, 每次lex()方法被调用, 词法分析器能返回一个类型符合的, 携带行号信息的记号, 
 如, 遇到 `+` 号:
 
    ```java
    private CvaToken handlePlus() {
        if (stream.hasNext()) {
            switch (stream.peek()) {
                case '+': {
                    // 截取两个;
                    stream.poll();
                    return new CvaToken(EnumCvaToken.INCREMENT, lineNum);
                }
                case '=': {
                    stream.poll();
                    return new CvaToken(EnumCvaToken.ADD_ASSIGN, lineNum);
                }
                default: {
                    break;
                }
            }
        }
        return new CvaToken(EnumCvaToken.ADD, lineNum);
    }
    ```
    我们这里把stream当做队列, 每次poll()会弹出队列首的字符, 这个首字符就是我们peek()到的字符.  
    如果遇到的不是这些前缀字符, 我们会到进入default分支中, 执行handleNorPrefOrIdOrNum()方法
    
    ```java
    private CvaToken handleNorPrefOrIdOrNum(char ch) {
        // 先看c是否是非前缀字符, 这里是 int, 必须先转成char看在不在表中;
        if (EnumCvaToken.containsKind(String.valueOf(ch))) {
            return new CvaToken(EnumCvaToken.selectReverse(String.valueOf(ch)), lineNum);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(ch);
        while (true) {
            ch = stream.peek();
            // Cva命名容许_和$符号;
            if (ch != LexerCommon.EOF
                    && !Character.isWhitespace(ch)
                    && !isSpecialCharacter(ch)) {
                builder.append(ch);
                this.stream.poll();
                continue;
            }
            break;
        }
        String literal = builder.toString();
        // 关键字;
        if (EnumCvaToken.containsKind(literal)) {
            return new CvaToken(EnumCvaToken.selectReverse(literal), lineNum);
        }
        else {
            if (isNumber(literal)) {
                // FIXME 自动机;
                if (isInt(literal)) {
                    return new CvaToken(EnumCvaToken.CONST_INT, lineNum, builder.toString());
                }
            }
            else if (isIdentifier(literal)) {
                return new CvaToken(EnumCvaToken.IDENTIFIER, lineNum, builder.toString());
            }
            else {
                errorLog("identifier or number which can only include alphabet, number or _, $",
                        "an illegal identifier with illegal char");
            }
        }
        return null;
    }
    ```
    // 请注意, 一般不要return null, 尽量抛出异常或者返回空对象, 这里是因为根本走不到这个分支, 才 return null  

5. 进入default分支的方法首先会查表, 这个Hash表是EnumCvaToken中的利用Enum的literal字面量属性反查Enum类型建的表,
这个表的key是Token的字面量, value是该Token的枚举, 在Enum 中构建如下
    ```java
    private static final Map<String, EnumCvaToken> lookup = new HashMap<>();
    
    static {
        for (EnumCvaToken kind : EnumSet.allOf(EnumCvaToken.class)) {
            if (kind.kindLiteral != null) {
                lookup.put(kind.kindLiteral, kind);
            }
        }
    }
    
    public static boolean containsKind(String literal) {
        return lookup.containsKey(literal);
    }
    ```
    // 以上是常见的建立枚举反查表的方法, 如果有看不懂的地方欢迎留言, 我看到会回复.
    在这一步, 如果遇到`{`, `}`这类非前缀字符(即不会产生歧义, 单字符只能单独出现在代码中),
     直接返回该Token, 否则会进入下一步

6. 当前的字符不是我们支持的特殊字符, 那就一直读取下去, 直到空格符/换行符/回车符/文件结束符/注释, 
 这样是尽可能找到了一个最长的序列. 
 
     首先检查这个序列是否是我们定义的关键字之一, 这个过程也是查我们前述的哈希表, 
     如果是关键字, 那么就返回正确类型和行号的Token;  
     
     如果不是关键字, 那么查看这个序列是不是一串数字, 如果是数字, 那么返回一个类型为`ConstInt`
     (目前还没有做自动机识别浮点数, 后期有空了会做正确的行号和数字串的记号;  
     
     如果也不是数字串, 那么检查是否符合程序对于标识符的规定, 如果符合,
     那么返回一个类型为`Identifier`(一般是用户自定义的变量常量名/类名/方法名),
     正确行号和标识符串的记号, Cva的变量名如同Java也需要字母开头/下划线或者不常见美元符号开头, 
     如果不符合, 那么只能是一个错误, 给出提示并结束分析.


样例输出分析
---
例如, 对于语句样例 
```groovy
echo "hello, world!\n";
println new Increment().incre();
echo "2 * 3 = ";
println 2 * 3; // 目前暂时不支持 printf;
```
 词法分析后的Token流是

```text {Token {WRITE literal: null : at line 93}} {Token {STRING literal: hello, world! : at line 93}} {Token {SEMI literal: null : at line 93}} {Token {WRITE_LINE literal: null : at line 94}} {Token {NEW literal: null : at line 94}} {Token {IDENTIFIER literal: Increment : at line 94}} {Token {OPEN_PAREN literal: null : at line 94}} {Token {CLOSE_PAREN literal: null : at line 94}} {Token {DOT literal: null : at line 94}} {Token {IDENTIFIER literal: incre : at line 94}} {Token {OPEN_PAREN literal: null : at line 94}} {Token {CLOSE_PAREN literal: null : at line 94}} {Token {SEMI literal: null : at line 94}} {Token {WRITE literal: null : at line 95}} {Token {STRING literal: 2 * 3 =  : at line 95}} {Token {SEMI literal: null : at line 95}}

```
可以看到对于空白符和注释, 在词法分析阶段我们就进行了跳过, 源代码文件流就会转换成这样的Token流供语法分析器处理
以上结果应该不难理解, 如果朋友们有任何疑问, 欢迎留言
