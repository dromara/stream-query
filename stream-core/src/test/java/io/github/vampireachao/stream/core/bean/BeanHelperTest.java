package io.github.vampireachao.stream.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanHelperTest {
    static Stream<String> methodsNameStream;

    @BeforeAll
    static void setUp() throws Exception {
        methodsNameStream = Arrays.stream(Person.class.getDeclaredMethods()).map(Method::getName);
    }

    @Test
    void testGetPropertyName() {
        Set<String> expect = new HashSet<String>(){{
            add("name");
            add("age");
            add("atWorking");
        }};
        assertEquals(expect,
                methodsNameStream.map(BeanHelper::getPropertyName).collect(toSet())
        );
    }

    @Test
    void testIsGetter() {
        Set<String> isGetterSet = methodsNameStream.filter(name -> name.startsWith("get")).collect(toSet());
        assertEquals(2, isGetterSet.size());
        assertEquals(true, isGetterSet.stream().allMatch(BeanHelper::isGetter));
    }

    @Test
    void testIsGetterBoolean() {
        Set<String> isGetterSet = methodsNameStream.filter(name -> name.startsWith("is")).collect(toSet());
        assertEquals(1, isGetterSet.size());
        assertEquals(true, isGetterSet.stream().allMatch(BeanHelper::isGetterBoolean));

    }

    @Test
    void testIsSetter() {
        Set<String> isGetterSet = methodsNameStream.filter(name -> name.startsWith("set")).collect(toSet());
        assertEquals(3, isGetterSet.size());
        assertEquals(true, isGetterSet.stream().allMatch(BeanHelper::isSetter));
    }
}

@Getter
@Setter
@AllArgsConstructor
class Person {
    private String name;
    private Integer age;
    private boolean atWorking;
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme