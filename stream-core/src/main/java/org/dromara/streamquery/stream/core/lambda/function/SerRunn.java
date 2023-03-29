package org.dromara.streamquery.stream.core.lambda.function;

import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * 可序列化的Runnable
 *
 * @author VampireAchao Cizai_

 * @see java.lang.Runnable
 */
@FunctionalInterface
public interface SerRunn extends Runnable, Serializable {

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @throws java.lang.Exception if any.
     * @see Thread#run()
     */
    @SuppressWarnings("all")
    void running() throws Throwable;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    default void run() {
        try {
            running();
        } catch (Throwable e) {
            throw new LambdaInvokeException(e);
        }
    }

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
