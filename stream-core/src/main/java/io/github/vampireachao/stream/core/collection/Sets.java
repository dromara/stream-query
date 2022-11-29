package io.github.vampireachao.stream.core.collection;

import java.util.*;

/**
 * @author VampireAchao
 * @since 2022/10/21 17:10
 */
public class Sets {

    private Sets() {
        /* Do not new me! */
    }

    @SafeVarargs
    public static <T> Set<T> of(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    public static <T> Set<T> ofColl(Collection<T> values) {
        if (Objects.isNull(values)) {
            return new HashSet<>();
        }
        return new HashSet<>(values);
    }

}
