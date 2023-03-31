# 提示：此文档中使用的方法皆需要引入`stream-core`

- 鉴于如需调用Collectors里的方法每次都要写一串所以
- Steam在1.1.11版本之后即可无需Steam.of().collect(Collectors.toMap(xxx));
- 可直接Steam.of().toMap(xxx);

## of

> `创建`steam流

```java
List<String>  strList  = new ArrayList<>();
Steam<String> strSteam = Steam.of(strList);// 可以接收任何类型的集合
```

## split

> 将字符串通过某字符`分割`然后转换成串行流

```java
String str = "1,2,3"; // 使用","分割字符串并转换成流的形式方便进行操作
List<Integer> list = Steam.split(str, ",").map(Integer::valueOf).toList();
// out>> [1,2,3]
// 当然如果无法分割的时候我们会做友好提示
List<Integer> list = Steam.split("1,2,3", "a").map(Integer::valueOf).toList();
// java.lang.RuntimeException: The current string does not contain indivisible a
```

## iterate

> 该方法可以通过传入的初识值和判断的条件还有想要进行的操作进行`迭代`

```java
//假设我们想要得到一个1-5的整数集合
List<Integer> list = Steam.iterate(1,i->i<=5,i->++i).toList();// d第一个参数为初始值，第二个参数为迭代条件，第三个为迭代过程的操作
//out>>[1, 2, 3, 4, 5]
```

## filterIter

> 声明：此方法的作者：[臧臧](https://gitee.com/ZVerify)，
> PR来自：https://gitee.com/dromara/stream-query/pulls/30 </br>
> 该方法可以通过我们当前的集合去和另一个集合通过某一操作相同值进行`过滤`

```java
List<Student> list = Arrays.asList(
Student.builder().name("臧臧").age(22).build(),
Student.builder().name("阿超").age(21).build()
);
List<Student> others = Arrays.asList(
Student.builder().age(22).build(),
Student.builder().age(23).build()
);
List<Student> students = Steam.of(list).filterIter(Student::getAge, others).toList();
// 简单来说就是当前集合根据某一操作判断另一集合存不存在一样的如果存在就保留不存在就filter掉
out>>[SteamTest.Student(name=臧臧, age=23)]
```

## toZip

> 与给定的可迭代对象`转换`成map，key为现有元素，value为给定可迭代对象迭代的元素，返回的map至少包含全部的key，如果对应位置上的value不存在，则为null
> 如果key重复, 则保留最后一个关联的value

```java
List<String>  listZ  = Arrays.asList("臧臧","阿超","阿超");
List<Integer> listA  = Arrays.asList(22,21,null);
Map<String, Integer> map = Steam.of(listZ).toZip(listA);
out>>("臧臧", 22);
     ("阿超", null);
```

## join

> 将集合中的所有元素通过指定字符`拼接`成字符串

```java
String noParStr     = Steam.of(list).join();//无任何参数的时候会直接进行拼接
String singleParStr = Steam.of(list).join(",");//一个参数的时候通过指定字符串在每个元素中间添加并去进行拼接
String fullParStr   = Steam.of(list).join(",", "(", ")");//三个参数的时候后面两个是字符串的前缀和后缀
out>>"123"
out>>"1,2,3"
out>>"(1,2,3)"                                                             
```

## toMap

> 集合`转`Map,类似于Collection的toMap()方法,但是对其底层进行了优化，防止了重复key的异常和当value为null时的NPE异常

```java
List<Student> studentList = Arrays.asList(
                Student.builder().name("null").age(null).build(),
                Student.builder().name("阿超").age(21).build(),
                Student.builder().name("阿超").age(25).build()
);
studentList.stream().collect(Collectors.toMap(Student::getName,Student::getAge));// 此时我们如果使用Collection的toMap()方法会报NPE异常
// 还是上边那个list就算age不为null也会因为重复键的问题抛出异常IllegalStateException: Duplicate key 
// 使用我们Steam中的toMap()就可以完美的防止这种问题如果key一样我们会取最后一个元素的value
Steam.of(studentList).toMap(Student::getName, Student::getAge);
out>>{null=null, 阿超=25}
```

## generate

> 返回无限串行无序流
> 其中每一个元素都由给定的{@code Supplier}生成
> 适用场景在一些生成常量流、随机元素等

```java
Random random = new Random();
Steam<Integer> limit = Steam.generate(() -> random.nextInt(10)).limit(10);
limit.allMatch(v -> v >= 0 && v < 10));
out>>True
```

## group

> 对集合通过指定字段进行`分组`

```java
//统一Bean对象
List<Student> list = Arrays.asList(
Student.builder().name("臧臧").age(23).build(),
Student.builder().name("臧臧").age(21).build(),
Student.builder().name("阿超").age(21).build()
);
```

### group(单参数)

> 我们在使用group的时候只传一个参数就是通过某属性对其进行`分组`

```java
Map<String, List<Student>> groupByName = Steam.of(list).group(Student::getName);
//通过name对其进行分组
out>>{
     臧臧=[SteamTest.Student(name=臧臧, age=23), SteamTest.Student(name=臧臧, age=21)], 
     阿超=[SteamTest.Student(name=阿超, age=21)]
     }
```

### group(双参数)

> 按子组收集数据,我们在使用两个参数的group的时候可以在第二个参数指定`收集器`

```java
// 假如我们想知道班里同一名字的同学个数可以使用一个收集器去进行收集
Map<String, Long> grByPCount = Steam.of(list).group(Student::getName, Collective.counting());
out>>{臧臧=2, 阿超=1}
```

### group(三参数)

> 在第二个参数指定`map工厂`

```java
// 有些小伙伴可能不知道什么时候去使用这里我给小伙伴们举个例子
// 假如我想要按照班里同学年龄分组
Map<String, List<Student>> grByPAge = Steam.of(list).group(Student::getAge);
//我们在输出是是这样的
out>>21=[
        SteamTest.Student(name=臧臧, age=21, sex=null), SteamTest.Student(name=阿超, age=21, sex=null)], 
     23=[SteamTest.Student(name=臧臧, age=23, sex=null)]
// 此时并不是我们集合原始的顺序，因为默认使用的是HashMap，在存储是数据时，是先用key做hash计算，然后根据hash的结果决定这条数据的位置，因为hash本身是无序的，导致了读出的顺序是乱的。
// 这种情况在有些场景是不允许的比如在分页的时候，所以我们要使用有序的LinkedHashMap使插入顺序和访问顺序一样
Map<Integer, List<Student>> grByPAge = Steam.of(list).group(Student::getAge,LinkedHashMap::new,Collective.toList());
out>>23=[
        SteamTest.Student(name=臧臧, age=23, sex=null)], 
     21=[SteamTest.Student(name=臧臧, age=21, sex=null), SteamTest.Student(name=阿超, age=21, sex=null)]
//这样就可以保证我们在分组顺序保持不变了
```

## mapIdx

> 返回与指定函数将元素作为参数执行的结果组成的流，`操作带下标`，并行流时下标永远为-1,第一个参数为当前集合元素，第二个参数为下标

```java
List<String> list     = Arrays.asList("dromara", "hutool", "sweet");
List<String> mapIndex = Steam.of(list).mapIdx((e, i) -> i + 1 + "." + e).toList();
out>>[1.dromara, 2.hutool, 3.sweet]
```

## mapMulti

> 借鉴于jdk16新方法mapMulti,允许你用多个元素`替换`流中的元素

```java
//假如我们有一个场景我们有下面这个集合
//List<String> list = Arrays.asList("dromara", "hutool", "sweet");
//我们要将集合中每一个字符串的全大写和全小写集合
List<String> list = Arrays.asList("dromara", "hutool", "sweet");
List<String> objects = Steam.of(list).<String>mapMulti((s, buffer) -> {
    buffer.accept(s.toUpperCase());
    buffer.accept(s.toLowerCase());
}).toList();
out>>[DROMARA, dromara, HUTOOL, hutool, SWEET, sweet]
// 我们在之前使用flamap的时候也可以实现
List<String> strings = Steam.of(list).flatMap(s -> Steam.of(s.toUpperCase(), s.toLowerCase())).toList();
// 此时其实用mapMulti效率会更高虽然底层也去调用了flatMap方法但是我们用一个SpinedBuffer来保存元素，从而避免了为每组结果元素创建新流的开销
```

## distinct

> 返回一个具有`去重`特征的流

```java
//对集合中的元素进行去重
List<Integer> list = Arrays.asList(1, 2, 2, 3);
List<Integer> distinctBy = Steam.of(list).distinct(String::valueOf).toList();        
out>>{1,2,3}
```

## forEachIdx

> 对流里面的每一个元素执行一个操作，操作带`下标`，并行流时下标永远为-1

```java
List<String> list = Arrays.asList("dromara", "hutool", "sweet");
Steam.Builder<String> builder = Steam.builder();
// 使用流构造器通过accept将list的每个元素和对应坐标+1插入然后进行构建
Steam.of(list).forEachIdx((e, i) -> builder.accept(i + 1 + "." + e));
List<String> forEachIdxList = builder.build().toList();
out>>[1.dromara, 2.hutool, 3.sweet]
```

## forEachOrderedIdx

> 按顺序对集合中每个元素进行操作，操作带`下标`，一般用于对并行流按顺序操作(下标永远为-1)

```java
List<String> list = Arrays.asList("dromara", "hutool", "sweet");
Steam.Builder<String> builder = Steam.builder();
Steam.of(list).parallel().forEachOrderedIdx((e, i) -> builder.accept(e));
List<String> forEachOrderedI = builder.build().toList();
out>>[dromara, hutool, sweet]即使是并行流也会按顺序进行操作
```

## flatIdx

> 类似于flatMap不过这个操作是带`下标`的

```java
List<String> list = Arrays.asList("dromara", "hutool", "sweet");
List<String> mapIndex = Steam.of(list).flatIdx((e, i) -> Steam.of(i + 1 + "." + e)).toList();
out>>[1.dromara, 2.hutool, 3.sweet]
```

## flatIter

> 扩散流操作，可能影响流元素个数，将原有流元素执行操作，返回多个流所有元素组成的流

```java
List<Integer> list = Arrays.asList(1, 2, 3);
List<String> flatMapIter = Steam.of(list).flatIter(e -> Steam.of(e+"0")).toList();
out>>[10, 20, 30]
```

## filter

> `过滤`元素，返回与指定操作结果匹配指定值的元素组成的流

```java
List<Integer> list = Arrays.asList(1, 2, 3);
List<Integer> filterIndex = Steam.of(list).filter(a->a>=2).toList();
out>>[2, 3]保留lambda操作为true的元素
```

## filterIdx

> 带`下标`的`过滤`元素，总体使用和filter一样不过我们可以传两个参数第二个参数为坐标，以备各种场景，并行流下标始终为-1

```java
List<String> list = Arrays.asList("dromara", "hutool", "sweet");
List<String> filterIndex = Steam.of(list).filterIdx((e, i) -> i < 2).toList();// 将下标小于2的元素过滤出来
out>>[dromara, hutool]
```

## nonNull

> `过滤`掉集合中的`null`

```java
List<Integer> list = Arrays.asList(1, null, 2, 3);
List<Integer> nonNull = Steam.of(list).nonNull().toList();
out>>[1, 2, 3]
```

## parallel

> 通过传入的boolean值`更改`流的`并行状态`，默认为更改为并行

```java
Steam.of(1, 2, 3).parallel(true).forEach(System.out::print);
System.out.println();
Steam.of(1, 2, 3).parallel(false).forEach(System.out::print);
out>>231
     123
```

## push

> 向当前集合中`push`元素

```java
List<Integer> list = Arrays.asList(1, 2);
List<Integer> push = Steam.of(list).push( 3,4,5,6,7).toList();
out>>[1, 2, 3, 4, 5, 6, 7]
```

## unshift

> 给定元素组成的流与当前流`合并`并且合并到当前流前方,形成新的流

```java
List<Integer> list = Arrays.asList(2, 3);
List<Integer> unshift = Steam.of(list).unshift(1,2,3).toList();
out>>[1, 2, 3, 2, 3]
```

## at

> `获取`流中指定下标的元素，如果是负数，则从最后一个开始数起并且将该元素使用Optional包裹

```java
List<Integer> list = Arrays.asList(1, 2, 3);
Steam.of(list).at(0).orElse(null);out>>1
Steam.of(list).at(1).orElse(null);out>>2
Steam.of(list).at(-3).orElse(null);out>>1
Steam.of(list).at(-1).orElse(null);out>>3
```

## splice

> 可以将集合中的元素范围删除(删除时不包含起始点)，并从start点进行添加元素。参数（起始点，要删除个数，要添加元素）

```java
List<Integer> list = Arrays.asList(1, 2, 3);
Steam.of(list).splice(1, 0, 2).toList();    out>>[1, 2, 2, 3]
Steam.of(list).splice(3, 1, 3).toList();    out>>[1, 2, 3, 3]
Steam.of(list).splice(2, 1, 4).toList();    out>>[1, 2, 4]
Steam.of(list).splice(2, 1).toList();       out>>[1, 2]
Steam.of(list).splice(2, 0).toList();       out>>[1, 2, 3]
Steam.of(list).splice(-1, 1).toList();      out>>[1, 2]
Steam.of(list).splice(-2, 2, 2, 3).toList();out>>[1, 2, 3]
```

## findFirst

> 获取与给定断言`匹配`的`第一`个元素

```java
List<Integer> list = Arrays.asList(1, 2, 3);
Integer find = Steam.of(list).findFirst(Objects::nonNull);
out>>1
```

## findFirstIdx

> 获取与给定断言`匹配`的第`一个`元素的`下标`，并行流永远为-1

```java
List<Integer> list = Arrays.asList(null, 2, 3);
Steam.of(list).findFirstIdx(Objects::nonNull);           out>>1
Steam.of(list).parallel().findFirstIdx(Objects::nonNull);out>>-1
```

## findLast

> 获取与给定断言`匹配`的`最后`一个元素

```java
List<Integer> list = Arrays.asList(1, 2, 3);
Integer find = Steam.of(list).findFirst(Objects::nonNull);
out>>3
```

## findLastIdx

> 获取与给定断言匹配的`最后`一个元素的`下标`,并行流永远为-1

```java
List<Integer> list = Arrays.asList(null, 2, 3);
Steam.of(list).findLastIdx(Objects::nonNull);           out>>1
Steam.of(list).parallel().findLastIdx(Objects::nonNull);out>>-1
```

## zip

> 将现有元素与给定迭代器中对应位置的元素通过某种操作进行`合并`并返回新元素组成的流
> 新流的数量等于旧流元素的数量
> 如果对应位置上已经没有可迭代元素，则当前可迭代元素元素为null

```java
List<Integer> orders = Arrays.asList(1, 2);
List<String> list = Arrays.asList("dromara", "hutool", "sweet");
List<String> zip = Steam.of(orders).zip(list, (e1, e2) -> e1 + "." + e2).toList();
out>>[1.dromara, 2.hutool, 3.sweet]
```

## split

> 按指定长度`切割`成双层流

```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
List<List<Integer>> lists = Steam.of(list).split(2).map(Steam::toList).toList();
out>>[[1, 2], [3, 4], [5]]
```

## splitList

> 按指定长度`切割`为元素为list的双层流

```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
List<List<Integer>> lists = Steam.of(list).splitList(2).toList();
out>>[[1, 2], [3, 4], [5]]
```

## ~~toTree~~

> 构造递归树,为了减轻数据库压力我们在构造递归树的时候经常一下把所有数据查出来，然后在Java中进行操作
> 为了防止每次写大量的代码造成的臃肿,我们Steam提供了一个通用的方法便于操作 </br>
>
笔者感觉lambda也能写递归感觉很神奇所以写了一篇文章对其进行了详细解读[Lambda表达式也能写递归吗](https://zverify.gitee.io/2022/08/26/Lambda%E8%A1%A8%E8%BE%BE%E5%BC%8F%E4%B9%9F%E8%83%BD%E5%86%99%E9%80%92%E5%BD%92%E5%90%97/)

```java
// 使用到的entity
@Data
@Builder
public static class Student {
    @Tolerate
    public Student() {
        // this is an accessible parameterless constructor.
    }

    private String name;
    private Integer age;
    private Long id;
    private Long parentId;
    private List<Student> children;
    private Boolean matchParent;
}
```

```java
// 我们在构造树的时候通常会需要以下几个字段，(主键ID,父ID,子集合,断言是否为根节点(默认为父ID为null))

List<Student> studentTree = Steam
   .of(
      Student.builder().id(1L).name("dromara").matchParent(true).build(),
      Student.builder().id(2L).name("baomidou").matchParent(true).build(),
      Student.builder().id(3L).name("hutool").parentId(1L).build(),
      Student.builder().id(4L).name("sa-token").parentId(1L).build(),
      Student.builder().id(5L).name("mybatis-plus").parentId(2L).build(),
      Student.builder().id(6L).name("looly").parentId(3L).build(),
      Student.builder().id(7L).name("click33").parentId(4L).build(),
      Student.builder().id(8L).name("jobob").parentId(5L).build()
   )
    // 仅需四个Lambde即可完成操作
   .toTree(Student::getId, Student::getParentId, Student::setChildren, Student::getMatchParent);

out>>[SteamTest.Student(name=dromara, age=null, id=1, parentId=null, children=[
        SteamTest.Student(name=hutool, age=null, id=3, parentId=1, children=[
            SteamTest.Student(name=looly, age=null, id=6, parentId=3, children=null, matchParent=null)], matchParent=null),
        SteamTest.Student(name=sa-token, age=null, id=4, parentId=1, children=[
            SteamTest.Student(name=click33, age=null, id=7, parentId=4, children=null, matchParent=null)], matchParent=null)], matchParent=true
      SteamTest.Student(name=baomidou, age=null, id=2, parentId=null, children=[
        SteamTest.Student(name=mybatis-plus, age=null, id=5, parentId=2, children=[
            SteamTest.Student(name=jobob, age=null, id=8, parentId=5, children=null, matchParent=null)], matchParent=null)], matchParent=true)]

```
