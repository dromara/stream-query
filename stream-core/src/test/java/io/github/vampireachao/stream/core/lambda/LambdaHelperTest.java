package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

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
        Constructor<Object> objectConstructor = (Constructor<Object>) LambdaHelper.resolve((SerSupp<Object>) Object::new);
        Assertions.assertNotNull(objectConstructor);

        Method intArrayConstructor = (Method) LambdaHelper.resolve((SerFunc<Integer, Integer[]>) Integer[]::new);
        Assertions.assertNotNull(intArrayConstructor);

        Method systemOutPrintln = (Method) LambdaHelper.resolve((SerCons<Object>) System.out::println);
        Assertions.assertNotNull(systemOutPrintln);
    }

}
