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
package org.dromara.streamquery.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.dromara.streamquery.stream.core.bean.BeanHelper;
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author VampireAchao
 * @since 2023/4/13 17:54
 */
public class WrapperHelper {

  private static final Map<String, SFunction<?, ?>> LAMBDA_GETTER_CACHE = new WeakHashMap<>();

  private WrapperHelper() {
    /* Do not new me! */
  }

  /**
   * in查询
   *
   * @param wrapper 条件构造器
   * @param dataList 数据
   * @param <T> 类型
   * @return 条件构造器
   */
  @SuppressWarnings("unchecked")
  public static <T> LambdaQueryWrapper<T> multiIn(LambdaQueryWrapper<T> wrapper, List<T> dataList) {
    if (Lists.isEmpty(dataList)) {
      return wrapper;
    }
    final Class<T> entityClass = Database.getEntityClass(dataList);
    final List<TableFieldInfo> fieldList = TableInfoHelper.getTableInfo(entityClass).getFieldList();
    multiOr(
        wrapper,
        fieldList,
        (w, tableField) -> {
          SFunction<T, ?> getterFunction =
              (SFunction<T, ?>)
                  LAMBDA_GETTER_CACHE.computeIfAbsent(
                      entityClass + StringPool.AT + tableField.getProperty(),
                      property -> {
                        Method getter =
                            ReflectHelper.getMethod(
                                entityClass,
                                BeanHelper.GETTER_PREFIX
                                    + tableField
                                        .getProperty()
                                        .substring(0, 1)
                                        .toUpperCase(Locale.ROOT)
                                    + tableField.getProperty().substring(1));
                        return LambdaHelper.revert(SFunction.class, getter);
                      });
          final List<?> list = Steam.of(dataList).map(getterFunction).nonNull().toList();
          w.in(Lists.isNotEmpty(list), getterFunction, list);
        });
    return wrapper;
  }

  /**
   * or 查询
   *
   * @param wrapper 条件构造器
   * @param dataList 数据
   * @param biConsumer 逻辑处理
   * @param <W> 条件构造器
   * @param <T> 实体类型
   * @param <R> 数据类型
   * @return 条件构造器
   */
  public static <W extends AbstractWrapper<T, ?, W>, T, R> W multiOr(
      W wrapper, Collection<R> dataList, BiConsumer<W, R> biConsumer) {
    if (Lists.isEmpty(dataList)) {
      return Database.notActive(wrapper);
    }
    return wrapper.nested(w -> dataList.forEach(data -> biConsumer.accept(w.or(), data)));
  }
}
