# 空检查属性获取-Opp

参考[Opt](https://www.hutool.cn/docs/#/core/JavaBean/%E7%A9%BA%E6%A3%80%E6%9F%A5%E5%B1%9E%E6%80%A7%E8%8E%B7%E5%8F%96-Opt?id=%e7%a9%ba%e6%a3%80%e6%9f%a5%e5%b1%9e%e6%80%a7%e8%8e%b7%e5%8f%96-opt)</br>
在嵌套对象的属性获取中，由于子对象无法得知是否为`null`，每次获取属性都要检查属性对象是否为`null`
，使得代码会变得特别臃肿，因此使用`Opp`来优雅的链式获取属性对象值。

## empty

> 返回一个包裹着null的Opp对象

```java
Opp<Object> empty=Opp.empty();
        empty.get();
        out>>null
```

## required

> 返回一个包裹的元素不可能为空的Opp对象(注意没有判断空字符串)
> 会抛出NPE

```java
String empty=null;
        Opp<String> required=Opp.required(empty);

```

## of

> 返回一个包裹的元素可能为空的Opp对象
> 并不会因为要包裹的对象为null抛出NPE

```java
String empty=null;
        Opp<String> required=Opp.of(empty);
```

## ofStr

> 返回一个包裹的元素可能为空的Opp对象(进行了空字符串的判断)

```java
boolean aNull=Opp.ofStr("  ").isNull();
        out>>true
```

## ofColl

> 返回一个包裹的集合可能为空的Opp对象，额外判断了集合内元素为空的情况

```java
Opp.ofColl(Collections.<String>emptyList()).isNull();
        out>>true
```

## ofTry

> 这是一个能够捕获异常到Opp对象中的方法并且可以指定返回值和兼容多线程的操作，方法重载，可以只进行操作，也可以进行操作，在后续参数传入我们要捕获的异常.class对象

我们在进行操作的时候如果没有指定要捕获的异常那么默认就会捕获所有异常，即使出现了异常也不会报错

```java
List<String> last=null;
        Opp<String> stringOpp=Opp.ofTry(()->last.get(0));
// 此时应该会NPE异常但是我们会将所有的异常捕获所以此时不会报错而且我们的Opp对象中的exception属性为java.lang.NullPointerException
        System.out.println(stringOpp.getException());
        out>>java.lang.NullPointerException
```

如果不是我们要捕获的异常或者其子类就会抛出

```java
Opp<String> stringOpp=Opp.ofTry(()->last.get(0),IndexOutOfBoundsException.class);
// 现在的意思就是将IndexOutOfBoundsException和其子异常进行捕获，如果发生的异常不是IndexOutOfBoundsException或者其子类就会直接抛出
        out>>java.lang.RuntimeException:java.lang.NullPointerException
```

> 以下方法应当配合ofTry()使用更佳

### getException()

> 获取异常，当调用ofTry()时，异常信息不会抛出，而是保存，调用此方法获取抛出的异常

```java
Opp<String> stringOpp=Opp.ofTry(()->last.get(0));
        System.out.println(stringOpp.getException());
        out>>java.lang.NullPointerException
```

## isFail()

> 当调用 ofTry时，捕获到我们要捕获的异常时返回true

```java
// 适用场景如果当一个请求发生超时异常时if ofTry(操作,超时异常.class).isFail return xxx;
这样就可以在超时的第一时间返回一个想展示给用户的数据
```

## get()

> 返回包裹里的元素，取不到则为null，注意和java.util.Optional的get()
> 不一样，不会抛出NoSuchElementException，如果需要一个绝对不能为null的值则可以使用orElseThrow()

```java
Object opp=Opp.of(null).get();
        out>>null
```

> 如果我们想要获取一个对象中的值,我们提供了lambda表达式的方法更简洁方便

```java
Opp.of(User.builder().username("Zverify").build()).get(User::getUsername);
        out>>Zverify
```

## isEmpty()

> 判断包裹里的元素是不是null，不存在为 true，否则为 false

```java
Opp.of(null).isEmpty();
        out>>true
```

## isPresent()

> 判断包裹里元素的值是否存在，存在为 true，否则为 false

```java
Opp.of(null).isPresent();
        out>>false
```

## ifPresent(::)

> 如果包裹里的值存在，就执行传入的操作

```java
Opp.ofNullable("Hello Steam!").ifPresent(System.out::println);
        out>>Hello Steam!
```

## filter()

> 判断包裹里的值存在并且与给定的条件是否满足 断言执行结果是否为true
> 如果满足条件则返回本身
> 如果给定的条件为null，抛出NPE

```java
String name="臧臧";
        Opp.of(name).filter(v->!"臧臧".equals(v)).orElseGet(()->"zz");
        out>>zz
```

## filterEqual()

> 将Opp包裹的对象的equals()方法包装起来，我们如果在过滤的时候只想要使用equals()方法则可以直接使用filterEqual()

```java
String name="臧臧";
        String s=Opp.of(name).filterEqual("zz").orElseGet(()->"zz");
        out>>zz
```

## map()

> 如果包裹里的值存在，就执行传入的操作并返回一个包裹了该操作返回值的Opp对象
> 如果不存在，返回一个空的Opp

```java
User user=User.builder().username("臧臧").nickname("ZVerify").build();
        Opp.of(user).map(User::getNickname).get();
        out>>ZVerify
```

## peek()

> 如果包裹里元素的值存在，就执行对应的操作，并返回本身
> 如果不存在，返回一个空的Opp
> 注意，传入的lambda中，对包裹内的元素执行赋值操作并不会影响到原来的元素

```java
User user=new User();
// 相当于ifPresent的链式调用
        Opp.of("hutool").peek(user::setUsername).peek(user::setNickname);
        out>>(username=hutool,nickname=hutool)
        String name=Opp.of("hutool").peek(username->username="123").peek(username->username="456").get();
        out>>hutool
```

## peeks()

> 对peek()函数的动态参数调用，更加灵活

```java
User user=new User();
        Opp.of("hutool").peeks(user::setUsername,user::setNickname);
        out>>(username=hutool,nickname=hutool)
```

## typeOf

### typeOfPeek

> 如果传入的lambda入参类型一致，或者是父类，就执行，目前不支持子泛型

```java
AtomicBoolean isExecute=new AtomicBoolean();
        Opp.of("").typeOfPeek((String str)->isExecute.set(true));
        out>>isExecute.get()==true
```

### typeOfMap

> 如果传入的lambda入参类型一致，或者是父类，就执行，目前不支持子泛型

```java
AtomicBoolean isExecute=new AtomicBoolean();

        Opp<Boolean> opp=Opp.of("").typeOfMap((String s)->{
        isExecute.set(true);
        return isExecute.get();
        });
        out>>opp.get()==true
```

### typeOfFilter

> 判断如果传入的类型一致，或者是父类，并且包裹里的值存在，并且与给定的条件是否满足
> 如果满足条件则返回本身
> 不满足条件或者元素本身为空时返回一个返回一个空的{@code Opp}

```java
Opp<String> opp=Opp.of("").typeOfFilter((String str)->Opp.ofStr(str).isNull());
        out>>opp.isNonNull()==true
```

## or()

> 如果包裹里元素的值存在，就返回本身，如果不存在，则使用传入的操作执行后获得的Opp对象

```java
User user=User.builder().username("hutool").build();
        String name=userOpp.map(User::getNickname).or(()->userOpp.map(User::getUsername)).get();
        out>>hutool
```

## orElse()

> 如果包裹里元素的值存在，则返回该值，否则返回传入的值

```java
String hutool=Opp.ofStr("").orElse("hutool");
        out>>hutool
```

## orElseRun()

> 如果包裹里元素的值存在，则返回该值，否则执行传入的操作
> 如果包裹里元素的值存在，则返回该值，否则执行传入的操作
> 如果值不存在，并且传入的操作为null,则抛出NPE

```java
// 这个方法推荐配合ifPresent()使用更佳
// 判断一个值是否为空，为空执行一段逻辑,否则执行另一段逻辑
final Map<String, Integer> map=new HashMap<>();
final String key="key";
        map.put("a",1);
        Opp.of(map.get(key))
        .ifPresent(v->map.put(key,v+1))
        .orElseRun(()->map.remove(key));
        out>>map.get(key)==null
```

## failOrElse()

> 异常则返回另一个可选值

```java
List<String> last=null;
        String npe=Opp.ofTry(()->last.get(0)).failOrElse("hutool");
        out>>hutool
```

## orElseGet()

> 如果包裹里元素的值存在，则返回该值，否则返回传入的操作执行后的返回值

```java
String name=" ";
        String zName=Opp.ofStr(name).orElseGet(()->"zz");
        out>>zz
```

## orElseThrow()

### [无参]

> 如果包裹里的值存在，则返回该值，否则抛出 NoSuchElementException

```java
Opp.ofStr(" ").orElseThrow();
        throw>>java.util.NoSuchElementException:No value present
```

### [有参]

> 如果包裹里的值存在，则返回该值，否则执行传入的操作，获取异常类型的返回值并抛出
> 往往是一个包含无参构造器的异常 例如传入IllegalStateException::new

```java
Opp.empty().orElseThrow(NullPointerException::new);
        throw>>java.lang.NullPointerException
```