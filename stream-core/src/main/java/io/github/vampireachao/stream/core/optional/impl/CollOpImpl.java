package io.github.vampireachao.stream.core.optional.impl;

import io.github.vampireachao.stream.core.optional.*;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @Description: CollOpImpl
 * @DateTime: 2022/9/9 19:43
 **/
public class CollOpImpl<E> implements CollOp<E> {

    protected Collection<E> list;

    public CollOpImpl(Collection<E> collection) {
        Boolean F = Boolean.TRUE;
        if (collection == null || collection.isEmpty()) {
            this.list = null;
            F = Boolean.FALSE;
        }
        if (F){
            for (E t : collection) {
                if (t != null) {
                    this.list = collection;
                    break;
                }
            }
        }else {
            // 集合中元素全部为空
            this.list = null;
        }
    }

    @Override
    public boolean isPresent() {
        return Objects.nonNull(this.list);
    }

    @Override
    public boolean isEmpty() {
        return Objects.isNull(this.list);
    }

    @Override
    public boolean isEqual(Collection<E> value) {
        return !this.list.retainAll(value);
    }

    @Override
    public boolean is(Predicate<Collection<E>> predicate) {

        return filterI(predicate).isPresent();
    }

    @Override
    public Collection<E> get() {
        return this.list;
    }

    @Override
    public Collection<E> orElse(Collection<E> other) {
        return isPresent() ? list : other;
    }

    @Override
    public Collection<E> orElseGet(Supplier<Collection<E>> other) {
        return isPresent() ? list : other.get();
    }

    @Override
    public Collection<E> orElseRun(Runnable other) {
        if (isEmpty()) {
            other.run();
        }
        return this.list;
    }

    @Override
    public Collection<E> orElseThrow() {
        return orElseThrow(NoSuchElementException::new);
    }

    @Override
    public <X extends Throwable> Collection<E> orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return this.list;

        }
        throw  exceptionSupplier.get();
    }

    @Override
    public <U> CollOp<U> map(Function<? super E, U> mapper) {
        List<U> us = Steam.of(this.list).map(mapper).toList();
        CollOp<U> of = CollOp.of(us);
        return isPresent() ? of : CollOp.empty();
    }



    @Override
    public <U> Op<U> flatMap(Function<? super Collection<E>, ? extends Op<? extends U>> mapper) {
        return isPresent() ? Op.of(mapper.apply(this.list).get()) : Op.empty();
    }

    @Override
    public <U> Optional<U> flatMapToOptional(Function<? super Collection<E>, ? extends Optional<? extends U>> mapper) {
        return isPresent() ? Optional.of(mapper.apply(this.list).get()) : Optional.empty();
    }

    @Override
    public CollOp<E> filter(Predicate<? super E> predicate) {
        return CollOp.of(Steam.of(this.list).filter(predicate).toList()) ;
    }


    private CollOp<E> filterI(Predicate<Collection<E>> predicate) {
        return isPresent() && predicate.test(this.list) ? this : CollOp.empty();
    }

    @Override
    public CollOp<E> ifPresent(Consumer<? super Collection<E>> action) {
        if (isPresent()) {
            action.accept(this.list);
        }
        return this;
    }

    @Override
    public CollOp<E> or(Supplier<CollOp<E>> other) {
        return isPresent() ? this :  other.get();
    }

    @Override
    public Steam<E> steam() {
        return Steam.of(this.list);
    }
}
