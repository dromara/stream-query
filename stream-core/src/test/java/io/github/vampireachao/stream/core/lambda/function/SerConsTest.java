package io.github.vampireachao.stream.core.lambda.function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author VampireAchao
 * @since 2023/1/30 10:50
 */
class SerConsTest {

    @Test
    void testMulti() {
        Assertions.assertDoesNotThrow(() -> {
            SerCons.multi().accept(new Object());
        });
    }
}
