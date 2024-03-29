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

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.SimpleTypeRegistry;
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.core.collection.Maps;
import org.dromara.streamquery.stream.core.lambda.function.SerBiCons;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.constant.PluginConst;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.mapper.IMapper;
import org.mybatis.spring.SqlSessionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.dromara.streamquery.stream.core.clazz.ClassHelper.cast;

/**
 * 辅助类
 *
 * @author VampireAchao Cizai_
 * @since 1.0
 */
public class Database {
  private static final Log LOG = LogFactory.getLog(Database.class);

  private static final Map<Class<?>, Map<String, String>> TABLE_PROPERTY_COLUMN_CACHE =
      new ConcurrentHashMap<>();
  private static final Map<Class<?>, Map<String, String>> TABLE_COLUMN_PROPERTY_CACHE =
      new ConcurrentHashMap<>();
  private static final Map<Class<?>, Class<?>> ENTITY_MAPPER_CLASS_CACHE =
      new ConcurrentHashMap<>();

  private Database() {
    /* Do not new me! */
  }

  /**
   * 是否激活
   *
   * @param wrapper 条件构造器
   * @return 是否激活
   */
  public static boolean isActive(AbstractWrapper<?, ?, ?> wrapper) {
    return (Objects.nonNull(wrapper))
        && (wrapper.getSqlComment() == null
            || !wrapper.getSqlComment().contains(PluginConst.WRAPPER_NOT_ACTIVE));
  }

  /**
   * 是否未激活
   *
   * @param wrapper 条件构造器
   * @return 是否未激活
   */
  public static boolean isNotActive(AbstractWrapper<?, ?, ?> wrapper) {
    return !isActive(wrapper);
  }

  /**
   * activeOrElse.
   *
   * @param wrapper a W object
   * @param mapper a {@link java.util.function.Function} object
   * @param other a U object
   * @param <W> a W class
   * @param <T> a T class
   * @param <U> a U class
   * @param <R> a R class
   * @return a R object
   */
  public static <W extends AbstractWrapper<T, ?, ?>, T, U extends R, R> R activeOrElse(
      W wrapper, Function<? super W, U> mapper, U other) {
    return isActive(wrapper) ? mapper.apply(wrapper) : other;
  }

  /**
   * notActive.
   *
   * @param wrapper a T object
   * @param <T> a T class
   * @return a T object
   */
  public static <T extends Join<?>> T notActive(T wrapper) {
    return notActive(true, wrapper);
  }

  /**
   * notActive.
   *
   * @param condition a {@link java.lang.Boolean} object
   * @param wrapper a T object
   * @param <T> a T class
   * @return a T object
   */
  public static <T extends Join<?>> T notActive(Boolean condition, T wrapper) {
    wrapper.comment(Boolean.TRUE.equals(condition), PluginConst.WRAPPER_NOT_ACTIVE);
    return wrapper;
  }

  /**
   * lambdaQuery.
   *
   * @param data a E object
   * @param condition a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @param <E> a E class
   * @return a {@link Opp} object
   * @deprecated use {@link WrapperHelper#lambdaQuery(Serializable, SFunction)} instead
   */
  @Deprecated
  public static <T, E extends Serializable> Opp<LambdaQueryWrapper<T>> lambdaQuery(
      E data, SFunction<T, E> condition) {
    return Opp.of(data)
        .map(
            value ->
                Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition)))
                    .eq(condition, value))
        .filter(Database::isActive);
  }

  /**
   * lambdaQuery.
   *
   * @param dataList a {@link java.util.Collection} object
   * @param condition a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @param <E> a E class
   * @return a {@link Opp} object
   * @deprecated use {@link WrapperHelper#lambdaQuery(Collection, SFunction)} instead
   */
  @Deprecated
  public static <T, E extends Serializable> Opp<LambdaQueryWrapper<T>> lambdaQuery(
      Collection<E> dataList, SFunction<T, E> condition) {
    return Opp.ofColl(dataList)
        .map(
            value ->
                Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition)))
                    .in(condition, new HashSet<>(value)))
        .filter(Database::isActive);
  }

  /**
   * lambdaQuery.
   *
   * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @param <K> a K class
   * @return a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
   */
  public static <T, K> LambdaQueryWrapper<T> lambdaQuery(SFunction<T, K> keyFunction) {
    return Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(keyFunction)));
  }

  /**
   * select.
   *
   * @param wrapper a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
   *     object
   * @param columns a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @return a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
   * @deprecated please use {@link WrapperHelper#select(LambdaQueryWrapper, SFunction[])}
   */
  @Deprecated
  @SafeVarargs
  public static <T> LambdaQueryWrapper<T> select(
      LambdaQueryWrapper<T> wrapper, SFunction<T, ?>... columns) {
    return select(wrapper, LambdaQueryWrapper::select, columns);
  }

  /**
   * select.
   *
   * @param wrapper a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
   *     object
   * @param whenAllMatchColumn a {@link SerBiCons} object
   * @param columns a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @return a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
   * @deprecated please use {@link WrapperHelper#select(LambdaQueryWrapper, SerBiCons, SFunction[])}
   */
  @Deprecated
  @SafeVarargs
  public static <T> LambdaQueryWrapper<T> select(
      LambdaQueryWrapper<T> wrapper,
      SerBiCons<LambdaQueryWrapper<T>, SFunction<T, ?>[]> whenAllMatchColumn,
      SFunction<T, ?>... columns) {
    return WrapperHelper.select(wrapper, whenAllMatchColumn, columns);
  }

  /**
   * 插入一条记录（选择字段，策略插入）
   *
   * @param entity 实体对象
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean save(T entity) {
    if (Objects.isNull(entity)) {
      return false;
    }
    Class<T> entityClass = cast(entity.getClass());
    Integer result = execute(entityClass, baseMapper -> baseMapper.insert(entity));
    return SqlHelper.retBool(result);
  }

  /**
   * 插入（批量）
   *
   * @param entityList 实体对象集合
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean saveBatch(Collection<T> entityList) {
    return saveBatch(entityList, PluginConst.DEFAULT_BATCH_SIZE);
  }

  /**
   * 插入（批量）
   *
   * @param entityList 实体对象集合
   * @param batchSize 插入批次数量
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean saveBatch(Collection<T> entityList, int batchSize) {
    if (CollectionUtils.isEmpty(entityList) || batchSize <= 0) {
      return false;
    }
    Class<T> entityClass = getEntityClass(entityList);
    Class<?> mapperClass = getMapperClass(entityClass);
    String sqlStatement = SqlHelper.getSqlStatement(mapperClass, SqlMethod.INSERT_ONE);
    return SqlHelper.executeBatch(
        entityClass,
        LOG,
        entityList,
        batchSize,
        (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
  }

  /**
   * 以几条sql方式插入（批量）需要实现IMapper
   *
   * @param entityList 数据
   * @param <T> a T class
   * @return 成功与否
   */
  public static <T> boolean saveFewSql(Collection<T> entityList) {
    return saveFewSql(entityList, PluginConst.DEFAULT_BATCH_SIZE);
  }

  /**
   * 以几条sql方式修改插入（批量）需要实现IMapper
   *
   * @param entityList 数据
   * @param batchSize 分批条数
   * @param <T> a T class
   * @return 成功与否
   */
  public static <T> boolean saveOrUpdateFewSql(Collection<T> entityList, int batchSize) {
    if (CollectionUtils.isEmpty(entityList) || batchSize < 0) {
      return false;
    }
    Class<T> entityClass = getEntityClass(entityList);
    TableInfo tableInfo = getTableInfo(entityClass);
    Map<Boolean, List<T>> isInsertDataListMap =
        Steam.of(entityList)
            .partition(
                entity ->
                    Objects.isNull(tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty())));
    final List<T> saveList = isInsertDataListMap.get(true);
    final List<T> updateList = isInsertDataListMap.get(false);
    boolean isSuccess = true;
    if (Lists.isNotEmpty(saveList)) {
      isSuccess = saveFewSql(saveList, batchSize);
    }
    if (Lists.isNotEmpty(updateList)) {
      final boolean isUpdateSuccess = updateFewSql(updateList, batchSize);
      if (isSuccess) {
        isSuccess = isUpdateSuccess;
      }
    }
    return isSuccess;
  }

  /**
   * 以几条sql方式修改插入（批量）需要实现IMapper
   *
   * @param entityList 数据
   * @param <T> a T class
   * @return 成功与否
   */
  public static <T> boolean saveOrUpdateFewSql(Collection<T> entityList) {
    return saveOrUpdateFewSql(entityList, PluginConst.DEFAULT_BATCH_SIZE);
  }

  /**
   * 以几条sql方式插入（批量）需要实现IMapper
   *
   * @param entityList 数据
   * @param batchSize 分批条数
   * @param <T> a T class
   * @return 成功与否
   */
  public static <T> boolean saveFewSql(Collection<T> entityList, int batchSize) {
    if (CollectionUtils.isEmpty(entityList) || batchSize <= 0) {
      return false;
    }
    return execute(
        getEntityClass(entityList),
        (IMapper<T> baseMapper) ->
            entityList.size() == baseMapper.saveFewSql(entityList, batchSize));
  }

  /**
   * 以单条sql方式插入（批量）需要实现IMapper
   *
   * @param entityList 数据
   * @param <T> a T class
   * @return 成功与否
   */
  public static <T> boolean updateOneSql(Collection<T> entityList) {
    if (CollectionUtils.isEmpty(entityList)) {
      return false;
    }
    return execute(
        getEntityClass(entityList),
        (IMapper<T> baseMapper) -> entityList.size() == baseMapper.updateOneSql(entityList));
  }

  /**
   * 以几条sql方式插入（批量）需要实现IMapper
   *
   * @param entityList 数据
   * @param <T> a T class
   * @return 成功与否
   */
  public static <T> boolean updateFewSql(Collection<T> entityList) {
    return updateFewSql(entityList, PluginConst.DEFAULT_BATCH_SIZE);
  }

  /**
   * 以几条sql方式插入（批量）需要实现IMapper
   *
   * @param entityList 数据
   * @param batchSize 分批条数
   * @param <T> a T class
   * @return 成功与否
   */
  public static <T> boolean updateFewSql(Collection<T> entityList, int batchSize) {
    if (CollectionUtils.isEmpty(entityList) || batchSize <= 0) {
      return false;
    }
    return execute(
        getEntityClass(entityList),
        (IMapper<T> baseMapper) ->
            entityList.size() == baseMapper.updateFewSql(entityList, batchSize));
  }

  /**
   * 批量修改插入
   *
   * @param entityList 实体对象集合
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean saveOrUpdateBatch(Collection<T> entityList) {
    return saveOrUpdateBatch(entityList, PluginConst.DEFAULT_BATCH_SIZE);
  }

  /**
   * 批量修改插入
   *
   * @param entityList 实体对象集合
   * @param batchSize 每次的数量
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
    if (CollectionUtils.isEmpty(entityList) || batchSize < 0) {
      return false;
    }
    Class<T> entityClass = getEntityClass(entityList);
    TableInfo tableInfo = getTableInfo(entityClass);
    Class<?> mapperClass = getMapperClass(entityClass);
    String keyProperty = tableInfo.getKeyProperty();
    Assert.notEmpty(
        keyProperty,
        "error: can not execute. because can not find column for primary key from entity!");
    return SqlHelper.saveOrUpdateBatch(
        entityClass,
        mapperClass,
        LOG,
        entityList,
        batchSize,
        (sqlSession, entity) -> {
          Object idVal = tableInfo.getPropertyValue(entity, keyProperty);
          return StringUtils.checkValNull(idVal)
              || CollectionUtils.isEmpty(
                  sqlSession.selectList(
                      SqlHelper.getSqlStatement(mapperClass, SqlMethod.SELECT_BY_ID), entity));
        },
        (sqlSession, entity) -> {
          MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
          param.put(Constants.ENTITY, entity);
          sqlSession.update(SqlHelper.getSqlStatement(mapperClass, SqlMethod.UPDATE_BY_ID), param);
        });
  }

  /**
   * 根据 ID 删除
   *
   * @param id 主键ID
   * @param entityClass 实体类
   * @param <T> a T class
   * @param <U> a U class
   * @return boolean
   */
  public static <T, U extends Serializable & Comparable<? super U>> boolean removeById(
      U id, Class<T> entityClass) {
    return execute(entityClass, baseMapper -> SqlHelper.retBool(baseMapper.deleteById(id)));
  }

  /**
   * 根据实体(ID)删除
   *
   * @param entity 实体
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean removeById(T entity) {
    if (Objects.isNull(entity)) {
      return false;
    }
    Class<T> entityClass = cast(entity.getClass());
    return execute(entityClass, baseMapper -> SqlHelper.retBool(baseMapper.deleteById(entity)));
  }

  /**
   * 根据 entity 条件，删除记录
   *
   * @param queryWrapper 实体包装类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean remove(AbstractWrapper<T, ?, ?> queryWrapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper ->
            activeOrElse(queryWrapper, w -> SqlHelper.retBool(baseMapper.delete(w)), false));
  }

  /**
   * 根据 ID 选择修改
   *
   * @param entity 实体对象
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean updateById(T entity) {
    if (Objects.isNull(entity)) {
      return false;
    }
    Class<T> entityClass = cast(entity.getClass());
    return execute(entityClass, baseMapper -> SqlHelper.retBool(baseMapper.updateById(entity)));
  }

  /**
   * 强制根据id修改，指定的字段不管是否为null也会修改
   *
   * @param entity 实体对象
   * @param updateKeys 指定字段
   * @param <T> a T class
   * @return 是否成功
   */
  @SafeVarargs
  public static <T> boolean updateForceById(T entity, SFunction<T, ?>... updateKeys) {
    if (Objects.isNull(entity) || ArrayUtils.isEmpty(updateKeys)) {
      return updateById(entity);
    }
    Class<T> entityClass = cast(entity.getClass());
    TableInfo tableInfo = getTableInfo(entityClass);
    T bean = ClassUtils.newInstance(entityClass);
    String keyProperty = tableInfo.getKeyProperty();
    ReflectHelper.setFieldValue(
        bean, keyProperty, ReflectHelper.getFieldValue(entity, keyProperty));
    LambdaUpdateWrapper<T> updateWrapper =
        Stream.of(updateKeys)
            .reduce(
                Wrappers.lambdaUpdate(bean),
                (wrapper, field) -> wrapper.set(field, field.apply(entity)),
                (l, r) -> r);
    return update(bean, updateWrapper);
  }

  /**
   * 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
   *
   * @param updateWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean update(AbstractWrapper<T, ?, ?> updateWrapper) {
    return update(null, updateWrapper);
  }

  /**
   * 根据 whereEntity 条件，更新记录
   *
   * @param entity 实体对象
   * @param updateWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean update(T entity, AbstractWrapper<T, ?, ?> updateWrapper) {
    return execute(
        getEntityClass(updateWrapper),
        baseMapper ->
            activeOrElse(
                updateWrapper, w -> SqlHelper.retBool(baseMapper.update(entity, w)), false));
  }

  /**
   * 根据ID 批量修改
   *
   * @param entityList 实体对象集合
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean updateBatchById(Collection<T> entityList) {
    return updateBatchById(entityList, PluginConst.DEFAULT_BATCH_SIZE);
  }

  /**
   * 根据ID 批量修改
   *
   * @param entityList 实体对象集合
   * @param batchSize 修改批次数量
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean updateBatchById(Collection<T> entityList, int batchSize) {
    if (CollectionUtils.isEmpty(entityList) || batchSize <= 0) {
      return false;
    }
    Class<T> entityClass = getEntityClass(entityList);
    Class<?> mapperClass = getMapperClass(entityClass);
    String sqlStatement = SqlHelper.getSqlStatement(mapperClass, SqlMethod.UPDATE_BY_ID);
    return SqlHelper.executeBatch(
        entityClass,
        LOG,
        entityList,
        batchSize,
        (sqlSession, entity) -> {
          MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
          param.put(Constants.ENTITY, entity);
          sqlSession.update(sqlStatement, param);
        });
  }

  /**
   * 删除（根据ID 批量删除）
   *
   * @param list 主键ID或实体列表
   * @param entityClass 实体类
   * @param <T> a T class
   * @param <U> a U class
   * @return boolean
   */
  public static <T, U extends Serializable & Comparable<? super U>> boolean removeByIds(
      Collection<U> list, Class<T> entityClass) {
    if (Lists.isEmpty(list)) {
      return false;
    }
    return execute(entityClass, baseMapper -> SqlHelper.retBool(baseMapper.deleteBatchIds(list)));
  }

  /**
   * 删除（根据ID 批量删除）
   *
   * @param list 主键ID或实体列表
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean removeByIds(Collection<T> list) {
    if (Lists.isEmpty(list)) {
      return false;
    }
    return execute(
        getEntityClass(list), baseMapper -> SqlHelper.retBool(baseMapper.deleteBatchIds(list)));
  }

  /**
   * 根据 columnMap 条件，删除记录
   *
   * @param columnMap 表字段 map 对象
   * @param entityClass 实体类
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean removeByMap(Map<String, Object> columnMap, Class<T> entityClass) {
    return execute(entityClass, baseMapper -> SqlHelper.retBool(baseMapper.deleteByMap(columnMap)));
  }

  /**
   * TableId 注解存在修改记录，否插入一条记录
   *
   * @param entity 实体对象
   * @param <T> a T class
   * @return boolean
   */
  @SuppressWarnings("unchecked")
  public static <T, U extends Serializable & Comparable<? super U>> boolean saveOrUpdate(T entity) {
    if (Objects.isNull(entity)) {
      return false;
    }
    Class<T> entityClass = cast(entity.getClass());
    TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
    if (tableInfo == null) {
      throw ExceptionUtils.mpe(
          "error: can not execute. because can not find cache of TableInfo for entity!");
    }
    String keyProperty = tableInfo.getKeyProperty();
    Assert.notEmpty(
        keyProperty, "error: can not execute. because can not find column for id from entity!");
    U idVal = (U) tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
    return StringUtils.checkValNull(idVal) || Objects.isNull(getById(idVal, entityClass))
        ? save(entity)
        : updateById(entity);
  }

  /**
   * 根据 ID 查询
   *
   * @param id 主键ID
   * @param entityClass 实体类
   * @param <T> a T class
   * @param <U> a U class
   * @return T
   */
  public static <T, U extends Serializable & Comparable<? super U>> T getById(
      U id, Class<T> entityClass) {
    return execute(entityClass, baseMapper -> baseMapper.selectById(id));
  }

  /**
   * 根据 Wrapper，查询一条记录 <br>
   *
   * <p>结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
   *
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param <T> a T class
   * @return T
   */
  public static <T> T getOne(AbstractWrapper<T, ?, ?> queryWrapper) {
    return getOne(queryWrapper, true);
  }

  /**
   * 根据 Wrapper，查询一条记录
   *
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param throwEx 有多个 result 是否抛出异常
   * @param <T> a T class
   * @return T
   */
  public static <T> T getOne(AbstractWrapper<T, ?, ?> queryWrapper, boolean throwEx) {
    if (!isActive(queryWrapper)) {
      return null;
    }
    Class<T> entityClass = getEntityClass(queryWrapper);
    return execute(entityClass, baseMapper -> baseMapper.selectOne(queryWrapper, throwEx));
  }

  /**
   * 查询（根据 columnMap 条件）
   *
   * @param columnMap 表字段 map 对象
   * @param entityClass 实体类
   * @param <T> a T class
   * @return {@code java.util.List<T>}
   */
  public static <T> List<T> listByMap(Map<String, Object> columnMap, Class<T> entityClass) {
    return execute(entityClass, baseMapper -> baseMapper.selectByMap(columnMap));
  }

  /**
   * 查询（根据ID 批量查询）
   *
   * @param idList 主键ID列表
   * @param entityClass 实体类
   * @param <T> a T class
   * @param <U> a U class
   * @return {@code java.util.List<T>}
   */
  public static <T, U extends Serializable & Comparable<? super U>> List<T> listByIds(
      Collection<U> idList, Class<T> entityClass) {
    if (Lists.isEmpty(idList)) {
      return Lists.empty();
    }
    return execute(entityClass, baseMapper -> baseMapper.selectBatchIds(idList));
  }

  /**
   * 根据 Wrapper，查询一条记录
   *
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param <T> a T class
   * @return {@link java.util.Map}<{@link java.lang.String}, {@link java.lang.Object}>
   */
  public static <T> Map<String, Object> getMap(AbstractWrapper<T, ?, ?> queryWrapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper ->
            activeOrElse(
                queryWrapper, w -> SqlHelper.getObject(LOG, baseMapper.selectMaps(w)), null));
  }

  /**
   * 查询总记录数
   *
   * @param entityClass 实体类
   * @param <T> a T class
   * @return long
   * @see Wrappers#emptyWrapper()
   */
  public static <T> long count(Class<T> entityClass) {
    return execute(entityClass, baseMapper -> baseMapper.selectCount(null));
  }

  /**
   * 根据 Wrapper 条件，查询总记录数
   *
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param <T> a T class
   * @return long
   */
  public static <T> long count(AbstractWrapper<T, ?, ?> queryWrapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper -> activeOrElse(queryWrapper, baseMapper::selectCount, 0L));
  }

  /**
   * 查询列表
   *
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param <T> a T class
   * @return {@code java.util.List<T>}
   */
  public static <T> List<T> list(AbstractWrapper<T, ?, ?> queryWrapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper -> activeOrElse(queryWrapper, baseMapper::selectList, new ArrayList<>()));
  }

  /**
   * 查询所有
   *
   * @param entityClass 实体类
   * @param <T> a T class
   * @return {@code java.util.List<T>}
   * @see Wrappers#emptyWrapper()
   */
  public static <T> List<T> list(Class<T> entityClass) {
    return execute(entityClass, baseMapper -> baseMapper.selectList(null));
  }

  /**
   * 查询列表
   *
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param <T> a T class
   * @return {@code List<Map<String, Object>>}
   */
  public static <T> List<Map<String, Object>> listMaps(AbstractWrapper<T, ?, ?> queryWrapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper -> activeOrElse(queryWrapper, baseMapper::selectMaps, new ArrayList<>()));
  }

  /**
   * 查询所有列表
   *
   * @param entityClass 实体类
   * @param <T> a T class
   * @return {@code List<Map<String, Object>>}
   * @see Wrappers#emptyWrapper()
   */
  public static <T> List<Map<String, Object>> listMaps(Class<T> entityClass) {
    return execute(entityClass, baseMapper -> baseMapper.selectMaps(null));
  }

  /**
   * 查询全部记录
   *
   * @param entityClass 实体类
   * @param <T> a T class
   * @return {@code java.util.List<T>}
   */
  public static <T> List<T> listObjs(Class<T> entityClass) {
    return listObjs(entityClass, i -> i);
  }

  /**
   * 根据 Wrapper 条件，查询全部记录
   *
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param <T> a T class
   * @return {@code List<Object>}
   */
  public static <T> List<Object> listObjs(AbstractWrapper<T, ?, ?> queryWrapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper -> activeOrElse(queryWrapper, baseMapper::selectObjs, new ArrayList<>()));
  }

  /**
   * 根据 Wrapper 条件，查询全部记录
   *
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param mapper 转换函数
   * @param <T> a T class
   * @param <V> a V class
   * @return {@code List<V>}
   */
  public static <T, V> List<V> listObjs(
      AbstractWrapper<T, ?, ?> queryWrapper, SFunction<? super T, V> mapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper ->
            activeOrElse(
                queryWrapper,
                w -> Steam.of(baseMapper.selectList(w)).map(mapper).toList(),
                new ArrayList<>()));
  }

  /**
   * 查询全部记录
   *
   * @param entityClass 实体类
   * @param mapper 转换函数
   * @param <T> a T class
   * @param <V> a V class
   * @return {@code List<V>}
   */
  public static <T, V> List<V> listObjs(Class<T> entityClass, SFunction<? super T, V> mapper) {
    return execute(
        entityClass, baseMapper -> Steam.of(baseMapper.selectList(null)).map(mapper).toList());
  }

  /**
   * 无条件翻页查询
   *
   * @param page 翻页对象
   * @param entityClass 实体类
   * @param <T> a T class
   * @param <E> a E class
   * @return {@code E}
   * @see Wrappers#emptyWrapper()
   */
  public static <T, E extends IPage<Map<String, Object>>> E pageMaps(E page, Class<T> entityClass) {
    return execute(entityClass, baseMapper -> baseMapper.selectMapsPage(page, null));
  }

  /**
   * 翻页查询
   *
   * @param page 翻页对象
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param <T> a T class
   * @param <E> a E class
   * @return {@code E}
   */
  public static <T, E extends IPage<Map<String, Object>>> E pageMaps(
      E page, AbstractWrapper<T, ?, ?> queryWrapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper -> activeOrElse(queryWrapper, w -> baseMapper.selectMapsPage(page, w), page));
  }

  /**
   * 无条件翻页查询
   *
   * @param page 翻页对象
   * @param entityClass 实体类
   * @param <T> a T class
   * @return {@link com.baomidou.mybatisplus.core.metadata.IPage}<{@code T}>
   * @see Wrappers#emptyWrapper()
   */
  public static <T> IPage<T> page(IPage<T> page, Class<T> entityClass) {
    return execute(entityClass, baseMapper -> baseMapper.selectPage(page, null));
  }

  /**
   * 翻页查询
   *
   * @param page 翻页对象
   * @param queryWrapper 实体对象封装操作类 {@link
   *     com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
   * @param <T> a T class
   * @return {@link com.baomidou.mybatisplus.core.metadata.IPage}<{@code T}>
   */
  public static <T> IPage<T> page(IPage<T> page, AbstractWrapper<T, ?, ?> queryWrapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper -> activeOrElse(queryWrapper, w -> baseMapper.selectPage(page, w), page));
  }

  /**
   * 根据updateWrapper尝试修改，否继续执行saveOrUpdate(T)方法 此次修改主要是减少了此项业务代码的代码量（存在性验证之后的saveOrUpdate操作）
   *
   * @param entity 实体对象
   * @param updateWrapper 更新构造器
   * @param <T> a T class
   * @return boolean
   */
  public static <T> boolean saveOrUpdate(T entity, AbstractWrapper<T, ?, ?> updateWrapper) {
    if (!isActive(updateWrapper)) {
      return false;
    }
    return update(entity, updateWrapper) || saveOrUpdate(entity);
  }

  /**
   * 根据 Wrapper，查询一条记录
   *
   * @param queryWrapper 实体对象封装操作类
   * @param mapper 转换函数
   * @param <T> a T class
   * @param <V> a V class
   * @return {@code V}
   */
  public static <T, V> V getObj(
      AbstractWrapper<T, ?, ?> queryWrapper, SFunction<? super T, V> mapper) {
    return execute(
        getEntityClass(queryWrapper),
        baseMapper -> activeOrElse(queryWrapper, w -> mapper.apply(baseMapper.selectOne(w)), null));
  }

  /**
   * 通过entityClass获取BaseMapper，再传入lambda使用该mapper，本方法自动释放链接
   *
   * @param entityClass 实体类
   * @param sFunction lambda操作
   * @param <T> a T class
   * @param <R> a R class
   * @param <M> a M class
   * @return {@link com.baomidou.mybatisplus.core.mapper.BaseMapper} 返回lambda执行结果
   */
  public static <T, R, M extends BaseMapper<T>> R execute(
      Class<T> entityClass, SFunction<M, R> sFunction) {
    SqlSession sqlSession = SqlHelper.sqlSession(entityClass);
    try {
      return sFunction.apply(getMapper(entityClass, sqlSession));
    } finally {
      SqlSessionUtils.closeSqlSession(
          sqlSession, GlobalConfigUtils.currentSessionFactory(entityClass));
    }
  }

  /**
   * buildMapper 动态构建Mapper
   *
   * @param configuration mybatis-plus配置
   * @param entityClass 实体类
   */
  public static void buildMapper(Configuration configuration, Class<?> entityClass) {
    if (!(configuration instanceof MybatisConfiguration)) {
      throw new IllegalArgumentException("configuration must be MybatisConfiguration");
    }
    Maps.computeIfAbsent(
        ENTITY_MAPPER_CLASS_CACHE,
        entityClass,
        k -> {
          Class<?> dynamicMapper =
              new ByteBuddy()
                  .makeInterface(
                      TypeDescription.Generic.Builder.parameterizedType(IMapper.class, entityClass)
                          .build())
                  .name(
                      String.format(
                          "%s.%sMapper", PluginConst.DYNAMIC_MAPPER_PREFIX, entityClass.getName()))
                  .make()
                  .load(ClassUtils.class.getClassLoader())
                  .getLoaded();
          configuration.addMapper(dynamicMapper);
          return dynamicMapper;
        });
  }

  /**
   * 通过entityClass获取Mapper，记得要释放连接 例：
   *
   * <pre>{@code
   * SqlSession sqlSession = SqlHelper.sqlSession(entityClass);
   * try {
   *     BaseMapper<User> userMapper = getMapper(User.class, sqlSession);
   * } finally {
   *     sqlSession.close();
   * }
   * }</pre>
   *
   * @param entityClass 实体
   * @param <T> 实体类型
   * @param <M> Mapper类型
   * @return Mapper
   */
  @SuppressWarnings("unchecked")
  public static <T, M extends BaseMapper<T>> M getMapper(
      Class<T> entityClass, SqlSession sqlSession) {
    if (entityClass == null) {
      throw ExceptionUtils.mpe("entityClass can't be null!");
    }
    TableInfo tableInfo =
        Optional.ofNullable(TableInfoHelper.getTableInfo(entityClass))
            .orElseThrow(
                () ->
                    ExceptionUtils.mpe(
                        "Can not find TableInfo from Class: \"%s\".", entityClass.getName()));
    Class<?> mapperClass = getMapperClass(entityClass);
    return (M) tableInfo.getConfiguration().getMapper(mapperClass, sqlSession);
  }

  /**
   * 获取实体类和mapper缓存
   *
   * @return 实体类和mapper缓存
   */
  public static Map<Class<?>, Class<?>> getEntityMapperClassCache() {
    return ENTITY_MAPPER_CLASS_CACHE;
  }

  /**
   * 获取mapperClass
   *
   * @param clazz 实体类
   * @param <T> 实体类型
   * @return mapperClass
   */
  public static <T> Class<?> getMapperClass(Class<T> clazz) {
    if (clazz == null
        || clazz.isPrimitive()
        || SimpleTypeRegistry.isSimpleType(clazz)
        || clazz.isInterface()) {
      throw ExceptionUtils.mpe("找不到指定的class！请仅在明确确定会有 class 的时候，调用该方法");
    }
    Class<?> targetClass = ClassUtils.getUserClass(clazz);
    Class<?> mapperClass = ENTITY_MAPPER_CLASS_CACHE.get(targetClass);
    if (null != mapperClass) {
      return mapperClass;
    }
    Class<?> currentClass = clazz;
    while (null == mapperClass && Object.class != currentClass) {
      currentClass = currentClass.getSuperclass();
      mapperClass = ENTITY_MAPPER_CLASS_CACHE.get(ClassUtils.getUserClass(currentClass));
    }
    if (mapperClass == null) {
      mapperClass = ClassUtils.toClassConfident(getTableInfo(clazz).getCurrentNamespace());
    }
    if (mapperClass != null) {
      ENTITY_MAPPER_CLASS_CACHE.put(targetClass, mapperClass);
    }
    return mapperClass;
  }

  /**
   * 获取po对应的 {@code Map<属性,字段>}
   *
   * @param entityClass 实体类型
   * @return {@link java.util.Map}<{@link java.lang.String}, {@link java.lang.String}>
   */
  public static Map<String, String> getPropertyColumnMap(Class<?> entityClass) {
    return Maps.computeIfAbsent(
        TABLE_PROPERTY_COLUMN_CACHE,
        entityClass,
        clazz -> {
          TableInfo tableInfo = getTableInfo(clazz);
          Map<String, String> propertyColumnMap =
              Steam.of(tableInfo.getFieldList())
                  .toMap(TableFieldInfo::getProperty, TableFieldInfo::getColumn);
          propertyColumnMap.put(tableInfo.getKeyProperty(), tableInfo.getKeyColumn());
          return Collections.unmodifiableMap(propertyColumnMap);
        });
  }

  /**
   * 获取po对应的 {@code Map<字段,属性>}
   *
   * @param entityClass 实体类型
   * @return {@link java.util.Map}<{@link java.lang.String}, {@link java.lang.String}>
   */
  public static Map<String, String> getColumnPropertyMap(Class<?> entityClass) {
    return Maps.computeIfAbsent(
        TABLE_COLUMN_PROPERTY_CACHE,
        entityClass,
        clazz -> {
          TableInfo tableInfo = getTableInfo(clazz);
          Map<String, String> columnPropertyMap =
              Steam.of(tableInfo.getFieldList())
                  .toMap(TableFieldInfo::getColumn, TableFieldInfo::getProperty);
          columnPropertyMap.put(tableInfo.getKeyColumn(), tableInfo.getKeyProperty());
          return Collections.unmodifiableMap(columnPropertyMap);
        });
  }

  /**
   * 通过属性lambda获取字段名
   *
   * @param property 属性lambda
   * @param <T> a T class
   * @param <R> a R class
   * @return 字段名
   */
  public static <T, R extends Comparable<? super R>> String propertyToColumn(
      SFunction<T, R> property) {
    LambdaMeta lambdaMeta = LambdaUtils.extract(property);
    return propertyToColumn(
        lambdaMeta.getInstantiatedClass(),
        PropertyNamer.methodToProperty(lambdaMeta.getImplMethodName()));
  }

  /**
   * 通过属性名获取字段名
   *
   * @param clazz 实体类型
   * @param property 属性名
   * @return 字段名
   */
  public static String propertyToColumn(Class<?> clazz, String property) {
    return getPropertyColumnMap(clazz).get(property);
  }

  /**
   * 通过字段名获取属性名
   *
   * @param clazz 实体类型
   * @param column 字段名
   * @return 属性名
   */
  public static String columnToProperty(Class<?> clazz, String column) {
    return getColumnPropertyMap(clazz).get(column);
  }

  /**
   * 将orders里的column从property转column
   *
   * @param page page对象
   * @param clazz 实体类型
   * @param <T> a T class
   */
  public static <T> void ordersPropertyToColumn(Page<T> page, Class<T> clazz) {
    page.orders()
        .forEach(
            order ->
                Opp.of(propertyToColumn(clazz, order.getColumn())).ifPresent(order::setColumn));
  }

  /**
   * 从集合中获取实体类型
   *
   * @param entityList 实体集合
   * @return 实体类型
   */
  protected static <T> Class<T> getEntityClass(Collection<T> entityList) {
    Class<T> entityClass = null;
    for (T entity : entityList) {
      if (entity != null) {
        entityClass = cast(entity.getClass());
        break;
      }
    }
    if (entityClass == null) {
      throw ExceptionUtils.mpe("error: can not get entityClass from entityList");
    }
    Assert.isFalse(
        SimpleTypeRegistry.isSimpleType(entityClass), "error: entityClass can not be simple type");
    return entityClass;
  }

  /**
   * 从wrapper中尝试获取实体类型
   *
   * @param queryWrapper 条件构造器
   * @return 实体类型
   */
  protected static <T> Class<T> getEntityClass(AbstractWrapper<T, ?, ?> queryWrapper) {
    Class<T> entityClass = queryWrapper.getEntityClass();
    if (entityClass == null) {
      T entity = queryWrapper.getEntity();
      if (entity != null) {
        entityClass = cast(entity.getClass());
      }
    }
    if (entityClass == null) {
      throw ExceptionUtils.mpe("error: can not get entityClass from wrapper");
    }
    return entityClass;
  }

  /**
   * 获取表信息，获取不到报错提示
   *
   * @param entityClass 实体类
   * @return 对应表信息
   */
  private static <T> TableInfo getTableInfo(Class<T> entityClass) {
    return Optional.ofNullable(TableInfoHelper.getTableInfo(entityClass))
        .orElseThrow(
            () ->
                ExceptionUtils.mpe(
                    "error: can not find TableInfo from Class: \"%s\".", entityClass.getName()));
  }

  /**
   * 判断是否动态mapper
   *
   * @param mapperClassName mapper类名
   * @return 是否动态mapper
   */
  public static boolean isDynamicMapper(String mapperClassName) {
    return mapperClassName.startsWith(PluginConst.DYNAMIC_MAPPER_PREFIX);
  }
}
