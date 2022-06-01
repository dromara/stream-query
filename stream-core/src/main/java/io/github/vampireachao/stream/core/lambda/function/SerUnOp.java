package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.function.UnaryOperator;

/**
 * 可序列化的UnaryOperator
 *
 * @author VampireAchao
 * @see java.util.function.UnaryOperator
 */
@FunctionalInterface
public interface SerUnOp<T> extends UnaryOperator<T>, Serializable {

}