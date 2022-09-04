package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerRunn;
import io.github.vampireachao.stream.core.reflect.AbstractTypeReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * @author VampireAchao
 * @since 2022/6/2 19:06
 */
@SuppressWarnings("unchecked")
class OpTest {

    @Test
    void blankTest() {
        // blank相对于ofNullable考虑了字符串为空串的情况
        final String hutool = StrOp.of("").orElse("hutool");
        Assertions.assertEquals("hutool", hutool);
    }

    @Test
    void getTest() {
        // 和原版Optional有区别的是，get不会抛出NoSuchElementException
        // 如果想使用原版Optional中的get这样，获取一个一定不为空的值，则应该使用orElseThrow
        final Object opp = Op.empty().get();
        Assertions.assertNull(opp);
    }

    @Test
    void isEmptyTest() {
        // 这是jdk11 Optional中的新函数，直接照搬了过来
        // 判断包裹内元素是否为空，注意并没有判断空字符串的情况
        final boolean isEmpty = Op.empty().isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    @Test
    void peekTest() {
        final User user = new User();
        // 相当于ifPresent的链式调用
        Op.of("hutool").ifPresent(user::setUsername).ifPresent(user::setNickname);
        Assertions.assertEquals("hutool", user.getNickname());
        Assertions.assertEquals("hutool", user.getUsername());

        // 注意，传入的lambda中，对包裹内的元素执行赋值操作并不会影响到原来的元素
        final String name = Op.of("hutool").ifPresent(username -> username = "123").ifPresent(username -> username = "456").get();
        Assertions.assertEquals("hutool", name);
    }

    @Test
    void peeksTest() {
        final User user = new User();
        // 可以一行搞定
        Op.of("hutool").ifPresent(SerCons.multi(user::setUsername, user::setNickname));
        // 也可以在适当的地方换行使得代码的可读性提高
        Op.of(user).ifPresent(SerCons.multi(
                u -> Assertions.assertEquals("hutool", u.getNickname()),
                u -> Assertions.assertEquals("hutool", u.getUsername())
        ));
        Assertions.assertEquals("hutool", user.getNickname());
        Assertions.assertEquals("hutool", user.getUsername());

        // 注意，传入的lambda中，对包裹内的元素执行赋值操作并不会影响到原来的元素,这是java语言的特性。。。
        // 这也是为什么我们需要getter和setter而不直接给bean中的属性赋值中的其中一个原因
        final String name = Op.of("hutool").ifPresent(SerCons.multi(
                username -> username = "123", username -> username = "456",
                n -> Assertions.assertEquals("hutool", n))).get();
        Assertions.assertEquals("hutool", name);
    }

    @Test
    void orTest() {
        // 这是jdk9 Optional中的新函数，直接照搬了过来
        // 给一个替代的Opp
        final String str = Op.<String>empty().or(() -> Op.of("Hello hutool!")).map(String::toUpperCase).orElseThrow();
        Assertions.assertEquals("HELLO HUTOOL!", str);

        final User user = User.builder().username("hutool").build();
        final Op<User> userOp = Op.of(user);
        // 获取昵称，获取不到则获取用户名
        final String name = userOp.map(User::getNickname).or(() -> userOp.map(User::getUsername)).get();
        Assertions.assertEquals("hutool", name);

        final String strOpt = Op.ofOptional(Optional.of("Hello hutool!")).map(String::toUpperCase).orElseThrow();
        Assertions.assertEquals("HELLO HUTOOL!", strOpt);
    }

    @Test
    void testSteam() {
        List<Integer> collToSteam = CollOp.of(Arrays.asList(1, 2, 2, 3)).steam().distinct().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), collToSteam);

        Assertions.assertEquals(1, Op.of(1).steam().findAny().orElse(null));
    }

    @Test
    void orElseThrowTest() {
        Op<Object> op = Op.empty();
        // 获取一个不可能为空的值，否则抛出NoSuchElementException异常
        Assertions.assertThrows(NoSuchElementException.class, op::orElseThrow);
        // 获取一个不可能为空的值，否则抛出自定义异常
        Assertions.assertThrows(IllegalStateException.class,
                () -> op.orElseThrow(IllegalStateException::new));
    }

    @Test
    void orElseRunTest() {
        // 判断一个值是否为空，为空执行一段逻辑,否则执行另一段逻辑
        final Map<String, Integer> map = new HashMap<>();
        final String key = "key";
        map.put(key, 1);
        Op.of(map.get(key))
                .ifPresent(v -> map.put(key, v + 1))
                .orElseRun(() -> map.remove(key));
        Assertions.assertEquals((Object) 2, map.get(key));
    }

    @Test
    void flattedMapTest() {
        // 和Optional兼容的flatMap
        final List<User> userList = new ArrayList<>();
        // 以前，不兼容
//		Op.ofNullable(userList).map(List::stream).flatMap(Stream::findFirst);
        // 现在，兼容
        final User user = Op.of(userList).map(List::stream)
                .flatMapToOptional(Stream::findFirst).orElseGet(User.builder()::build);
        Assertions.assertNull(user.getUsername());
        Assertions.assertNull(user.getNickname());
    }

    @Test
    void emptyTest() {
        final Collection<String> past = Op.of(Collections.<String>emptyList()).filter(l -> !l.isEmpty()).orElseGet(() -> Collections.singletonList("hutool"));
        final Collection<String> hutool = CollOp.of(Collections.<String>emptyList()).orElseGet(() -> Collections.singletonList("hutool"));
        Assertions.assertEquals(past, hutool);
        Assertions.assertEquals(Collections.singletonList("hutool"), hutool);
        Assertions.assertTrue(CollOp.of(Arrays.asList(null, null, null)).isEmpty());
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "ConstantConditions"})
    @Test
    void failOrElseTest() {
        // 有一些资深的程序员跟我说你这个lambda，双冒号语法糖看不懂...
        // 为了尊重资深程序员的意见，并且提升代码可读性，封装了一下 "try catch NPE 和 数组越界"的情况

        // 以前这种写法，简洁但可读性稍低，对资深程序员不太友好
        final List<String> last = null;
        final String npeSituation = CollOp.of(last).flatMapToOptional(l -> l.stream().findFirst()).orElse("hutool");
        final String indexOutSituation = CollOp.of(last).flatMapToOptional(l -> l.stream().findFirst()).orElse("hutool");

        // 现在代码整洁度降低，但可读性up，如果再人说看不懂这代码...
        final String npe = ThrowOp.of(() -> last.get(0)).orElse("hutool");
        final String indexOut = ThrowOp.of(() -> {
            final List<String> list = new ArrayList<>();
            // 你可以在里面写一长串调用链 list.get(0).getUser().getId()
            return list.get(0);
        }).orElse("hutool");
        Assertions.assertEquals(npe, npeSituation);
        Assertions.assertEquals(indexOut, indexOutSituation);
        Assertions.assertEquals("hutool", npe);
        Assertions.assertEquals("hutool", indexOut);

        // 多线程下情况测试
        Stream.iterate(0, i -> ++i).limit(20000).parallel().forEach(i -> {
            final ThrowOp<Object> op = ThrowOp.of(() -> {
                if (i % 2 == 0) {
                    throw new IllegalStateException(i + "");
                } else {
                    throw new NullPointerException(i + "");
                }
            });
            Assertions.assertTrue(
                    (i % 2 == 0 && op.getException() instanceof IllegalStateException) ||
                            (i % 2 != 0 && op.getException() instanceof NullPointerException)
            );
        });

        Assertions.assertDoesNotThrow(() -> ThrowOp.of(() -> {
            throw new NullPointerException();
        }, NullPointerException.class, IllegalStateException.class));
        Assertions.assertDoesNotThrow(() -> ThrowOp.of(() -> {
            throw new IllegalStateException();
        }, NullPointerException.class, IllegalStateException.class));

        Assertions.assertThrows(RuntimeException.class, () -> ThrowOp.of(() -> {
            throw new IllegalStateException();
        }, NullPointerException.class));

    }

    @Test
    void testEmpty() {
        Assertions.assertTrue(CollOp.of(Arrays.asList(null, null, null)).isEmpty());
        Assertions.assertTrue(CollOp.of(Arrays.asList(null, 1, null)).isPresent());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class User {
        private String username;
        private String nickname;
    }

    @Test
    void testtypeOf() {
        Stream.<SerRunn>of(() -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Op<String> op = Op.of("").typeOf((String str) -> isExecute.set(true));
            Assertions.assertTrue(op.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Op<String> op = Op.of("").typeOf(Object.class, (Object str) -> isExecute.set(true));
            Assertions.assertTrue(op.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Op<int[]> op = Op.of(new int[]{1, 2}).typeOf((int[] array) -> isExecute.set(true));
            Assertions.assertTrue(op.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Op<List<Integer>> op = Op.of(Arrays.asList(1, 2, 3, 4)).typeOf((List<Integer> array) -> isExecute.set(true));
            Assertions.assertTrue(op.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Op<List<Integer>> op = Op.of(Arrays.asList(1, 2, 3)).typeOf(List.class, (array) -> isExecute.set(true));
            Assertions.assertTrue(op.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Op<Map<Integer, String>> op = Op.of(Collections.singletonMap(1, "")).typeOf(new AbstractTypeReference<Map<Integer, String>>() {}.getClass(), (array) -> isExecute.set(true));
            Assertions.assertTrue(op.isPresent());
            Assertions.assertTrue(isExecute.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Op<Map<Integer, String>> op = Op.of(Collections.singletonMap(1, "")).typeOf(new AbstractTypeReference<Map<Integer, String>>() {}.getClass(), (array) -> isExecute.set(true));
            Assertions.assertTrue(op.isPresent());
            Assertions.assertTrue(isExecute.get());
        }).forEach(SerRunn::run);
    }

    @Test
    void testTypeOfMap() {
        Stream.<SerRunn>of(() -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Op<Boolean> op = Op.of("").typeOfMap((String str) -> {
                isExecute.set(true);
                return isExecute.get();
            });
            Assertions.assertTrue(op.get());
        }, () -> {
            AtomicBoolean isExecute = new AtomicBoolean();
            Op<Boolean> op = Op.of("").typeOfMap((String str) -> {
                isExecute.set(true);
                return isExecute.get();
            }).typeOfMap(Object.class, i -> false).typeOfMap(new AbstractTypeReference<String>() {}, i -> true);
            Assertions.assertTrue(op.isEmpty());
        }).forEach(SerRunn::run);
    }


    @Test
    void testTypeOfFilter() {
        Stream.<SerRunn>of(() -> {
            Op<String> op = Op.of("").typeOfFilter((String str) -> str.trim().isEmpty());
            Assertions.assertTrue(op.isPresent());
        }, () -> {
            Op<String> op = Op.of("").typeOfFilter((String str) -> !str.trim().isEmpty());
            Assertions.assertTrue(op.isEmpty());
        }).forEach(SerRunn::run);
    }

    @Test
    void testIsEqual() {
        Assertions.assertTrue(Op.of(1).isEqual(1));
    }

}
