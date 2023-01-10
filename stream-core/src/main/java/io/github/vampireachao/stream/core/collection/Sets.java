package io.github.vampireachao.stream.core.collection;

import java.util.*;

/**
 * <p>Sets class.</p>
 *
 * @author VampireAchao
 * @since 2022/10/21 17:10
 */
public class Sets {

    private Sets() {
        /* Do not new me! */
    }

    /**
     * <p>of.</p>
     *
     * @param values a T object
     * @param <T>    a T class
     * @return a {@link java.util.Set} object
     */
    @SafeVarargs
    public static <T> Set<T> of(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    /**
     * <p>ofColl.</p>
     *
     * @param values a {@link java.util.Collection} object
     * @param <T>    a T class
     * @return a {@link java.util.Set} object
     */
    public static <T> Set<T> ofColl(Collection<T> values) {
        if (Objects.isNull(values)) {
            return new HashSet<>();
        }
        return new HashSet<>(values);
    }


    public static <T> Set<T> empty() {
        return Collections.emptySet();
    }

}
