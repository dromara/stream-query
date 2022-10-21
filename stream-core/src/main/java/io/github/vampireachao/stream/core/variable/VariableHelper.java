package io.github.vampireachao.stream.core.variable;

import io.github.vampireachao.stream.core.stream.Steam;

import java.util.function.Supplier;

/**
 * @author VampireAchao
 * @since 2022/10/21 15:56
 */
public class VariableHelper {

    public static <T> T first(T[] variables, Supplier<T> orGet) {
        return Steam.of(variables).findFirst().orElseGet(orGet);
    }
}
