package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.LambdaHelper;
import io.github.vampireachao.stream.core.lambda.function.SerArgsUnOp;
import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerPred;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * ITypeOp
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/4
 */
public interface ITypeOp<T> extends BaseOp<T> {

    /**
     * <p>typeOf.</p>
     *
     * @param action a {@link io.github.vampireachao.stream.core.lambda.function.SerCons} object
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    Op<T> typeOf(SerCons<? super T> action);

    /**
     * <p>typeOfMap.</p>
     *
     * @param mapper a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    <U> Op<U> typeOfMap(SerFunc<? super T, ? extends U> mapper);

    /**
     * <p>typeOfFilter.</p>
     *
     * @param condition a {@link io.github.vampireachao.stream.core.lambda.function.SerPred} object
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    Op<T> typeOfFilter(SerPred<? super T> condition);

    /**
     * <p>typeOf.</p>
     *
     * @param type   a {@link java.lang.reflect.Type} object
     * @param action a {@link io.github.vampireachao.stream.core.lambda.function.SerCons} object
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    Op<T> typeOf(Type type, SerCons<? super T> action);

    /**
     * <p>typeOfMap.</p>
     *
     * @param type   a {@link java.lang.reflect.Type} object
     * @param mapper a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    <U> Op<U> typeOfMap(Type type, SerFunc<? super T, ? extends U> mapper);

    /**
     * <p>typeOfFilter.</p>
     *
     * @param type      a {@link java.lang.reflect.Type} object
     * @param predicate a {@link io.github.vampireachao.stream.core.lambda.function.SerPred} object
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    Op<T> typeOfFilter(Type type, SerPred<? super T> predicate);

    /**
     * <p>isNotInstance.</p>
     *
     * @param value  a T object
     * @param lambda a S object
     * @param <S>    a S class
     * @return a boolean
     */
    default <S extends Serializable> boolean isNotInstance(T value, S lambda) {
        return !ReflectHelper.isInstance(value, SerArgsUnOp.<Type>last().apply(LambdaHelper.resolve(lambda).getParameterTypes()));
    }

}
