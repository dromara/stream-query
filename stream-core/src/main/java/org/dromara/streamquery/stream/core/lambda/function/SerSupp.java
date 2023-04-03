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
package org.dromara.streamquery.stream.core.lambda.function;

import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 可序列化的Supplier
 *
 * @param <T> the type of results supplied by this supplier
 * @author VampireAchao Cizai_

 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface SerSupp<T> extends Supplier<T>, Serializable {

    /**
     * Gets a result.
     *
     * @return a result
     * @throws java.lang.Exception exception
     */
    T getting() throws Throwable;

    /**
     * Gets a result.
     */
    @Override
    default T get() {
        try {
            return getting();
        } catch (Throwable e) {
            throw new LambdaInvokeException(e);
        }
    }

    /**
     * last
     *
     * @param serSups lambda
     * @param <T>     type
     * @return lambda
     */
    @SafeVarargs
    static <T> SerSupp<T> last(SerSupp<T>... serSups) {
        return Stream.of(serSups).reduce((l, r) -> r).orElseGet(() -> () -> null);
    }

}
