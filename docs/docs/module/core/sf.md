> 借鉴了`KT`的作用域函数

## isEmpty

判断是否为null

## isPresent

判断是否存在值

## ofColl

将参数(集合)包裹到Sf中无论是否为null

```java
Sf.ofColl(Arrays.asList(null, null, null)).isEmpty();
out --> false
```

## mayColl

将集合包裹到Sf中自动过滤掉元素为null的

```java
Sf.mayColl(Arrays.asList(null, null, null)).isEmpty();
out --> true
```

## ofStr

将传入字符串包裹到Sf中无论是否为null

```java
Sf.ofStr("  ").isEmpty();
out --> false
```

## mayStr

将传入字符串包裹到Sf中如果为空字符串则过滤掉

```java
Sf.mayStr("  ").isEmpty()
```

## let

对当前Sf中所包裹对象进行操作并返回值，返回值会包裹到Sf中替换之前的元素，用于操作非null，否则抛出NPE如为了保证安全操作可以使用mayLet

```java
Sf.ofStr(" ").let(String::length).get();
out --> 1
```

## mayLet

对当前Sf中所包裹对象进行操作并返回值，返回值会包裹到Sf中替换之前的元素,如果所操作对象为null则不进行操作

```java
Sf.mayStr(null).mayLet(a -> a.toString().length()).isEmpty();
out --> true
```

## also

对当前Sf中所包裹对象进行消费操作无返回值，用于操作非null，否则抛出NPE如为了保证安全操作可以使用mayAlso

```java
AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
Sf.of(null).also(a -> name.set("ZVerify"));
// 此时原子引用类里的元素为字符串ZVerify
```

## mayAlso

对当前Sf中所包裹对象进行消费操作无返回值，如果所操作对象为null则不进行操作

```java
AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
Sf.of(null).mayAlso(a -> name.set("ZVerify"));
// 此时原子引用类里的元素为常量Z_VERIFY_NAME
```

## takeIf

对当前Sf中所包裹对象进行操作并返回一个布尔值，如果当前返回值为false则将Sf中包裹的数据置为null返回true则不变，用于操作非null，否则抛出NPE如为了保证安全操作可以使用mayTakeIf

```java
AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
Sf.of(name).takeIf((a) -> false).mayAlso(a -> a.set("ZVerify"));
// 此时原子引用类里的元素为常量Z_VERIFY_NAME
```

## mayTakeIf

对当前Sf中所包裹对象进行操作并返回一个布尔值，如果当前返回值为false则将Sf中包裹的数据置为null返回true则不变，如果所操作对象为null则不进行操作

```java
AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
Sf.of(name).takeIf((a) -> true).mayAlso(a -> a.set("ZVerify"));
// 此时原子引用类里的元素为字符串ZVerify
```

## takeUnless

与takeIf相反

## mayTakeUnless

与mayTakeIf相反

## is

对当前Sf中所包裹对象进行操作返回一个布尔值如果所操作对象不等于null并且则判断所传入操作是否返回为true，如果等于null则返回false，如果操作不为null，那么操作返回true则为true，返回false则为false

```java
AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
Sf.of(name.get()).is(Objects::nonNull);
out --> true
```

## require

获取当前Sf对象如果当前Sf中所包裹元素为null则抛出指定异常，不为null则返回Sf对象（中间操作）

```java
AtomicReference<String> name = new AtomicReference<>(null);
Sf<String> stringSf = Sf.ofStr(name.get());
stringSf.require(IllegalStateException::new)
// 会抛出IllegalStateException，默认为NoSuchElementException
```

## orThrow

获取当前Sf中包裹对象如果当前Sf中所包裹元素为null则抛出指定异常，不为null则返回Sf中包裹对象

```java
AtomicReference<String> name = new AtomicReference<>(null);
Sf<String> stringSf = Sf.ofStr(name.get());
stringSf.require(IllegalStateException::new)
// 会抛出IllegalStateException，默认为NoSuchElementException
```

## or

中间生产者操作，如果当前Sf中所包裹元素为null则拿到生产者所生产对象包裹到Sf中替换原有值，不为null则返回Sf本身

```java
AtomicReference<String> name = new AtomicReference<>(null);
Sf.ofStr(name.get()).or(() -> Sf.ofStr(Z_VERIFY_NAME));
// 此时原子引用类里的元素为常量Z_VERIFY_NAME
```

## orGet

生产者操作，获取当前Sf中包裹对象如果当前Sf中所包裹元素为null则拿到生产者所生产对象包裹到Sf中替换原有值，不为null则返回Sf所包裹对象

```java
AtomicReference<String> name = new AtomicReference<>(null);
Sf.ofStr(name.get()).orGet(() -> Z_VERIFY_NAME);
// 此时原子引用类里的元素为常量Z_VERIFY_NAME
```
