package io.github.vampireachao.stream.core.optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

/**
 * @author VampireAchao
 * @since 2022/6/2 16:26
 */
class OppTest {

    @Test
    void test() {
        List<Object> objects = Opp.ofEmptyAble(Collections.emptyList()).map(d -> d).orDefault();
        Assertions.assertNotNull(objects);
    }
}
