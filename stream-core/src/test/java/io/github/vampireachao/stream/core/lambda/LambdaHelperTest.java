package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.lambda.function.SerArgsFunc;
import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.reflect.AbstractTypeReference;
import io.github.vampireachao.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * LambdaHelper测试
 *
 * @author VampireAchao ZVerify
 * @since 2022/5/31 19:51
 */
class LambdaHelperTest {

    @Test
    <T> void testResolve() {
        Assertions.assertEquals(Integer[][].class, LambdaHelper.resolve((Serializable & BiConsumer<Integer[][], Integer>) (i, a) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(Integer.class, LambdaHelper.resolve((Serializable & BiConsumer<Integer[][], Integer>) (i, a) -> {}).getParameterTypes()[1]);
        Assertions.assertEquals(Integer.class, LambdaHelper.resolve((Serializable & BiConsumer<Integer, Integer[][][]>) (i, a) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(Integer[][][].class, LambdaHelper.resolve((Serializable & BiConsumer<Integer, Integer[][][]>) (i, a) -> {}).getParameterTypes()[1]);
        Assertions.assertEquals(0, LambdaHelper.<SerSupp<Object>>resolve(Object::new).getParameterTypes().length);
        Assertions.assertEquals(Object.class, LambdaHelper.<SerSupp<Object>>resolve(Object::new).getReturnType());
        Assertions.assertEquals(int.class, LambdaHelper.<SerFunc<Integer, Integer[]>>resolve(Integer[]::new).getParameterTypes()[0]);
        Assertions.assertEquals(Integer[].class, LambdaHelper.<SerFunc<Integer, Integer[]>>resolve(Integer[]::new).getReturnType());
        Assertions.assertEquals(int.class, LambdaHelper.<SerFunc<Integer, Integer[][]>>resolve(Integer[][]::new).getParameterTypes()[0]);
        Assertions.assertEquals(Integer[][].class, LambdaHelper.<SerFunc<Integer, Integer[][]>>resolve(Integer[][]::new).getReturnType());
        Assertions.assertEquals(Object.class, LambdaHelper.<SerCons<Object>>resolve(System.out::println).getParameterTypes()[0]);
        Assertions.assertEquals(void.class, LambdaHelper.<SerCons<Object>>resolve(System.out::println).getReturnType());
        Assertions.assertEquals(String[].class, LambdaHelper.<SerCons<String[]>>resolve((String[] stringList) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(String[].class, LambdaHelper.<SerCons<String[]>>resolve((String[] stringList) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(void.class, LambdaHelper.<SerCons<String[]>>resolve((String[] stringList) -> {}).getReturnType());
        Assertions.assertEquals(List.class, LambdaHelper.<SerCons<List<String>>>resolve((List<String> stringList) -> {}).getParameterTypes()[0]);
        Assertions.assertEquals(void.class, LambdaHelper.<SerCons<List<String>>>resolve((List<String> stringList) -> {}).getReturnType());
        Assertions.assertEquals(0, LambdaHelper.<SerFunc<Object, String>>resolve(Object::toString).getParameterTypes().length);
        Assertions.assertEquals(String.class, LambdaHelper.<SerFunc<Object, String>>resolve(Object::toString).getReturnType());
        Assertions.assertEquals(Void.class, LambdaHelper.resolve((Serializable & Function<Void, Void>) w -> w).getReturnType());
        Assertions.assertEquals(new AbstractTypeReference<T[]>() {}.getTypeName(), LambdaHelper.resolve((SerArgsFunc<Object, Steam<?>>) Steam::of).getParameterTypes()[0].getTypeName());
        Assertions.assertEquals(new AbstractTypeReference<Steam<T>>() {}.getTypeName(), LambdaHelper.resolve((SerArgsFunc<Object, Steam<?>>) Steam::of).getReturnType().getTypeName());
    }

    @Test
    void testGetPropertyNames() {
        List<String> propertyNames = LambdaHelper.getPropertyNames(LambdaExecutable::getName, LambdaExecutable::getLambda);
        Assertions.assertEquals("name", propertyNames.get(0));
        Assertions.assertEquals("lambda", propertyNames.get(1));
    }

    @Test
    void testProxy() {
        Assertions.assertEquals(int.class, LambdaHelper.<SerFunc<Integer, String[]>>resolve(String[]::new).getParameterTypes()[0]);
        Assertions.assertEquals(int.class, LambdaHelper.<SerFunc<Integer, Integer[][]>>resolve(Integer[][]::new).getParameterTypes()[0]);
        Assertions.assertEquals(Object.class, LambdaHelper.<SerCons<Object>>resolve(System.out::println).getParameterTypes()[0]);
    }

}
