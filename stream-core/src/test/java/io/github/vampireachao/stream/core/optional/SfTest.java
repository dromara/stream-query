package io.github.vampireachao.stream.core.optional;

import org.junit.jupiter.api.Test;

/**
 * @author VampireAchao
 * @since 2022/9/15
 */
class SfTest {

    @Test
    void testOf() {
        Sf.of(1).$let(i -> i + 1);
    }


}
