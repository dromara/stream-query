/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.streamquery.stream.core.stream.collector;

import org.dromara.streamquery.stream.core.collection.Maps;
import org.dromara.streamquery.stream.core.lambda.function.SerBiOp;
import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.lambda.function.SerPred;
import org.dromara.streamquery.stream.core.lambda.function.SerUnOp;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.dromara.streamquery.stream.core.clazz.ClassHelper.cast;

/**
 * 收集器
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/29 8:55
 */
public class Collective {

  static final Set<Collector.Characteristics> CH_CONCURRENT_ID =
      Collections.unmodifiableSet(
          EnumSet.of(
              Collector.Characteristics.CONCURRENT,
              Collector.Characteristics.UNORDERED,
              Collector.Characteristics.IDENTITY_FINISH));
  static final Set<Collector.Characteristics> CH_CONCURRENT_NOID =
      Collections.unmodifiableSet(
          EnumSet.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED));
  static final Set<Collector.Characteristics> CH_ID =
      Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
  static final Set<Collector.Characteristics> CH_UNORDERED_ID =
      Collections.unmodifiableSet(
          EnumSet.of(
              Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
  static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();
  private static final String NON_NULL_MSG = "element cannot be mapped to a null key";

  private Collective() {}

  /**
   * toArray
   *
   * @param arrayFactor arrayFactor
   * @param <T> item type
   * @return array
   */
  public static <T> Collector<T, ?, T[]> toArray(IntFunction<T[]> arrayFactor) {
    return collectingAndThen(toList(), list -> list.toArray(arrayFactor.apply(list.size())));
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code Collection},
   * in encounter order. The {@code Collection} is created by the provided factory.
   *
   * @param <T> the type of the input elements
   * @param <C> the type of the resulting {@code Collection}
   * @param collectionFactory a {@code Supplier} which returns a new, empty {@code Collection} of
   *     the appropriate type
   * @return a {@code Collector} which collects all the input elements into a {@code Collection}, in
   *     encounter order
   */
  public static <T, C extends Collection<T>> Collector<T, ?, C> toCollection(
      Supplier<C> collectionFactory) {
    return new Collective.CollectorImpl<>(
        collectionFactory,
        Collection::add,
        (r1, r2) -> {
          r1.addAll(r2);
          return r1;
        },
        CH_ID);
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code List}. There
   * are no guarantees on the type, mutability, serializability, or thread-safety of the {@code
   * List} returned; if more control over the returned {@code List} is required, use {@link
   * #toCollection(Supplier)}.
   *
   * @param <T> the type of the input elements
   * @return a {@code Collector} which collects all the input elements into a {@code List}, in
   *     encounter order
   */
  public static <T> Collector<T, ?, List<T>> toList() {
    return new Collective.CollectorImpl<>(
        (Supplier<List<T>>) ArrayList::new,
        List::add,
        (left, right) -> {
          left.addAll(right);
          return left;
        },
        CH_ID);
  }

  /**
   * Constructs a collector that retrieves a sublist from the input elements.
   *
   * @param limit The maximum length of the returned list. If null, returns all remaining elements.
   * @param <T> The type of the input elements.
   * @return a {@code Collector} that collects all input elements into a {@code List}, representing
   *     a subset of the original collection.
   */
  public static <T> Collector<T, ?, List<T>> limiting(int limit) {
    return subList(null, limit);
  }

  /**
   * Constructs a collector that retrieves a sublist from the input elements.
   *
   * @param skip The number of elements to skip, ignore the first few elements of the collection. If
   *     null, no elements are skipped.
   * @param <T> The type of the input elements.
   * @return a {@code Collector} that collects all input elements into a {@code List}, representing
   *     a subset of the original collection.
   */
  public static <T> Collector<T, ?, List<T>> skipping(int skip) {
    return subList(skip, null);
  }

  /**
   * Constructs a collector that retrieves a sublist from the input elements.
   *
   * @param skip The number of elements to skip, ignore the first few elements of the collection. If
   *     null, no elements are skipped.
   * @param limit The maximum length of the returned list. If null, returns all remaining elements.
   * @param <T> The type of the input elements.
   * @return a {@code Collector} that collects all input elements into a {@code List}, representing
   *     a subset of the original collection.
   */
  public static <T> Collector<T, ?, List<T>> subList(Integer skip, Integer limit) {
    Integer offset = Opp.of(skip).orElse(0);
    return mapping(
        Function.identity(),
        collectingAndThen(
            toList(),
            list -> {
              int size = list.size();
              int fromIndex = Math.min(size, offset);
              int toIndex = Opp.of(limit).map(l -> Math.min(fromIndex + l, size)).orElse(size);
              return list.subList(fromIndex, toIndex);
            }));
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code Set}. There
   * are no guarantees on the type, mutability, serializability, or thread-safety of the {@code Set}
   * returned; if more control over the returned {@code Set} is required, use {@link
   * #toCollection(Supplier)}.
   *
   * <p>This is an {@code Collector.Characteristics#UNORDERED} unordered Collector.
   *
   * @param <T> the type of the input elements
   * @return a {@code Collector} which collects all the input elements into a {@code Set}
   */
  public static <T> Collector<T, ?, Set<T>> toSet() {
    return new Collective.CollectorImpl<>(
        (Supplier<Set<T>>) HashSet::new,
        Set::add,
        (left, right) -> {
          left.addAll(right);
          return left;
        },
        CH_UNORDERED_ID);
  }

  /**
   * Returns a {@code Collector} that concatenates the input elements into a {@code String}, in
   * encounter order.
   *
   * @return a {@code Collector} that concatenates the input elements into a {@code String}, in
   *     encounter order
   */
  public static Collector<CharSequence, ?, String> joining() {
    return new Collective.CollectorImpl<>(
        StringBuilder::new,
        StringBuilder::append,
        (r1, r2) -> {
          r1.append(r2);
          return r1;
        },
        StringBuilder::toString,
        CH_NOID);
  }

  /**
   * Returns a {@code Collector} that concatenates the input elements, separated by the specified
   * delimiter, in encounter order.
   *
   * @param delimiter the delimiter to be used between each element
   * @return A {@code Collector} which concatenates CharSequence elements, separated by the
   *     specified delimiter, in encounter order
   */
  public static Collector<CharSequence, ?, String> joining(CharSequence delimiter) {
    return joining(delimiter, "", "");
  }

  /**
   * Returns a {@code Collector} that concatenates the input elements, separated by the specified
   * delimiter, with the specified prefix and suffix, in encounter order.
   *
   * @param delimiter the delimiter to be used between each element
   * @param prefix the sequence of characters to be used at the beginning of the joined result
   * @param suffix the sequence of characters to be used at the end of the joined result
   * @return A {@code Collector} which concatenates CharSequence elements, separated by the
   *     specified delimiter, in encounter order
   */
  public static Collector<CharSequence, ?, String> joining(
      CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
    return new Collective.CollectorImpl<>(
        () -> new StringJoiner(delimiter, prefix, suffix),
        StringJoiner::add,
        StringJoiner::merge,
        StringJoiner::toString,
        CH_NOID);
  }

  /**
   * {@code BinaryOperator<Map>} that merges the contents of its right argument into its left
   * argument, using the provided merge function to handle duplicate keys.
   *
   * @param <K> type of the map keys
   * @param <V> type of the map values
   * @param <M> type of the map
   * @param mergeFunction A merge function suitable for {@link Map#merge(Object, Object, BiFunction)
   *     Map.merge()}
   * @return a merge function for two maps
   */
  private static <K, V, M extends Map<K, V>> BinaryOperator<M> mapMerger(
      BinaryOperator<V> mergeFunction) {
    return (m1, m2) -> {
      for (Map.Entry<K, V> e : m2.entrySet()) {
        m1.merge(e.getKey(), e.getValue(), mergeFunction);
      }
      return m1;
    };
  }

  /**
   * Adapts a {@code Collector} accepting elements of type {@code U} to one accepting elements of
   * type {@code T} by applying a mapping function to each input element before accumulation.
   *
   * @param <T> the type of the input elements
   * @param <U> type of elements accepted by downstream collector
   * @param <A> intermediate accumulation type of the downstream collector
   * @param <R> result type of collector
   * @param mapper a function to be applied to the input elements
   * @param downstream a collector which will accept mapped values
   * @return a collector which applies the mapping function to the input elements and provides the
   *     mapped results to the downstream collector
   * @apiNote The {@code mapping()} collectors are most useful when used in a multi-level reduction,
   *     such as downstream of a {@code groupingBy} or {@code partitioningBy}. For example, given a
   *     stream of {@code Person}, to accumulate the set of last names in each city:
   *     <pre>{@code
   * Map<City, Set<String>> lastNamesByCity
   *     = people.stream().collect(groupingBy(Person::getCity,
   *                                          mapping(Person::getLastName, toSet())));
   * }</pre>
   */
  public static <T, U, A, R> Collector<T, ?, R> mapping(
      Function<? super T, ? extends U> mapper, Collector<? super U, A, R> downstream) {
    BiConsumer<A, ? super U> downstreamAccumulator = downstream.accumulator();
    return new Collective.CollectorImpl<>(
        downstream.supplier(),
        (r, t) -> downstreamAccumulator.accept(r, Opp.of(t).map(mapper).get()),
        downstream.combiner(),
        downstream.finisher(),
        downstream.characteristics());
  }

  /**
   * flatMapping.
   *
   * @param mapper a {@link java.util.function.Function} object
   * @param downstream a {@link java.util.stream.Collector} object
   * @param <T> a T class
   * @param <U> a U class
   * @param <A> a A class
   * @param <R> a R class
   * @return a {@link java.util.stream.Collector} object
   */
  public static <T, U, A, R> Collector<T, ?, R> flatMapping(
      Function<? super T, Stream<? extends U>> mapper, Collector<? super U, A, R> downstream) {
    BiConsumer<A, ? super U> downstreamAccumulator = downstream.accumulator();
    return new Collective.CollectorImpl<>(
        downstream.supplier(),
        (r, t) ->
            Opp.of(t)
                .map(mapper)
                .ifPresent(s -> s.sequential().forEach(v -> downstreamAccumulator.accept(r, v))),
        downstream.combiner(),
        downstream.finisher(),
        downstream.characteristics());
  }

  /**
   * filtering.
   *
   * @param predicate a {@link java.util.function.Predicate} object
   * @param downstream a {@link java.util.stream.Collector} object
   * @param <T> a T class
   * @param <A> a A class
   * @param <R> a R class
   * @return a {@link java.util.stream.Collector} object
   */
  public static <T, A, R> Collector<T, ?, R> filtering(
      SerPred<? super T> predicate, Collector<? super T, A, R> downstream) {
    BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
    return new Collective.CollectorImpl<>(
        downstream.supplier(),
        (r, t) -> Opp.of(t).filter(predicate).ifPresent(e -> downstreamAccumulator.accept(r, e)),
        downstream.combiner(),
        downstream.finisher(),
        downstream.characteristics());
  }

  /**
   * flatMappingIter.
   *
   * @param mapper a {@link java.util.function.Function} object
   * @param downstream a {@link java.util.stream.Collector} object
   * @param <T> a T class
   * @param <U> a U class
   * @param <A> a A class
   * @param <R> a R class
   * @return a {@link java.util.stream.Collector} object
   */
  public static <T, U, A, R> Collector<T, ?, R> flatMappingIter(
      Function<? super T, Iterable<? extends U>> mapper, Collector<? super U, A, R> downstream) {
    return flatMapping(t -> Opp.of(t).map(mapper).map(Steam::of).get(), downstream);
  }

  /**
   * Adapts a {@code Collector} to perform an additional finishing transformation. For example, one
   * could adapt the {@link #toList()} collector to always produce an immutable list with:
   *
   * <pre>{@code
   * List<String> people
   *     = people.stream().collect(collectingAndThen(toList(), Collections::unmodifiableList));
   * }</pre>
   *
   * @param <T> the type of the input elements
   * @param <A> intermediate accumulation type of the downstream collector
   * @param <R> result type of the downstream collector
   * @param <RR> result type of the resulting collector
   * @param downstream a collector
   * @param finisher a function to be applied to the final result of the downstream collector
   * @return a collector which performs the action of the downstream collector, followed by an
   *     additional finishing step
   */
  public static <T, A, R, RR> Collector<T, A, RR> collectingAndThen(
      Collector<T, A, R> downstream, Function<R, RR> finisher) {
    Set<Collector.Characteristics> characteristics = downstream.characteristics();
    if (characteristics.contains(Collector.Characteristics.IDENTITY_FINISH)) {
      if (characteristics.size() != 1) {
        characteristics = EnumSet.copyOf(characteristics);
        characteristics.remove(Collector.Characteristics.IDENTITY_FINISH);
        characteristics = Collections.unmodifiableSet(characteristics);
      } else {
        characteristics = Collective.CH_NOID;
      }
    }
    return new Collective.CollectorImpl<>(
        downstream.supplier(),
        downstream.accumulator(),
        downstream.combiner(),
        downstream.finisher().andThen(finisher),
        characteristics);
  }

  /**
   * Returns a {@code Collector} accepting elements of type {@code T} that counts the number of
   * input elements. If no elements are present, the result is 0.
   *
   * @param <T> the type of the input elements
   * @return a {@code Collector} that counts the input elements
   * @implSpec This produces a result equivalent to:
   *     <pre>{@code
   * reducing(0L, e -> 1L, Long::sum)
   * }</pre>
   */
  public static <T> Collector<T, ?, Long> counting() {
    return reducing(0L, e -> 1L, Long::sum);
  }

  /**
   * Returns a {@code Collector} that produces the minimal element according to a given {@code
   * Comparator}, described as an {@code Optional<T>}.
   *
   * @param <T> the type of the input elements
   * @param comparator a {@code Comparator} for comparing elements
   * @return a {@code Collector} that produces the minimal value
   * @implSpec This produces a result equivalent to:
   *     <pre>{@code
   * reducing(BinaryOperator.minBy(comparator))
   * }</pre>
   */
  public static <T> Collector<T, ?, Optional<T>> minBy(Comparator<? super T> comparator) {
    return reducing(BinaryOperator.minBy(comparator));
  }

  /**
   * Returns a {@code Collector} that produces the maximal element according to a given {@code
   * Comparator}, described as an {@code Optional<T>}.
   *
   * @param <T> the type of the input elements
   * @param comparator a {@code Comparator} for comparing elements
   * @return a {@code Collector} that produces the maximal value
   * @implSpec This produces a result equivalent to:
   *     <pre>{@code
   * reducing(BinaryOperator.maxBy(comparator))
   * }</pre>
   */
  public static <T> Collector<T, ?, Optional<T>> maxBy(Comparator<? super T> comparator) {
    return reducing(BinaryOperator.maxBy(comparator));
  }

  /**
   * Returns a {@code Collector} that produces the sum of a integer-valued function applied to the
   * input elements. If no elements are present, the result is 0.
   *
   * @param <T> the type of the input elements
   * @param mapper a function extracting the property to be summed
   * @return a {@code Collector} that produces the sum of a derived property
   */
  public static <T> Collector<T, ?, Integer> summingInt(ToIntFunction<? super T> mapper) {
    return new Collective.CollectorImpl<>(
        () -> new int[1],
        (a, t) -> a[0] += mapper.applyAsInt(t),
        (a, b) -> {
          a[0] += b[0];
          return a;
        },
        a -> a[0],
        CH_NOID);
  }

  /**
   * Returns a {@code Collector} that produces the sum of a long-valued function applied to the
   * input elements. If no elements are present, the result is 0.
   *
   * @param <T> the type of the input elements
   * @param mapper a function extracting the property to be summed
   * @return a {@code Collector} that produces the sum of a derived property
   */
  public static <T> Collector<T, ?, Long> summingLong(ToLongFunction<? super T> mapper) {
    return new Collective.CollectorImpl<>(
        () -> new long[1],
        (a, t) -> a[0] += mapper.applyAsLong(t),
        (a, b) -> {
          a[0] += b[0];
          return a;
        },
        a -> a[0],
        CH_NOID);
  }

  /**
   * Returns a {@code Collector} that produces the sum of a double-valued function applied to the
   * input elements. If no elements are present, the result is 0.
   *
   * <p>The sum returned can vary depending upon the order in which values are recorded, due to
   * accumulated rounding error in addition of values of differing magnitudes. Values sorted by
   * increasing absolute magnitude tend to yield more accurate results. If any recorded value is a
   * {@code NaN} or the sum is at any point a {@code NaN} then the sum will be {@code NaN}.
   *
   * @param <T> the type of the input elements
   * @param mapper a function extracting the property to be summed
   * @return a {@code Collector} that produces the sum of a derived property
   */
  public static <T> Collector<T, ?, Double> summingDouble(ToDoubleFunction<? super T> mapper) {
    /*
     * In the arrays allocated for the collect operation, index 0
     * holds the high-order bits of the running sum, index 1 holds
     * the low-order bits of the sum computed via compensated
     * summation, and index 2 holds the simple sum used to compute
     * the proper result if the stream contains infinite values of
     * the same sign.
     */
    return new Collective.CollectorImpl<>(
        () -> new double[3],
        (a, t) -> {
          sumWithCompensation(a, mapper.applyAsDouble(t));
          a[2] += mapper.applyAsDouble(t);
        },
        (a, b) -> {
          sumWithCompensation(a, b[0]);
          a[2] += b[2];
          return sumWithCompensation(a, b[1]);
        },
        Collective::computeFinalSum,
        CH_NOID);
  }

  /**
   * Incorporate a new double value using Kahan summation / compensation summation.
   *
   * <p>High-order bits of the sum are in intermediateSum[0], low-order bits of the sum are in
   * intermediateSum[1], any additional elements are application-specific.
   *
   * @param intermediateSum the high-order and low-order words of the intermediate sum
   * @param value the name value to be included in the running sum
   */
  static double[] sumWithCompensation(double[] intermediateSum, double value) {
    double tmp = value - intermediateSum[1];
    double sum = intermediateSum[0];
    // Little wolf of rounding error
    double velvel = sum + tmp;
    intermediateSum[1] = (velvel - sum) - tmp;
    intermediateSum[0] = velvel;
    return intermediateSum;
  }

  /**
   * If the compensated sum is spuriously NaN from accumulating one or more same-signed infinite
   * values, return the correctly-signed infinity stored in the simple sum.
   */
  static double computeFinalSum(double[] summands) {
    // Better error bounds to add both terms as the final sum
    double tmp = summands[0] + summands[1];
    double simpleSum = summands[summands.length - 1];
    if (Double.isNaN(tmp) && Double.isInfinite(simpleSum)) {
      return simpleSum;
    } else {
      return tmp;
    }
  }

  /**
   * Returns a {@code Collector} that produces the arithmetic mean of an integer-valued function
   * applied to the input elements. If no elements are present, the result is 0.
   *
   * @param <T> the type of the input elements
   * @param mapper a function extracting the property to be summed
   * @return a {@code Collector} that produces the sum of a derived property
   */
  public static <T> Collector<T, ?, Double> averagingInt(ToIntFunction<? super T> mapper) {
    return new Collective.CollectorImpl<>(
        () -> new long[2],
        (a, t) -> {
          a[0] += mapper.applyAsInt(t);
          a[1]++;
        },
        (a, b) -> {
          a[0] += b[0];
          a[1] += b[1];
          return a;
        },
        a -> (a[1] == 0) ? 0.0d : (double) a[0] / a[1],
        CH_NOID);
  }

  /**
   * Returns a {@code Collector} that produces the arithmetic mean of a long-valued function applied
   * to the input elements. If no elements are present, the result is 0.
   *
   * @param <T> the type of the input elements
   * @param mapper a function extracting the property to be summed
   * @return a {@code Collector} that produces the sum of a derived property
   */
  public static <T> Collector<T, ?, Double> averagingLong(ToLongFunction<? super T> mapper) {
    return new Collective.CollectorImpl<>(
        () -> new long[2],
        (a, t) -> {
          a[0] += mapper.applyAsLong(t);
          a[1]++;
        },
        (a, b) -> {
          a[0] += b[0];
          a[1] += b[1];
          return a;
        },
        a -> (a[1] == 0) ? 0.0d : (double) a[0] / a[1],
        CH_NOID);
  }

  /**
   * Returns a {@code Collector} that produces the arithmetic mean of a double-valued function
   * applied to the input elements. If no elements are present, the result is 0.
   *
   * <p>The average returned can vary depending upon the order in which values are recorded, due to
   * accumulated rounding error in addition of values of differing magnitudes. Values sorted by
   * increasing absolute magnitude tend to yield more accurate results. If any recorded value is a
   * {@code NaN} or the sum is at any point a {@code NaN} then the average will be {@code NaN}.
   *
   * @param <T> the type of the input elements
   * @param mapper a function extracting the property to be summed
   * @return a {@code Collector} that produces the sum of a derived property
   * @implNote The {@code double} format can represent all consecutive integers in the range
   *     -2<sup>53</sup> to 2<sup>53</sup>. If the pipeline has more than 2<sup>53</sup> values, the
   *     divisor in the average computation will saturate at 2<sup>53</sup>, leading to additional
   *     numerical errors.
   */
  public static <T> Collector<T, ?, Double> averagingDouble(ToDoubleFunction<? super T> mapper) {
    /*
     * In the arrays allocated for the collect operation, index 0
     * holds the high-order bits of the running sum, index 1 holds
     * the low-order bits of the sum computed via compensated
     * summation, and index 2 holds the number of values seen.
     */
    return new Collective.CollectorImpl<>(
        () -> new double[4],
        (a, t) -> {
          sumWithCompensation(a, mapper.applyAsDouble(t));
          a[2]++;
          a[3] += mapper.applyAsDouble(t);
        },
        (a, b) -> {
          sumWithCompensation(a, b[0]);
          sumWithCompensation(a, b[1]);
          a[2] += b[2];
          a[3] += b[3];
          return a;
        },
        a -> (a[2] == 0) ? 0.0d : (computeFinalSum(a) / a[2]),
        CH_NOID);
  }

  /**
   * Returns a {@code Collector} which performs a reduction of its input elements under a specified
   * {@code BinaryOperator} using the provided identity.
   *
   * @param <T> element type for the input and output of the reduction
   * @param identity the identity value for the reduction (also, the value that is returned when
   *     there are no input elements)
   * @param op a {@code BinaryOperator<T>} used to reduce the input elements
   * @return a {@code Collector} which implements the reduction operation
   * @apiNote The {@code reducing()} collectors are most useful when used in a multi-level
   *     reduction, downstream of {@code groupingBy} or {@code partition}. To perform a simple
   *     reduction on a stream, use {@link java.util.stream.Stream#reduce(Object, BinaryOperator)}}
   *     instead.
   * @see #reducing(BinaryOperator)
   * @see #reducing(Object, Function, BinaryOperator)
   */
  public static <T> Collector<T, ?, T> reducing(T identity, BinaryOperator<T> op) {
    return new Collective.CollectorImpl<>(
        boxSupplier(identity),
        (a, t) -> a[0] = op.apply(a[0], t),
        (a, b) -> {
          a[0] = op.apply(a[0], b[0]);
          return a;
        },
        a -> a[0],
        CH_NOID);
  }

  @SuppressWarnings("unchecked")
  private static <T> Supplier<T[]> boxSupplier(T identity) {
    return () -> (T[]) new Object[] {identity};
  }

  /**
   * Returns a {@code Collector} which performs a reduction of its input elements under a specified
   * {@code BinaryOperator}. The result is described as an {@code Optional<T>}.
   *
   * @param <T> element type for the input and output of the reduction
   * @param op a {@code BinaryOperator<T>} used to reduce the input elements
   * @return a {@code Collector} which implements the reduction operation
   * @apiNote The {@code reducing()} collectors are most useful when used in a multi-level
   *     reduction, downstream of {@code groupingBy} or {@code partition}. To perform a simple
   *     reduction on a stream, use {@link java.util.stream.Stream#reduce(BinaryOperator)} instead.
   *     <p>For example, given a stream of {@code Person}, to calculate tallest person in each city:
   *     <pre>{@code
   * Comparator<Person> byHeight = Comparator.comparing(Person::getHeight);
   * Map<City, Person> tallestByCity
   *     = people.stream().collect(groupingBy(Person::getCity, reducing(BinaryOperator.maxBy(byHeight))));
   * }</pre>
   *
   * @see #reducing(Object, BinaryOperator)
   * @see #reducing(Object, Function, BinaryOperator)
   */
  public static <T> Collector<T, ?, Optional<T>> reducing(BinaryOperator<T> op) {
    class OptionalBox implements Consumer<T> {
      T value = null;
      boolean present = false;

      @Override
      public void accept(T t) {
        if (present) {
          value = op.apply(value, t);
        } else {
          value = t;
          present = true;
        }
      }
    }

    return new Collective.CollectorImpl<>(
        OptionalBox::new,
        OptionalBox::accept,
        (a, b) -> {
          if (b.present) {
            a.accept(b.value);
          }
          return a;
        },
        a -> Optional.ofNullable(a.value),
        CH_NOID);
  }

  /**
   * Returns a {@code Collector} which performs a reduction of its input elements under a specified
   * mapping function and {@code BinaryOperator}. This is a generalization of {@link
   * #reducing(Object, BinaryOperator)} which allows a transformation of the elements before
   * reduction.
   *
   * @param <T> the type of the input elements
   * @param <U> the type of the mapped values
   * @param identity the identity value for the reduction (also, the value that is returned when
   *     there are no input elements)
   * @param mapper a mapping function to apply to each input value
   * @param op a {@code BinaryOperator<U>} used to reduce the mapped values
   * @return a {@code Collector} implementing the map-reduce operation
   * @apiNote The {@code reducing()} collectors are most useful when used in a multi-level
   *     reduction, downstream of {@code groupingBy} or {@code partition}. To perform a simple
   *     map-reduce on a stream, use {@link java.util.stream.Stream#map(Function)} and {@link
   *     java.util.stream.Stream#reduce(Object, BinaryOperator)} instead.
   *     <p>For example, given a stream of {@code Person}, to calculate the longest last name of
   *     residents in each city:
   *     <pre>{@code
   * Comparator<String> byLength = Comparator.comparing(String::length);
   * Map<City, String> longestLastNameByCity
   *     = people.stream().collect(groupingBy(Person::getCity,
   *                                          reducing(Person::getLastName, BinaryOperator.maxBy(byLength))));
   * }</pre>
   *
   * @see #reducing(Object, BinaryOperator)
   * @see #reducing(BinaryOperator)
   */
  public static <T, U> Collector<T, ?, U> reducing(
      U identity, Function<? super T, ? extends U> mapper, BinaryOperator<U> op) {
    return new Collective.CollectorImpl<>(
        boxSupplier(identity),
        (a, t) -> a[0] = op.apply(a[0], mapper.apply(t)),
        (a, b) -> {
          a[0] = op.apply(a[0], b[0]);
          return a;
        },
        a -> a[0],
        CH_NOID);
  }

  /**
   * Returns a {@code Collector} implementing a "group by" operation on input elements of type
   * {@code T}, grouping elements according to a classification function, and returning the results
   * in a {@code Map}.
   *
   * <p>The classification function maps elements to some key type {@code K}. The collector produces
   * a {@code Map<K, List<T>>} whose keys are the values resulting from applying the classification
   * function to the input elements, and whose corresponding values are {@code List}s containing the
   * input elements which map to the associated key under the classification function.
   *
   * <p>There are no guarantees on the type, mutability, serializability, or thread-safety of the
   * {@code Map} or {@code List} objects returned.
   *
   * @param <T> the type of the input elements
   * @param <K> the type of the keys
   * @param classifier the classifier function mapping input elements to keys
   * @return a {@code Collector} implementing the group-by operation
   * @implSpec This produces a result similar to:
   *     <pre>{@code
   * groupingBy(classifier, toList());
   * }</pre>
   *
   * @implNote The returned {@code Collector} is not concurrent. For parallel stream pipelines, the
   *     {@code combiner} function operates by merging the keys from one map into another, which can
   *     be an expensive operation. If preservation of the order in which elements appear in the
   *     resulting {@code Map} collector is not required, using {@link
   *     #groupingByConcurrent(Function)} may offer better parallel performance.
   * @see #groupingBy(Function, Collector)
   * @see #groupingBy(Function, Supplier, Collector)
   * @see #groupingByConcurrent(Function)
   */
  public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(
      Function<? super T, ? extends K> classifier) {
    return groupingBy(classifier, toList());
  }

  /**
   * Returns a {@code Collector} implementing a cascaded "group by" operation on input elements of
   * type {@code T}, grouping elements according to a classification function, and then performing a
   * reduction operation on the values associated with a given key using the specified downstream
   * {@code Collector}.
   *
   * <p>The classification function maps elements to some key type {@code K}. The downstream
   * collector operates on elements of type {@code T} and produces a result of type {@code D}. The
   * resulting collector produces a {@code Map<K, D>}.
   *
   * <p>There are no guarantees on the type, mutability, serializability, or thread-safety of the
   * {@code Map} returned.
   *
   * <p>For example, to compute the set of last names of people in each city:
   *
   * <pre>{@code
   * Map<City, Set<String>> namesByCity
   *     = people.stream().collect(groupingBy(Person::getCity,
   *                                          mapping(Person::getLastName, toSet())));
   * }</pre>
   *
   * @param <T> the type of the input elements
   * @param <K> the type of the keys
   * @param <A> the intermediate accumulation type of the downstream collector
   * @param <D> the result type of the downstream reduction
   * @param classifier a classifier function mapping input elements to keys
   * @param downstream a {@code Collector} implementing the downstream reduction
   * @return a {@code Collector} implementing the cascaded group-by operation
   * @implNote The returned {@code Collector} is not concurrent. For parallel stream pipelines, the
   *     {@code combiner} function operates by merging the keys from one map into another, which can
   *     be an expensive operation. If preservation of the order in which elements are presented to
   *     the downstream collector is not required, using {@link #groupingByConcurrent(Function,
   *     Collector)} may offer better parallel performance.
   * @see #groupingBy(Function)
   * @see #groupingBy(Function, Supplier, Collector)
   * @see #groupingByConcurrent(Function, Collector)
   */
  public static <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(
      Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
    return groupingBy(classifier, HashMap::new, downstream);
  }

  /**
   * Returns a {@code Collector} implementing a cascaded "group by" operation on input elements of
   * type {@code T}, grouping elements according to a classification function, and then performing a
   * reduction operation on the values associated with a given key using the specified downstream
   * {@code Collector}. The {@code Map} produced by the Collector is created with the supplied
   * factory function.
   *
   * <p>The classification function maps elements to some key type {@code K}. The downstream
   * collector operates on elements of type {@code T} and produces a result of type {@code D}. The
   * resulting collector produces a {@code Map<K, D>}.
   *
   * <p>For example, to compute the set of last names of people in each city, where the city names
   * are sorted:
   *
   * <pre>{@code
   * Map<City, Set<String>> namesByCity
   *     = people.stream().collect(groupingBy(Person::getCity, TreeMap::new,
   *                                          mapping(Person::getLastName, toSet())));
   * }</pre>
   *
   * @param <T> the type of the input elements
   * @param <K> the type of the keys
   * @param <A> the intermediate accumulation type of the downstream collector
   * @param <D> the result type of the downstream reduction
   * @param <M> the type of the resulting {@code Map}
   * @param classifier a classifier function mapping input elements to keys
   * @param downstream a {@code Collector} implementing the downstream reduction
   * @param mapFactory a function which, when called, produces a new empty {@code Map} of the
   *     desired type
   * @return a {@code Collector} implementing the cascaded group-by operation
   * @implNote The returned {@code Collector} is not concurrent. For parallel stream pipelines, the
   *     {@code combiner} function operates by merging the keys from one map into another, which can
   *     be an expensive operation. If preservation of the order in which elements are presented to
   *     the downstream collector is not required, using {@link #groupingByConcurrent(Function,
   *     Supplier, Collector)} may offer better parallel performance.
   * @see #groupingBy(Function, Collector)
   * @see #groupingBy(Function)
   * @see #groupingByConcurrent(Function, Supplier, Collector)
   */
  public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> groupingBy(
      Function<? super T, ? extends K> classifier,
      Supplier<? super M> mapFactory,
      Collector<? super T, A, D> downstream) {
    Supplier<A> downstreamSupplier = downstream.supplier();
    BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
    BiConsumer<Map<K, A>, T> accumulator =
        (m, t) -> {
          // stream-core changed this line
          K key = Opp.of(t).map(classifier).orElse(null);
          A container = Maps.computeIfAbsent(m, key, k -> downstreamSupplier.get());
          Opp.of(t).ifPresent(o -> downstreamAccumulator.accept(container, o));
        };
    BinaryOperator<Map<K, A>> merger = Collective.mapMerger(downstream.combiner());
    @SuppressWarnings("unchecked")
    Supplier<Map<K, A>> mangledFactory = (Supplier<Map<K, A>>) mapFactory;

    if (downstream.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
      return new Collective.CollectorImpl<>(mangledFactory, accumulator, merger, CH_ID);
    } else {
      SerUnOp<A> downstreamFinisher = SerUnOp.casting(downstream.finisher());
      Function<Map<K, A>, M> finisher =
          intermediate -> {
            intermediate.replaceAll((k, v) -> downstreamFinisher.apply(v));
            return cast(intermediate);
          };
      return new Collective.CollectorImpl<>(mangledFactory, accumulator, merger, finisher, CH_NOID);
    }
  }

  /**
   * Returns a concurrent {@code Collector} implementing a "group by" operation on input elements of
   * type {@code T}, grouping elements according to a classification function.
   *
   * <p>This is a {@code Collector.Characteristics#CONCURRENT} concurrent and {@code
   * Collector.Characteristics#UNORDERED} unordered Collector.
   *
   * <p>The classification function maps elements to some key type {@code K}. The collector produces
   * a {@code ConcurrentMap<K, List<T>>} whose keys are the values resulting from applying the
   * classification function to the input elements, and whose corresponding values are {@code List}s
   * containing the input elements which map to the associated key under the classification
   * function.
   *
   * <p>There are no guarantees on the type, mutability, or serializability of the {@code Map} or
   * {@code List} objects returned, or of the thread-safety of the {@code List} objects returned.
   *
   * @param <T> the type of the input elements
   * @param <K> the type of the keys
   * @param classifier a classifier function mapping input elements to keys
   * @return a concurrent, unordered {@code Collector} implementing the group-by operation
   * @implSpec This produces a result similar to:
   *     <pre>{@code
   * groupingByConcurrent(classifier, toList());
   * }</pre>
   *
   * @see #groupingBy(Function)
   * @see #groupingByConcurrent(Function, Collector)
   * @see #groupingByConcurrent(Function, Supplier, Collector)
   */
  public static <T, K> Collector<T, ?, ConcurrentMap<K, List<T>>> groupingByConcurrent(
      Function<? super T, ? extends K> classifier) {
    return groupingByConcurrent(classifier, ConcurrentHashMap::new, toList());
  }

  /**
   * Returns a concurrent {@code Collector} implementing a cascaded "group by" operation on input
   * elements of type {@code T}, grouping elements according to a classification function, and then
   * performing a reduction operation on the values associated with a given key using the specified
   * downstream {@code Collector}.
   *
   * <p>This is a {@code Collector.Characteristics#CONCURRENT} concurrent and {@code
   * Collector.Characteristics#UNORDERED} unordered Collector.
   *
   * <p>The classification function maps elements to some key type {@code K}. The downstream
   * collector operates on elements of type {@code T} and produces a result of type {@code D}. The
   * resulting collector produces a {@code Map<K, D>}.
   *
   * <p>For example, to compute the set of last names of people in each city, where the city names
   * are sorted:
   *
   * <pre>{@code
   * ConcurrentMap<City, Set<String>> namesByCity
   *     = people.stream().collect(groupingByConcurrent(Person::getCity,
   *                                                    mapping(Person::getLastName, toSet())));
   * }</pre>
   *
   * @param <T> the type of the input elements
   * @param <K> the type of the keys
   * @param <A> the intermediate accumulation type of the downstream collector
   * @param <D> the result type of the downstream reduction
   * @param classifier a classifier function mapping input elements to keys
   * @param downstream a {@code Collector} implementing the downstream reduction
   * @return a concurrent, unordered {@code Collector} implementing the cascaded group-by operation
   * @see #groupingBy(Function, Collector)
   * @see #groupingByConcurrent(Function)
   * @see #groupingByConcurrent(Function, Supplier, Collector)
   */
  public static <T, K, A, D> Collector<T, ?, ConcurrentMap<K, D>> groupingByConcurrent(
      Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
    return groupingByConcurrent(classifier, ConcurrentHashMap::new, downstream);
  }

  /**
   * Returns a concurrent {@code Collector} implementing a cascaded "group by" operation on input
   * elements of type {@code T}, grouping elements according to a classification function, and then
   * performing a reduction operation on the values associated with a given key using the specified
   * downstream {@code Collector}. The {@code ConcurrentMap} produced by the Collector is created
   * with the supplied factory function.
   *
   * <p>This is a {@code Collector.Characteristics#CONCURRENT} concurrent and {@code
   * Collector.Characteristics#UNORDERED} unordered Collector.
   *
   * <p>The classification function maps elements to some key type {@code K}. The downstream
   * collector operates on elements of type {@code T} and produces a result of type {@code D}. The
   * resulting collector produces a {@code Map<K, D>}.
   *
   * <p>For example, to compute the set of last names of people in each city, where the city names
   * are sorted:
   *
   * <pre>{@code
   * ConcurrentMap<City, Set<String>> namesByCity
   *     = people.stream().collect(groupingBy(Person::getCity, ConcurrentSkipListMap::new,
   *                                          mapping(Person::getLastName, toSet())));
   * }</pre>
   *
   * @param <T> the type of the input elements
   * @param <K> the type of the keys
   * @param <A> the intermediate accumulation type of the downstream collector
   * @param <D> the result type of the downstream reduction
   * @param <M> the type of the resulting {@code ConcurrentMap}
   * @param classifier a classifier function mapping input elements to keys
   * @param downstream a {@code Collector} implementing the downstream reduction
   * @param mapFactory a function which, when called, produces a new empty {@code ConcurrentMap} of
   *     the desired type
   * @return a concurrent, unordered {@code Collector} implementing the cascaded group-by operation
   * @see #groupingByConcurrent(Function)
   * @see #groupingByConcurrent(Function, Collector)
   * @see #groupingBy(Function, Supplier, Collector)
   */
  public static <T, K, A, D, M extends ConcurrentMap<K, D>> Collector<T, ?, M> groupingByConcurrent(
      Function<? super T, ? extends K> classifier,
      Supplier<M> mapFactory,
      Collector<? super T, A, D> downstream) {
    Supplier<A> downstreamSupplier = downstream.supplier();
    BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
    BinaryOperator<ConcurrentMap<K, A>> merger = Collective.mapMerger(downstream.combiner());
    Supplier<ConcurrentMap<K, A>> mangledFactory = cast(mapFactory);
    BiConsumer<ConcurrentMap<K, A>, T> accumulator;
    if (downstream.characteristics().contains(Collector.Characteristics.CONCURRENT)) {
      accumulator =
          (m, t) -> {
            K key = Objects.requireNonNull(classifier.apply(t), NON_NULL_MSG);
            A resultContainer = Maps.computeIfAbsent(m, key, k -> downstreamSupplier.get());
            downstreamAccumulator.accept(resultContainer, t);
          };
    } else {
      accumulator =
          (m, t) -> {
            K key = Objects.requireNonNull(classifier.apply(t), NON_NULL_MSG);
            A resultContainer = Maps.computeIfAbsent(m, key, k -> downstreamSupplier.get());
            synchronized (resultContainer) {
              downstreamAccumulator.accept(resultContainer, t);
            }
          };
    }

    if (downstream.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
      return new Collective.CollectorImpl<>(mangledFactory, accumulator, merger, CH_CONCURRENT_ID);
    } else {
      SerUnOp<A> downstreamFinisher = SerUnOp.casting(downstream.finisher());
      Function<ConcurrentMap<K, A>, M> finisher =
          intermediate -> {
            intermediate.replaceAll((k, v) -> downstreamFinisher.apply(v));
            return cast(intermediate);
          };
      return new Collective.CollectorImpl<>(
          mangledFactory, accumulator, merger, finisher, CH_CONCURRENT_NOID);
    }
  }

  /**
   * Returns a {@code Collector} which partitions the input elements according to a {@code
   * Predicate}, and organizes them into a {@code Map<Boolean, List<T>>}.
   *
   * <p>There are no guarantees on the type, mutability, serializability, or thread-safety of the
   * {@code Map} returned.
   *
   * @param <T> the type of the input elements
   * @param predicate a predicate used for classifying input elements
   * @return a {@code Collector} implementing the partitioning operation
   * @see #partitioningBy(Predicate, Collector)
   */
  public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(
      Predicate<? super T> predicate) {
    return partitioningBy(predicate, toList());
  }

  /**
   * Returns a {@code Collector} which partitions the input elements according to a {@code
   * Predicate}, reduces the values in each partition according to another {@code Collector}, and
   * organizes them into a {@code Map<Boolean, D>} whose values are the result of the downstream
   * reduction.
   *
   * <p>There are no guarantees on the type, mutability, serializability, or thread-safety of the
   * {@code Map} returned.
   *
   * @param <T> the type of the input elements
   * @param <A> the intermediate accumulation type of the downstream collector
   * @param <D> the result type of the downstream reduction
   * @param predicate a predicate used for classifying input elements
   * @param downstream a {@code Collector} implementing the downstream reduction
   * @return a {@code Collector} implementing the cascaded partitioning operation
   * @see #partitioningBy(Predicate)
   */
  public static <T, D, A> Collector<T, ?, Map<Boolean, D>> partitioningBy(
      Predicate<? super T> predicate, Collector<? super T, A, D> downstream) {
    BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
    BiConsumer<Collective.Partition<A>, T> accumulator =
        (result, t) ->
            downstreamAccumulator.accept(predicate.test(t) ? result.forTrue : result.forFalse, t);
    BinaryOperator<A> op = downstream.combiner();
    BinaryOperator<Collective.Partition<A>> merger =
        (left, right) ->
            new Collective.Partition<>(
                op.apply(left.forTrue, right.forTrue), op.apply(left.forFalse, right.forFalse));
    Supplier<Collective.Partition<A>> supplier =
        () -> new Collective.Partition<>(downstream.supplier().get(), downstream.supplier().get());
    if (downstream.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
      return new Collective.CollectorImpl<>(supplier, accumulator, merger, CH_ID);
    } else {
      Function<Collective.Partition<A>, Map<Boolean, D>> finisher =
          par ->
              new Collective.Partition<>(
                  downstream.finisher().apply(par.forTrue),
                  downstream.finisher().apply(par.forFalse));
      return new Collective.CollectorImpl<>(supplier, accumulator, merger, finisher, CH_NOID);
    }
  }

  /**
   * Returns a {@code Collector} that accumulates elements into a {@code Map} whose keys and values
   * are the result of applying the provided mapping functions to the input elements.
   *
   * <p>If the mapped keys contains duplicates (according to {@link
   * java.lang.Object#equals(Object)}), an {@code IllegalStateException} is thrown when the
   * collection operation is performed. If the mapped keys may have duplicates, use {@link
   * #toMap(Function, Function, BinaryOperator)} instead.
   *
   * @param <T> the type of the input elements
   * @param <K> the output type of the key mapping function
   * @param <U> the output type of the value mapping function
   * @param keyMapper a mapping function to produce keys
   * @param valueMapper a mapping function to produce values
   * @return a {@code Collector} which collects elements into a {@code Map} whose keys and values
   *     are the result of applying mapping functions to the input elements
   * @apiNote It is common for either the key or the value to be the input elements. In this case,
   *     the utility method {@link java.util.function.Function#identity()} may be helpful. For
   *     example, the following produces a {@code Map} mapping students to their grade point
   *     average:
   *     <pre>{@code
   * Map<Student, Double> studentToGPA
   *     students.stream().collect(toMap(Functions.identity(),
   *                                     student -> computeGPA(student)));
   * }</pre>
   *     And the following produces a {@code Map} mapping a unique identifier to students:
   *     <pre>{@code
   * Map<String, Student> studentIdToStudent
   *     students.stream().collect(toMap(Student::getId,
   *                                     Functions.identity());
   * }</pre>
   *
   * @implNote The returned {@code Collector} is not concurrent. For parallel stream pipelines, the
   *     {@code combiner} function operates by merging the keys from one map into another, which can
   *     be an expensive operation. If it is not required that results are inserted into the {@code
   *     Map} in encounter order, using {@link #toConcurrentMap(Function, Function)} may offer
   *     better parallel performance.
   * @see #toMap(Function, Function, BinaryOperator)
   * @see #toMap(Function, Function, BinaryOperator, Supplier)
   * @see #toConcurrentMap(Function, Function)
   */
  public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(
      Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
    return toMap(keyMapper, valueMapper, SerBiOp.justAfter(), HashMap::new);
  }

  /**
   * Returns a {@code Collector} that accumulates elements into a {@code Map}
   *
   * @param keyMapper a mapping function to produce keys
   * @param <T> the type of the input elements
   * @param <K> the output type of the key mapping function
   * @return a {@code Collector} which collects elements into a {@code Map}
   */
  public static <T, K> Collector<T, ?, Map<K, T>> toMap(
      Function<? super T, ? extends K> keyMapper) {
    return toMap(keyMapper, Function.identity(), SerBiOp.justAfter(), HashMap::new);
  }

  /**
   * Returns a {@code Collector} that accumulates elements into a {@code Map} whose keys and values
   * are the result of applying the provided mapping functions to the input elements.
   *
   * <p>If the mapped keys contains duplicates (according to {@link
   * java.lang.Object#equals(Object)}), the value mapping function is applied to each equal element,
   * and the results are merged using the provided merging function.
   *
   * @param <T> the type of the input elements
   * @param <K> the output type of the key mapping function
   * @param <U> the output type of the value mapping function
   * @param keyMapper a mapping function to produce keys
   * @param valueMapper a mapping function to produce values
   * @param mergeFunction a merge function, used to resolve collisions between values associated
   *     with the same key, as supplied to {@link java.util.Map#merge(Object, Object, BiFunction)}
   * @return a {@code Collector} which collects elements into a {@code Map} whose keys are the
   *     result of applying a key mapping function to the input elements, and whose values are the
   *     result of applying a value mapping function to all input elements equal to the key and
   *     combining them using the merge function
   * @apiNote There are multiple ways to deal with collisions between multiple elements mapping to
   *     the same key. The other forms of {@code toMap} simply use a merge function that throws
   *     unconditionally, but you can easily write more flexible merge policies. For example, if you
   *     have a stream of {@code Person}, and you want to produce a "phone book" mapping name to
   *     address, but it is possible that two persons have the same name, you can do as follows to
   *     gracefully deals with these collisions, and produce a {@code Map} mapping names to a
   *     concatenated list of addresses:
   *     <pre>{@code
   * Map<String, String> phoneBook
   *     people.stream().collect(toMap(Person::getName,
   *                                   Person::getAddress,
   *                                   (s, a) -> s + ", " + a));
   * }</pre>
   *
   * @implNote The returned {@code Collector} is not concurrent. For parallel stream pipelines, the
   *     {@code combiner} function operates by merging the keys from one map into another, which can
   *     be an expensive operation. If it is not required that results are merged into the {@code
   *     Map} in encounter order, using {@link #toConcurrentMap(Function, Function, BinaryOperator)}
   *     may offer better parallel performance.
   * @see #toMap(Function, Function)
   * @see #toMap(Function, Function, BinaryOperator, Supplier)
   * @see #toConcurrentMap(Function, Function, BinaryOperator)
   */
  public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper,
      BinaryOperator<U> mergeFunction) {
    return toMap(keyMapper, valueMapper, mergeFunction, HashMap::new);
  }

  /**
   * Returns a {@code Collector} that accumulates elements into a {@code Map} whose keys and values
   * are the result of applying the provided mapping functions to the input elements.
   *
   * <p>If the mapped keys contains duplicates (according to {@link
   * java.lang.Object#equals(Object)}), the value mapping function is applied to each equal element,
   * and the results are merged using the provided merging function. The {@code Map} is created by a
   * provided supplier function.
   *
   * @param <T> the type of the input elements
   * @param <K> the output type of the key mapping function
   * @param <U> the output type of the value mapping function
   * @param <M> the type of the resulting {@code Map}
   * @param keyMapper a mapping function to produce keys
   * @param valueMapper a mapping function to produce values
   * @param mergeFunction a merge function, used to resolve collisions between values associated
   *     with the same key, as supplied to {@link java.util.Map#merge(Object, Object, BiFunction)}
   * @param mapSupplier a function which returns a new, empty {@code Map} into which the results
   *     will be inserted
   * @return a {@code Collector} which collects elements into a {@code Map} whose keys are the
   *     result of applying a key mapping function to the input elements, and whose values are the
   *     result of applying a value mapping function to all input elements equal to the key and
   *     combining them using the merge function
   * @implNote The returned {@code Collector} is not concurrent. For parallel stream pipelines, the
   *     {@code combiner} function operates by merging the keys from one map into another, which can
   *     be an expensive operation. If it is not required that results are merged into the {@code
   *     Map} in encounter order, using {@link #toConcurrentMap(Function, Function, BinaryOperator,
   *     Supplier)} may offer better parallel performance.
   * @see #toMap(Function, Function)
   * @see #toMap(Function, Function, BinaryOperator)
   * @see #toConcurrentMap(Function, Function, BinaryOperator, Supplier)
   */
  public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper,
      BinaryOperator<U> mergeFunction,
      Supplier<M> mapSupplier) {
    BiConsumer<M, T> accumulator =
        (map, element) -> {
          Opp<? extends K> keyOpp = Opp.of(element).map(keyMapper);
          U value =
              Opp.of(element)
                  .map(valueMapper)
                  .map(
                      newValue ->
                          keyOpp
                              .map(map::get)
                              .map(oldValue -> mergeFunction.apply(oldValue, newValue))
                              .orElse(newValue))
                  .get();
          map.put(keyOpp.get(), value);
        };
    return new Collective.CollectorImpl<>(
        mapSupplier, accumulator, mapMerger(mergeFunction), CH_ID);
  }

  /**
   * Returns a concurrent {@code Collector} that accumulates elements into a {@code ConcurrentMap}
   * whose keys and values are the result of applying the provided mapping functions to the input
   * elements.
   *
   * <p>If the mapped keys contains duplicates (according to {@link
   * java.lang.Object#equals(Object)}), an {@code IllegalStateException} is thrown when the
   * collection operation is performed. If the mapped keys may have duplicates, use {@link
   * #toConcurrentMap(Function, Function, BinaryOperator)} instead.
   *
   * @param <T> the type of the input elements
   * @param <K> the output type of the key mapping function
   * @param <U> the output type of the value mapping function
   * @param keyMapper the mapping function to produce keys
   * @param valueMapper the mapping function to produce values
   * @return a concurrent, unordered {@code Collector} which collects elements into a {@code
   *     ConcurrentMap} whose keys are the result of applying a key mapping function to the input
   *     elements, and whose values are the result of applying a value mapping function to the input
   *     elements
   * @apiNote It is common for either the key or the value to be the input elements. In this case,
   *     the utility method {@link java.util.function.Function#identity()} may be helpful. For
   *     example, the following produces a {@code Map} mapping students to their grade point
   *     average:
   *     <pre>{@code
   * Map<Student, Double> studentToGPA
   *     students.stream().collect(toMap(Functions.identity(),
   *                                     student -> computeGPA(student)));
   * }</pre>
   *     And the following produces a {@code Map} mapping a unique identifier to students:
   *     <pre>{@code
   * Map<String, Student> studentIdToStudent
   *     students.stream().collect(toConcurrentMap(Student::getId,
   *                                               Functions.identity());
   * }</pre>
   *     <p>This is a {@code Collector.Characteristics#CONCURRENT} concurrent and {@code
   *     Collector.Characteristics#UNORDERED} unordered Collector.
   * @see #toMap(Function, Function)
   * @see #toConcurrentMap(Function, Function, BinaryOperator)
   * @see #toConcurrentMap(Function, Function, BinaryOperator, Supplier)
   */
  public static <T, K, U> Collector<T, ?, ConcurrentMap<K, U>> toConcurrentMap(
      Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
    return toConcurrentMap(keyMapper, valueMapper, SerBiOp.justAfter(), ConcurrentHashMap::new);
  }

  /**
   * Returns a concurrent {@code Collector} that accumulates elements into a {@code ConcurrentMap}
   * whose keys and values are the result of applying the provided mapping functions to the input
   * elements.
   *
   * <p>If the mapped keys contains duplicates (according to {@link
   * java.lang.Object#equals(Object)}), the value mapping function is applied to each equal element,
   * and the results are merged using the provided merging function.
   *
   * @param <T> the type of the input elements
   * @param <K> the output type of the key mapping function
   * @param <U> the output type of the value mapping function
   * @param keyMapper a mapping function to produce keys
   * @param valueMapper a mapping function to produce values
   * @param mergeFunction a merge function, used to resolve collisions between values associated
   *     with the same key, as supplied to {@link java.util.Map#merge(Object, Object, BiFunction)}
   * @return a concurrent, unordered {@code Collector} which collects elements into a {@code
   *     ConcurrentMap} whose keys are the result of applying a key mapping function to the input
   *     elements, and whose values are the result of applying a value mapping function to all input
   *     elements equal to the key and combining them using the merge function
   * @apiNote There are multiple ways to deal with collisions between multiple elements mapping to
   *     the same key. The other forms of {@code toConcurrentMap} simply use a merge function that
   *     throws unconditionally, but you can easily write more flexible merge policies. For example,
   *     if you have a stream of {@code Person}, and you want to produce a "phone book" mapping name
   *     to address, but it is possible that two persons have the same name, you can do as follows
   *     to gracefully deals with these collisions, and produce a {@code Map} mapping names to a
   *     concatenated list of addresses:
   *     <pre>{@code
   * Map<String, String> phoneBook
   *     people.stream().collect(toConcurrentMap(Person::getName,
   *                                             Person::getAddress,
   *                                             (s, a) -> s + ", " + a));
   * }</pre>
   *     <p>This is a {@code Collector.Characteristics#CONCURRENT} concurrent and {@code
   *     Collector.Characteristics#UNORDERED} unordered Collector.
   * @see #toConcurrentMap(Function, Function)
   * @see #toConcurrentMap(Function, Function, BinaryOperator, Supplier)
   * @see #toMap(Function, Function, BinaryOperator)
   */
  public static <T, K, U> Collector<T, ?, ConcurrentMap<K, U>> toConcurrentMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper,
      BinaryOperator<U> mergeFunction) {
    return toConcurrentMap(keyMapper, valueMapper, mergeFunction, ConcurrentHashMap::new);
  }

  /**
   * Returns a concurrent {@code Collector} that accumulates elements into a {@code ConcurrentMap}
   * whose keys and values are the result of applying the provided mapping functions to the input
   * elements.
   *
   * <p>If the mapped keys contains duplicates (according to {@link
   * java.lang.Object#equals(Object)}), the value mapping function is applied to each equal element,
   * and the results are merged using the provided merging function. The {@code ConcurrentMap} is
   * created by a provided supplier function.
   *
   * <p>This is a {@code Collector.Characteristics#CONCURRENT} concurrent and {@code
   * Collector.Characteristics#UNORDERED} unordered Collector.
   *
   * @param <T> the type of the input elements
   * @param <K> the output type of the key mapping function
   * @param <U> the output type of the value mapping function
   * @param <M> the type of the resulting {@code ConcurrentMap}
   * @param keyMapper a mapping function to produce keys
   * @param valueMapper a mapping function to produce values
   * @param mergeFunction a merge function, used to resolve collisions between values associated
   *     with the same key, as supplied to {@link java.util.Map#merge(Object, Object, BiFunction)}
   * @param mapSupplier a function which returns a new, empty {@code Map} into which the results
   *     will be inserted
   * @return a concurrent, unordered {@code Collector} which collects elements into a {@code
   *     ConcurrentMap} whose keys are the result of applying a key mapping function to the input
   *     elements, and whose values are the result of applying a value mapping function to all input
   *     elements equal to the key and combining them using the merge function
   * @see #toConcurrentMap(Function, Function)
   * @see #toConcurrentMap(Function, Function, BinaryOperator)
   * @see #toMap(Function, Function, BinaryOperator, Supplier)
   */
  public static <T, K, U, M extends ConcurrentMap<K, U>> Collector<T, ?, M> toConcurrentMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper,
      BinaryOperator<U> mergeFunction,
      Supplier<M> mapSupplier) {
    BiConsumer<M, T> accumulator =
        (map, element) ->
            map.merge(keyMapper.apply(element), valueMapper.apply(element), mergeFunction);
    return new Collective.CollectorImpl<>(
        mapSupplier, accumulator, mapMerger(mergeFunction), CH_CONCURRENT_ID);
  }

  /**
   * Returns a {@code Collector} which applies an {@code int}-producing mapping function to each
   * input element, and returns summary statistics for the resulting values.
   *
   * @param <T> the type of the input elements
   * @param mapper a mapping function to apply to each element
   * @return a {@code Collector} implementing the summary-statistics reduction
   * @see #summarizingDouble(ToDoubleFunction)
   * @see #summarizingLong(ToLongFunction)
   */
  public static <T> Collector<T, ?, IntSummaryStatistics> summarizingInt(
      ToIntFunction<? super T> mapper) {
    return new Collective.CollectorImpl<>(
        IntSummaryStatistics::new,
        (r, t) -> r.accept(mapper.applyAsInt(t)),
        (l, r) -> {
          l.combine(r);
          return l;
        },
        CH_ID);
  }

  /**
   * Returns a {@code Collector} which applies an {@code long}-producing mapping function to each
   * input element, and returns summary statistics for the resulting values.
   *
   * @param <T> the type of the input elements
   * @param mapper the mapping function to apply to each element
   * @return a {@code Collector} implementing the summary-statistics reduction
   * @see #summarizingDouble(ToDoubleFunction)
   * @see #summarizingInt(ToIntFunction)
   */
  public static <T> Collector<T, ?, LongSummaryStatistics> summarizingLong(
      ToLongFunction<? super T> mapper) {
    return new Collective.CollectorImpl<>(
        LongSummaryStatistics::new,
        (r, t) -> r.accept(mapper.applyAsLong(t)),
        (l, r) -> {
          l.combine(r);
          return l;
        },
        CH_ID);
  }

  /**
   * Returns a {@code Collector} which applies an {@code double}-producing mapping function to each
   * input element, and returns summary statistics for the resulting values.
   *
   * @param <T> the type of the input elements
   * @param mapper a mapping function to apply to each element
   * @return a {@code Collector} implementing the summary-statistics reduction
   * @see #summarizingLong(ToLongFunction)
   * @see #summarizingInt(ToIntFunction)
   */
  public static <T> Collector<T, ?, DoubleSummaryStatistics> summarizingDouble(
      ToDoubleFunction<? super T> mapper) {
    return new Collective.CollectorImpl<>(
        DoubleSummaryStatistics::new,
        (r, t) -> r.accept(mapper.applyAsDouble(t)),
        (l, r) -> {
          l.combine(r);
          return l;
        },
        CH_ID);
  }

  /**
   * entryToMap.
   *
   * @param <K> a K class
   * @param <V> a V class
   * @return a {@link java.util.stream.Collector} object
   */
  public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> entryToMap() {
    return toMap(Map.Entry::getKey, Map.Entry::getValue);
  }

  public static <K, V, M extends Map<K, V>> Collector<Map.Entry<K, V>, ?, M> entryToMap(
      Supplier<M> mapSupplier) {
    return toMap(Map.Entry::getKey, Map.Entry::getValue, SerBiOp.justAfter(), mapSupplier);
  }

  /**
   * Simple implementation class for {@code Collector}.
   *
   * @param <T> the type of elements to be collected
   * @param <R> the type of the result
   */
  static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
    private final Supplier<A> supplier;
    private final BiConsumer<A, T> accumulator;
    private final BinaryOperator<A> combiner;
    private final Function<A, R> finisher;
    private final Set<Characteristics> characteristics;

    CollectorImpl(
        Supplier<A> supplier,
        BiConsumer<A, T> accumulator,
        BinaryOperator<A> combiner,
        Function<A, R> finisher,
        Set<Characteristics> characteristics) {
      this.supplier = supplier;
      this.accumulator = accumulator;
      this.combiner = combiner;
      this.finisher = finisher;
      this.characteristics = characteristics;
    }

    CollectorImpl(
        Supplier<A> supplier,
        BiConsumer<A, T> accumulator,
        BinaryOperator<A> combiner,
        Set<Characteristics> characteristics) {
      this(supplier, accumulator, combiner, SerFunc.cast(), characteristics);
    }

    @Override
    public BiConsumer<A, T> accumulator() {
      return accumulator;
    }

    @Override
    public Supplier<A> supplier() {
      return supplier;
    }

    @Override
    public BinaryOperator<A> combiner() {
      return combiner;
    }

    @Override
    public Function<A, R> finisher() {
      return finisher;
    }

    @Override
    public Set<Characteristics> characteristics() {
      return characteristics;
    }
  }

  /** Implementation class used by partition. */
  private static final class Partition<T> extends AbstractMap<Boolean, T>
      implements Map<Boolean, T> {
    final T forTrue;
    final T forFalse;

    Partition(T forTrue, T forFalse) {
      this.forTrue = forTrue;
      this.forFalse = forFalse;
    }

    @Override
    public Set<Map.Entry<Boolean, T>> entrySet() {
      return new AbstractSet<Map.Entry<Boolean, T>>() {
        @Override
        public Iterator<Map.Entry<Boolean, T>> iterator() {
          Map.Entry<Boolean, T> falseEntry = new SimpleImmutableEntry<>(false, forFalse);
          Map.Entry<Boolean, T> trueEntry = new SimpleImmutableEntry<>(true, forTrue);
          return Arrays.asList(falseEntry, trueEntry).iterator();
        }

        @Override
        public int size() {
          return 2;
        }
      };
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      if (!super.equals(o)) {
        return false;
      }
      Partition<?> partition = (Partition<?>) o;
      return Objects.equals(forTrue, partition.forTrue)
          && Objects.equals(forFalse, partition.forFalse);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), forTrue, forFalse);
    }
  }

  /**
   * 将流转为{@link Steam}
   *
   * @param <T> 输入元素类型
   * @return 收集器
   */
  public static <T> Collector<T, ?, Steam<T>> toSteam() {
    return transform(ArrayList::new, Steam::of);
  }

  /**
   * 收集元素，将其转为指定{@link Collection}集合后，再对该集合进行转换，并最终返回转换后的结果。 返回的收集器的效果等同于：
   *
   * <pre>{@code
   * 	Collection<T> coll = Stream.of(a, b, c, d)
   * 		.collect(Collectors.toCollection(collFactory));
   * 	R result = mapper.apply(coll);
   * }</pre>
   *
   * @param collFactory 中间收集输入元素的集合的创建方法
   * @param mapper 最终将元素集合映射为返回值的方法
   * @param <R> 返回值类型
   * @param <T> 输入元素类型
   * @param <C> 中间收集输入元素的集合类型
   * @return 收集器
   */
  public static <T, R, C extends Collection<T>> Collector<T, C, R> transform(
      Supplier<C> collFactory, Function<C, R> mapper) {
    Objects.requireNonNull(collFactory);
    Objects.requireNonNull(mapper);
    return new CollectorImpl<>(
        collFactory,
        C::add,
        (l1, l2) -> {
          l1.addAll(l2);
          return l1;
        },
        mapper,
        CH_NOID);
  }

  /**
   * 收集元素，将其转为{@link ArrayList}集合后，再对该集合进行转换，并最终返回转换后的结果。 返回的收集器的效果等同于：
   *
   * <pre>{@code
   * 	List<T> coll = Stream.of(a, b, c, d)
   * 		.collect(Collectors.toList());
   * 	R result = mapper.apply(coll);
   * }</pre>
   *
   * @param mapper 最终将元素集合映射为返回值的方法
   * @param <R> 返回值类型
   * @param <T> 输入元素类型
   * @return 收集器
   */
  public static <T, R> Collector<T, List<T>, R> transform(Function<List<T>, R> mapper) {
    return transform(ArrayList::new, mapper);
  }
}
