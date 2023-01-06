package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.lambda.function.SerArgsFunc;
import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.reflect.AbstractTypeReference;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import io.github.vampireachao.stream.core.stream.Steam;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.invoke.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * LambdaHelper测试
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/31 19:51
 */
class LambdaHelperTest {

    @Test
    <T> void testResolve() {
        Assertions.assertEquals(Integer[][].class, LambdaHelper.resolve((Serializable & BiConsumer<Integer[][], Integer>) (i, a) -> {
        }).getParameterTypes()[0]);
        Assertions.assertEquals(Integer.class, LambdaHelper.resolve((Serializable & BiConsumer<Integer[][], Integer>) (i, a) -> {
        }).getParameterTypes()[1]);
        Assertions.assertEquals(Integer.class, LambdaHelper.resolve((Serializable & BiConsumer<Integer, Integer[][][]>) (i, a) -> {
        }).getParameterTypes()[0]);
        Assertions.assertEquals(Integer[][][].class, LambdaHelper.resolve((Serializable & BiConsumer<Integer, Integer[][][]>) (i, a) -> {
        }).getParameterTypes()[1]);
        Assertions.assertEquals(0, LambdaHelper.<SerSupp<Object>>resolve(Object::new).getParameterTypes().length);
        Assertions.assertEquals(Object.class, LambdaHelper.<SerSupp<Object>>resolve(Object::new).getReturnType());
        Assertions.assertEquals(int.class, LambdaHelper.<SerFunc<Integer, Integer[]>>resolve(Integer[]::new).getParameterTypes()[0]);
        Assertions.assertEquals(Integer[].class, LambdaHelper.<SerFunc<Integer, Integer[]>>resolve(Integer[]::new).getReturnType());
        Assertions.assertEquals(int.class, LambdaHelper.<SerFunc<Integer, Integer[][]>>resolve(Integer[][]::new).getParameterTypes()[0]);
        Assertions.assertEquals(Integer[][].class, LambdaHelper.<SerFunc<Integer, Integer[][]>>resolve(Integer[][]::new).getReturnType());
        Assertions.assertEquals(Object.class, LambdaHelper.<SerCons<Object>>resolve(System.out::println).getParameterTypes()[0]);
        Assertions.assertEquals(void.class, LambdaHelper.<SerCons<Object>>resolve(System.out::println).getReturnType());
        Assertions.assertEquals(String[].class, LambdaHelper.<SerCons<String[]>>resolve((String[] stringList) -> {
        }).getParameterTypes()[0]);
        Assertions.assertEquals(String[].class, LambdaHelper.<SerCons<String[]>>resolve((String[] stringList) -> {
        }).getParameterTypes()[0]);
        Assertions.assertEquals(void.class, LambdaHelper.<SerCons<String[]>>resolve((String[] stringList) -> {
        }).getReturnType());
        Assertions.assertEquals(List.class, LambdaHelper.<SerCons<List<String>>>resolve((List<String> stringList) -> {
        }).getParameterTypes()[0]);
        Assertions.assertEquals(void.class, LambdaHelper.<SerCons<List<String>>>resolve((List<String> stringList) -> {
        }).getReturnType());
        Assertions.assertEquals(0, LambdaHelper.<SerFunc<Object, String>>resolve(Object::toString).getParameterTypes().length);
        Assertions.assertEquals(String.class, LambdaHelper.<SerFunc<Object, String>>resolve(Object::toString).getReturnType());
        Assertions.assertEquals(Void.class, LambdaHelper.resolve((Serializable & Function<Void, Void>) w -> w).getReturnType());
        Assertions.assertEquals(new AbstractTypeReference<T[]>() {
        }.getTypeName(), LambdaHelper.resolve((SerArgsFunc<Object, Steam<?>>) Steam::of).getParameterTypes()[0].getTypeName());
        Assertions.assertEquals(Steam.class.getName(), LambdaHelper.resolve((SerArgsFunc<Object, Steam<?>>) Steam::of).getReturnType().getTypeName());
    }

    @Test
    void testGetPropertyNames() {
        List<String> propertyNames = LambdaHelper.getPropertyNames(LambdaExecutable::getName, LambdaExecutable::getLambda);
        Assertions.assertEquals("name", propertyNames.get(0));
        Assertions.assertEquals("lambda", propertyNames.get(1));
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    void testProxy() {
        final LambdaExecutable resolve = LambdaHelper.<SerFunc<A, ?>>resolve(A::getR);
        final Type type = resolve.getInstantiatedTypes()[0];
        final Map<String, Type> genericMap = ReflectHelper.getGenericMap(type);
        Assertions.assertEquals(A.class, genericMap.get("R"));
        Assertions.assertEquals(int.class, LambdaHelper.<SerFunc<Integer, String[]>>resolve(String[]::new).getParameterTypes()[0]);
        Assertions.assertEquals(int.class, LambdaHelper.<SerFunc<Integer, Integer[][]>>resolve(Integer[][]::new).getParameterTypes()[0]);
        Assertions.assertEquals(Object.class, LambdaHelper.<SerCons<Object>>resolve(System.out::println).getParameterTypes()[0]);

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle getR = lookup.findVirtual(B.class, "getR", MethodType.methodType(Object.class));
        SerFunc<A, A> lambda = MethodHandleProxies.asInterfaceInstance(SerFunc.class, getR);
        LambdaExecutable lambdaExecutable = LambdaHelper.resolve(lambda);
        Assertions.assertEquals(B.class, LambdaHelper.resolve(lambda).getInstantiatedTypes()[0]);
    }

    private abstract static class B<T, R> {

        R getR() {
            return null;
        }

    }

    private static class A extends B<Object, A> {
    }

    @Test
    void testRevert() {
        final LambdaExecutable getName = LambdaHelper.<SerFunc<LambdaExecutable, String>>resolve(LambdaExecutable::getName);
        final SerFunc<LambdaExecutable, String> revertedGetName = LambdaHelper.revert(SerFunc.class, getName.getExecutable());
        Assertions.assertEquals(revertedGetName.apply(getName), getName.getName());

        final LambdaExecutable constructor = LambdaHelper.<SerSupp<LambdaExecutable>>resolve(LambdaExecutable::new);
        final SerSupp<LambdaExecutable> revertedConstructor = LambdaHelper.revert(SerSupp.class, constructor.getExecutable());
        Assertions.assertEquals(LambdaExecutable.class, revertedConstructor.get().getClass());
    }


    @Test
    @SneakyThrows
    void testVirtual() {
        final MethodHandle virtual = MethodHandles.lookup().findVirtual(LambdaExecutable.class, "getName", MethodType.methodType(String.class));
        final SerFunc<LambdaExecutable, String> proxy = MethodHandleProxies.asInterfaceInstance(SerFunc.class, virtual);
        InvocationHandler handler = Proxy.getInvocationHandler(proxy);
        MethodHandle methodHandle = ReflectHelper.getFieldValue(handler, "val$target");
        final CallSite callSite = LambdaMetafactory.altMetafactory(
                MethodHandles.lookup(),
                "apply",
                MethodType.methodType(SerFunc.class),
                MethodType.methodType(Object.class, Object.class),
                methodHandle,
                MethodType.methodType(String.class, LambdaExecutable.class),
                LambdaMetafactory.FLAG_SERIALIZABLE
        );
        final MethodHandle target = callSite.getTarget();
        final SerFunc<LambdaExecutable, String> invoke = (SerFunc<LambdaExecutable, String>) target.invoke();
        final LambdaExecutable executable = new LambdaExecutable();
        executable.setName("test");
        Assertions.assertEquals("test", invoke.apply(executable));
        Assertions.assertEquals("getName", LambdaHelper.resolve(invoke).getName());
    }


}
