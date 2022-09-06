package io.github.vampireachao.stream.core.reflect;

import java.lang.reflect.Type;

/**
 * 单个泛型类型
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/6/2 18:53
 */
public abstract class AbstractTypeReference<T> implements Type {

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
        return ReflectHelper.getGenericTypes(this.getClass())[0].getTypeName();
    }

    public Type getType() {
        return ReflectHelper.getGenericTypes(this.getClass())[0];
    }
}