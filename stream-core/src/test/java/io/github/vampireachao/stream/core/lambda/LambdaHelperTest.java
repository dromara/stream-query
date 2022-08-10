package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.Executable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * LambdaHelper测试
 *
 * @author VampireAchao
 * @since 2022/5/31 19:51
 */
@SuppressWarnings("unchecked")
class LambdaHelperTest {

    @Test
    void testResolve() {
        Assertions.assertAll(() -> {
                    LambdaExecutable lambdaExecutable = LambdaHelper.resolve((SerSupp<Object>) Object::new);
                    Assertions.assertEquals(0, lambdaExecutable.getParameterTypes().length);
                    Assertions.assertEquals(Object.class, lambdaExecutable.getReturnType());
                }, () -> {
                    LambdaExecutable lambdaExecutable = LambdaHelper.resolve((SerFunc<Integer, Integer[]>) Integer[]::new);
                    Assertions.assertEquals(int.class, lambdaExecutable.getParameterTypes()[0]);
                    Assertions.assertEquals(Integer.class, ((Class<Array>) lambdaExecutable.getReturnType()).getComponentType());
                }, () -> {
                    LambdaExecutable lambdaExecutable = LambdaHelper.resolve((SerCons<Object>) System.out::println);
                    Assertions.assertEquals(Object.class, lambdaExecutable.getParameterTypes()[0]);
                    Assertions.assertEquals(void.class, lambdaExecutable.getReturnType());
                }, () -> {
                    LambdaExecutable lambdaExecutable = LambdaHelper.resolve((SerCons<String[]>) (String[] stringList) -> {});
                    Assertions.assertInstanceOf(Class.class, lambdaExecutable.getParameterTypes()[0]);
                    Assertions.assertEquals(String.class, ((Class<Array>) lambdaExecutable.getParameterTypes()[0]).getComponentType());
                    Assertions.assertEquals(void.class, lambdaExecutable.getReturnType());
                }, () -> {
                    LambdaExecutable lambdaExecutable = LambdaHelper.resolve((SerCons<List<String>>) (List<String> stringList) -> {});
                    Assertions.assertEquals(List.class, lambdaExecutable.getParameterTypes()[0]);
                    Assertions.assertEquals(void.class, lambdaExecutable.getReturnType());
                }, () -> {
                    LambdaExecutable lambdaExecutable = LambdaHelper.resolve((SerFunc<Object, String>) Object::toString);
                    Assertions.assertEquals(0, lambdaExecutable.getParameterTypes().length);
                    Assertions.assertEquals(String.class, lambdaExecutable.getReturnType());
                }, () -> {
                    LambdaExecutable resolve = LambdaHelper.resolve((Serializable & Function<Void, Void>) w -> w);
                    Assertions.assertEquals(ReflectHelper.getDescriptor(resolve.getExecutable()), resolve.getLambda().getImplMethodSignature());
                }
        );
    }

    @Test
    void testGetPropertyNames() {
        String[] propertyNames = LambdaHelper.getPropertyNames(LambdaExecutable::getName, LambdaExecutable::getLambda);
        Assertions.assertEquals("name", propertyNames[0]);
        Assertions.assertEquals("lambda", propertyNames[1]);
    }

    @Test
    void testProxy() {
        LambdaExecutable lambdaExecutable = LambdaHelper.resolve((Serializable & BiFunction<Executable, SerializedLambda, LambdaExecutable>) LambdaExecutable::new);
        LambdaExecutable resolve = LambdaHelper.resolve((Serializable & Function<LambdaExecutable, String>) LambdaExecutable::getName);
    }

}
