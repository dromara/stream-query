package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * 可序列化的Consumer
 *
 * @author VampireAchao
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface SerCons<T> extends Consumer<T>, Serializable {

}