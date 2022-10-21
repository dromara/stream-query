package io.github.vampireachao.stream.core.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author VampireAchao
 * @since 2022/10/21 17:08
 */
public class Lists {

    private Lists() {
        /* Do not new me! */
    }

    @SafeVarargs
    public static <T> List<T> of(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

}
