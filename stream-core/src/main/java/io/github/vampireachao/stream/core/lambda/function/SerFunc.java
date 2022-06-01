package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的Function
 *
 * @param <T>
 * @author VampireAchao
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface SerFunc<T, R> extends Function<T, R>, Serializable {

}