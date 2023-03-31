## LambdaHelper

`LambdaHelper`可以解析`lambda`

例如可以通过lambda获取其信息

```java
LambdaExecutable lambdaExecutable=LambdaHelper.resolve((Serializable&BiConsumer<Integer[][],Integer>)(i,a)->{});
        System.out.println(lambdaExecutable);
// LambdaExecutable{
//  executable=private static void org.dromara.streamquery.stream.core.lambda.LambdaHelperTest.lambda$testResolve$9b6dfab2$1(java.lang.Integer[][],java.lang.Integer),
//  instantiatedTypes=[class [[Ljava.lang.Integer;, class java.lang.Integer],
//  parameterTypes=[class [[Ljava.lang.Integer;, class java.lang.Integer],
//  returnType=void,
//  name='lambda$testResolve$9b6dfab2$1', 
//  clazz=class org.dromara.streamquery.stream.core.lambda.LambdaHelperTest,
//  lambda=SerializedLambda[capturingClass=class org.dromara.streamquery.stream.core.lambda.LambdaHelperTest, functionalInterfaceMethod=java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V, implementation=invokeStatic org/dromara/streamquery/stream/core/lambda/LambdaHelperTest.lambda$testResolve$c70a867a$1:([[Ljava/lang/Integer;Ljava/lang/Integer;)V, instantiatedMethodType=([[Ljava/lang/Integer;Ljava/lang/Integer;)V, numCaptured=0],
//  proxy=null
// }
```

而且其在`idea`的`debug`模式下，在`Evaluate`调试窗口声明的`lambda`也可以被解析

![debug-evaluate](../../../static/img/debug-evaluate.png)

## 拓展lambda

使用`SerCons.multi`，可以让你的`forEach`支持多个`lambda`操作

```java
import static org.dromara.streamquery.stream.core.lambda.function.SerCons.multi;


Arrays.asList("vampire","a chao").forEach(multi(
        System.out::println,
        System.err::println,
        i -> System.out.println(i.equals("vampire"))
));

```

看不懂？没关系，[戳我学习](https://www.hutool.cn/docs/#/core/JavaBean/%E7%A9%BA%E6%A3%80%E6%9F%A5%E5%B1%9E%E6%80%A7%E8%8E%B7%E5%8F%96-Opt?id=%e5%ad%a6%e4%b9%a0%ef%bc%9a)
