package org.dromara.streamquery.stream.core.reflect;

import java.lang.reflect.Type;

/**
 * 单个泛型类型
 *
 * @author VampireAchao Cizai_

 * @since 2022/6/2 18:53
 */
public abstract class AbstractTypeReference<T> implements Type {

    /**
     * <p>
     * Returns a string describing this type, including information
     * about any type parameters.
     *
     * @implSpec The default implementation calls {@code toString}.
     * @since 1.8
     */
    @Override
    public String getTypeName() {
        return ReflectHelper.getGenericTypes(this.getClass())[0].getTypeName();
    }

    /**
     * <p>getType.</p>
     *
     * @return a {@link java.lang.reflect.Type} object
     */
    public Type getType() {
        return ReflectHelper.getGenericTypes(this.getClass())[0];
    }
}
