package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * 可序列化的Predicate
 *
 * @author VampireAchao
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface SerPred<T> extends Predicate<T>, Serializable {

}