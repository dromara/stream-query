package io.github.vampireachao.stream.core.optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author VampireAchao
 * @since 2022/6/2 19:06
 */
class OppTest {

    @Test
    void testTypeOfPeek() {
        Assertions.assertAll(() -> {
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
            Opp<List<Integer>> opp = Opp.ofNullable(Arrays.asList(1, 2, 3)).typeOfPeek((List<Integer> array) -> isExecute.set(true));
            Assertions.assertTrue(opp.isPresent());
            Assertions.assertTrue(isExecute.get());
        });
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
            }).typeOfMap(Object.class, i -> false);
            Assertions.assertTrue(opp.isEmpty());
        });
    }
}
