package io.github.vampireachao.stream.core.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author VampireAchao
 * @since 2022/7/19 14:14
 */
@SuppressWarnings("serial")
class SteamTest {

    @Test
    void testOf() {
        Assertions.assertEquals(3, Steam.of(Arrays.asList(1, 2, 3)).count());
        Assertions.assertEquals(3, Steam.of(1, 2, 3).count());
    }

    @Test
    void testToCollection() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> toCollection = Steam.of(list).map(String::valueOf).toCollection(LinkedList::new);
        Assertions.assertEquals(Arrays.asList("1", "2", "3"), toCollection);
    }

    @Test
    void testToList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> toList = Steam.of(list).map(String::valueOf).toList();
        Assertions.assertEquals(Arrays.asList("1", "2", "3"), toList);
    }

    @Test
    void testToSet() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Set<String> toSet = Steam.of(list).map(String::valueOf).toSet();
        Assertions.assertEquals(new HashSet<>(Arrays.asList("1", "2", "3")), toSet);
    }

    @Test
    void testJoining() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        String joining = Steam.of(list).joining();
        Assertions.assertEquals("123", joining);
        Assertions.assertEquals("1,2,3", Steam.of(list).joining(","));
        Assertions.assertEquals("(1,2,3)", Steam.of(list).joining(",", "(", ")"));
    }

    @Test
    void testToMap() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Map<String, Integer> identityMap = Steam.of(list).toMap(String::valueOf);
        Assertions.assertEquals(new HashMap<String, Integer>() {{
            put("1", 1);
            put("2", 2);
            put("3", 3);
        }}, identityMap);
    }


}
