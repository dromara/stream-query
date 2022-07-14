package io.github.vampireachao.stream.core.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 单个泛型类型
 *
 * @author VampireAchao
 * @since 2022/6/2 18:53
 */
public abstract class TypeReference<T> implements Type {

    /**
     * Returns a string describing this type, including information
     * about any type parameters.
     *
     * @return a string describing this type
     * @implSpec The default implementation calls {@code toString}.
     * @since 1.8
     */
    @Override
    public String getTypeName() {
        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
    }
}