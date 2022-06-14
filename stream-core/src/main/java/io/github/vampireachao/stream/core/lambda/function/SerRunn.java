package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;

/**
 * 可序列化的Runnable
 *
 * @author VampireAchao
 * @see java.lang.Runnable
 */
@FunctionalInterface
public interface SerRunn extends Runnable, Serializable {
}
