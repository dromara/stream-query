package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 可序列化的Supplier
 *
 * @param <T>
 * @author VampireAchao
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface SerSupp<T> extends Supplier<T>, Serializable {

}