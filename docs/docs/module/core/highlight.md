# 两个核心类

> - `HighlightHelper`
>- `FoundWord`

## FoundWord

> 条件构造类--> 配合HighlightHelper使用
> 高亮我们一般会对接别人的接口或者自己写的方法，我们在进行条件构造的时候一般会使用两个参数的构造方法
> 第一个参数传我们想要高亮的数据，第二个参数传我们数据的坐标，对接接口的时候一般都会返回下标。然后如果自己写的话可以配合Hutool的DfaUtil使用，也可以自己实现

```java
String text = "我有一颗大土豆，刚出锅的";// 假如需要让`大`和`出锅`高亮，我们可以这样去构造条件
List<FoundWord> foundWords = Steam.of(
    new FoundWord("大", 4),
    new FoundWord("出锅", 9)
).toList();
```

> 这样的话条件就构造好啦下面我们使用HighlightHelper去自定义实现高亮处理

## HighlightHelper

> 自定义高亮处理，同样使用lambda表达式
> 有个场景我们需要让上边FoundWord构造的条件数据在html中变成红色，我们可以这样去使用

### highlight

> 1. 第一个参数是我们的原始数据
> 2. 第二个参数是FoundWord集合
> 3. 第三个参数是函数式接口UnaryOperator(表示一个操作，它接收一个参数并返回与其输入参数相同类型的结果)

```java
String highlight = HighlightHelper.highlight(text, foundWords, s -> "<span style='color:red'>" + s + "</span>");
out --> 我有一颗<span style='color:red'>大</span>土豆，刚<span style='color:red'>出锅</span>的
```

> 这就是我们的高亮处理的使用,支持多数据高亮和自定义高亮处理