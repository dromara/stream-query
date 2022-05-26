package io.github.vampireachao.stream.core;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * stream辅助类
 *
 * @author <achao1441470436@gmail.com>
 * @since 2022/5/26 19:11
 */
public class StreamHelper {

    private StreamHelper() {
        /* Do not new me! */
    }

    public static <T, R> Set<R> mapToSet(Stream<T> stream, Function<T, R> function) {
        return stream.map(function).collect(Collectors.toSet());
    }

    public static <T, R> Set<R> mapToSet(List<T> dataList, Function<T, R> function) {
        return dataList.stream().map(function).collect(Collectors.toSet());
    }


}
