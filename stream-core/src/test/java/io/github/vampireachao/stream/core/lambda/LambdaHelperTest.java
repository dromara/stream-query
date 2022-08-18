package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * LambdaHelper测试
 *
 * @author VampireAchao
 * @since 2022/5/31 19:51
 */
class LambdaHelperTest {

    @Test
    void testResolve() {
        Assertions.assertEquals(Integer[][].class, LambdaHelper.resolve((Serializable & BiConsumer<Integer[][], Integer>) (i, a) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(Integer.class, LambdaHelper.resolve((Serializable & BiConsumer<Integer[][], Integer>) (i, a) -> {}).getParameterTypes()[1]);
        Assertions.assertEquals(Integer.class, LambdaHelper.resolve((Serializable & BiConsumer<Integer, Integer[][][]>) (i, a) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(Integer[][][].class, LambdaHelper.resolve((Serializable & BiConsumer<Integer, Integer[][][]>) (i, a) -> {}).getParameterTypes()[1]);
        Assertions.assertEquals(0, LambdaHelper.resolve((SerSupp<Object>) Object::new).getParameterTypes().length);
        Assertions.assertEquals(Object.class, LambdaHelper.resolve((SerSupp<Object>) Object::new).getReturnType());
        Assertions.assertEquals(int.class, LambdaHelper.resolve((SerFunc<Integer, Integer[]>) Integer[]::new).getParameterTypes()[0]);
        Assertions.assertEquals(Integer[].class, LambdaHelper.resolve((SerFunc<Integer, Integer[]>) Integer[]::new).getReturnType());
        Assertions.assertEquals(int.class, LambdaHelper.resolve((SerFunc<Integer, Integer[][]>) Integer[][]::new).getParameterTypes()[0]);
        Assertions.assertEquals(Integer[][].class, LambdaHelper.resolve((SerFunc<Integer, Integer[][]>) Integer[][]::new).getReturnType());
        Assertions.assertEquals(Object.class, LambdaHelper.resolve((SerCons<Object>) System.out::println).getParameterTypes()[0]);
        Assertions.assertEquals(void.class, LambdaHelper.resolve((SerCons<Object>) System.out::println).getReturnType());
        Assertions.assertEquals(String[].class, LambdaHelper.resolve((SerCons<String[]>) (String[] stringList) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(String[].class, LambdaHelper.resolve((SerCons<String[]>) (String[] stringList) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(void.class, LambdaHelper.resolve((SerCons<String[]>) (String[] stringList) -> {}).getReturnType());
        Assertions.assertEquals(List.class, LambdaHelper.resolve((SerCons<List<String>>) (List<String> stringList) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(void.class, LambdaHelper.resolve((SerCons<List<String>>) (List<String> stringList) -> {}).getReturnType());
        Assertions.assertEquals(0, LambdaHelper.resolve((SerFunc<Object, String>) Object::toString).getParameterTypes().length);
        Assertions.assertEquals(String.class, LambdaHelper.resolve((SerFunc<Object, String>) Object::toString).getReturnType());
        Assertions.assertEquals(ReflectHelper.getDescriptor(LambdaHelper.resolve((Serializable & Function<Void, Void>) w -> w).getExecutable()), LambdaHelper.resolve((Serializable & Function<Void, Void>) w -> w).getLambda().getImplMethodSignature());
    }

    @Test
    void testGetPropertyNames() {
        List<String> propertyNames = LambdaHelper.getPropertyNames(LambdaExecutable::getName, LambdaExecutable::getLambda);
        Assertions.assertEquals("name", propertyNames.get(0));
        Assertions.assertEquals("lambda", propertyNames.get(1));
    }

    @Test
    void testProxy() {
        Assertions.assertEquals(int.class, LambdaHelper.resolve((SerFunc<Integer, Integer[]>) Integer[]::new).getParameterTypes()[0]);
//        Assertions.assertEquals(int.class, LambdaHelper.resolve((SerFunc<Integer, Integer[][]>) Integer[][]::new).getParameterTypes()[0]);
        Assertions.assertEquals(Object.class, LambdaHelper.resolve((SerCons<Object>) System.out::println).getParameterTypes()[0]);
    }

}
