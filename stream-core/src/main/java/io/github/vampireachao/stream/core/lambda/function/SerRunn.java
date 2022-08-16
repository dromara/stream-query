package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * 可序列化的Runnable
 *
 * @author VampireAchao
 * @see java.lang.Runnable
 */
@FunctionalInterface
public interface SerRunn extends Runnable, Serializable {

    /**
     * multi
     *
     * @param serRunns lambda
     * @return lambda
     */
    static SerRunn multi(SerRunn... serRunns) {
        return () -> Stream.of(serRunns).forEach(SerRunn::run);
    }

}
