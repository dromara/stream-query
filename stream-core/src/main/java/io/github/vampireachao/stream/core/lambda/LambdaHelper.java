package io.github.vampireachao.stream.core.lambda;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * LambdaHelper
 *
 * @author <achao1441470436@gmail.com>
 * @since 2022/5/29 9:19
 */
public class LambdaHelper {

    private LambdaHelper() {
        /* Do not new me! */
    }

    public static SerializedLambda serialize(Serializable lambda) {
        if (lambda instanceof SerializedLambda) {
            return (SerializedLambda) lambda;
        }
        Objects.requireNonNull(lambda, "lambda不能");
        final Class<? extends Serializable> lambdaClass = lambda.getClass();
        if (!lambdaClass.isSynthetic()) {
            throw new IllegalArgumentException("Not a lambda expression: " + lambdaClass.getName());
        }
        try {
            return (SerializedLambda) lambdaClass.getDeclaredMethod("writeReplace").invoke(lambda);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }


}
