package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * @author VampireAchao
 * @since 2022/6/2 19:06
 */
class OppTest {

    @Test
    void testOfEmptyAble() {
        Assertions.assertTrue(Opp.ofEmptyAble(Arrays.asList(null, null, null)).isEmpty());
        Assertions.assertTrue(Opp.ofEmptyAble(Arrays.asList(null, 1, null)).isPresent());
    }

    @Test
    void testTypeOfPeek() {
        Stream.<Runnable>of(() -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Opp<String> opp = Opp.ofNullable("").typeOfPeek((String str) -> {
                isExecute.set(true);
            });
            Assertions.assertTrue(opp.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Opp<String> opp = Opp.ofNullable("").typeOfPeek(Object.class, (Object str) -> isExecute.set(true));
            Assertions.assertTrue(opp.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Opp<int[]> opp = Opp.ofNullable(new int[]{1, 2}).typeOfPeek((int[] array) -> isExecute.set(true));
            Assertions.assertTrue(opp.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Opp<List<Integer>> opp = Opp.ofNullable(Arrays.asList(1, 2, 3, 4)).typeOfPeek((List<Integer> array) -> isExecute.set(true));
            Assertions.assertTrue(opp.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Opp<List<Integer>> opp = Opp.ofNullable(Arrays.asList(1, 2, 3)).typeOfPeek(List.class, (array) -> isExecute.set(true));
            Assertions.assertTrue(opp.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Opp<Map<Integer, String>> opp = Opp.ofNullable(Collections.singletonMap(1, "")).typeOfPeek(new TypeReference<Map<Integer, String>>() {}.getClass(), (array) -> isExecute.set(true));
            Assertions.assertTrue(opp.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Opp<Map<Integer, String>> opp = Opp.ofNullable(Collections.singletonMap(1, "")).typeOfPeek(new TypeReference<Map<Integer, String>>() {}.getClass(), (array) -> isExecute.set(true));
            Assertions.assertTrue(opp.isPresent());
            Assertions.assertTrue(isExecute.get());
        }).forEach(Runnable::run);
    }

    @Test
    void testTypeOfMap() {
        Assertions.assertAll(() -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Opp<Boolean> opp = Opp.ofNullable("").typeOfMap((String str) -> {
                isExecute.set(true);
                return isExecute.get();
            });
            Assertions.assertTrue(opp.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Opp<Boolean> opp = Opp.ofNullable("").typeOfMap((String str) -> {
                isExecute.set(true);
                return isExecute.get();
            }).typeOfMap(Object.class, i -> false).typeOfMap(new TypeReference<String>() {}, i -> true);
            Assertions.assertTrue(opp.isEmpty());
        });
    }
}
