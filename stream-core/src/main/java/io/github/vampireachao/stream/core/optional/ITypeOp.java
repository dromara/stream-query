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

    Op<T> typeOf(SerCons<? super T> action);

    <U> Op<U> typeOfMap(SerFunc<? super T, ? extends U> mapper);

    Op<T> typeOfFilter(SerPred<? super T> condition);

    Op<T> typeOf(Type type, SerCons<? super T> action);

    <U> Op<U> typeOfMap(Type type, SerFunc<? super T, ? extends U> mapper);

    Op<T> typeOfFilter(Type type, SerPred<? super T> predicate);

    default <S extends Serializable> boolean isNotInstance(T value, S lambda) {
        return !ReflectHelper.isInstance(value, SerArgsUnOp.<Type>last().apply(LambdaHelper.resolve(lambda).getParameterTypes()));
    }

}
