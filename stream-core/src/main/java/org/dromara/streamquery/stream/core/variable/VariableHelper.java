package org.dromara.streamquery.stream.core.variable;

import org.dromara.streamquery.stream.core.stream.Steam;

import java.util.function.Supplier;

/**
 * <p>VariableHelper class.</p>
 *
 * @author VampireAchao
 * @since 2022/10/21 15:56
 */
public class VariableHelper {

    private VariableHelper() {
        /* Do not new me! */
    }

    /**
     * <p>first.</p>
     *
     * @param variables an array of T[] objects
     * @param orGet     a {@link java.util.function.Supplier} object
     * @param <T>       a T class
     * @return a T object
     */
    public static <T> T first(T[] variables, Supplier<T> orGet) {
        return Steam.of(variables).findFirst().orElseGet(orGet);
    }
}
