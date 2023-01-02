package io.github.vampireachao.stream.core.lambda.function;

/**
 * SerArgsUnOp
 *
 * @author VampireAchao Cizai_

 * @since 2022/6/8
 */
@FunctionalInterface
public interface SerArgsUnOp<T> extends SerArgsFunc<T, T> {


    /**
     * Returns a unary operator that always returns its input argument.
     *
     * @param <T> the type of the input and output of the operator
     * @return a unary operator that always returns its input argument
     */
    static <T> SerArgsUnOp<T> last() {
        return t -> t != null && t.length > 0 ? t[t.length - 1] : null;
    }

}
