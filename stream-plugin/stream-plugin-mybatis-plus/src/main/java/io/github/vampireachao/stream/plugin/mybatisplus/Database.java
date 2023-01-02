package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.vampireachao.stream.core.lambda.LambdaHelper;
import io.github.vampireachao.stream.core.lambda.function.SerBiCons;
import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.Opp;
import io.github.vampireachao.stream.core.optional.Sf;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import io.github.vampireachao.stream.core.stream.Steam;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper.IMapper;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 辅助类
 *
 * @author VampireAchao Cizai_
 * @since 1.0
 */
public class Database {
    private static final Log log = LogFactory.getLog(Database.class);

    private static final Map<Class<?>, Map<String, String>> TABLE_PROPERTY_COLUMN_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Map<String, String>> TABLE_COLUMN_PROPERTY_CACHE = new ConcurrentHashMap<>();

    /**
     *
     */
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
        return (Objects.nonNull(wrapper)) &&
                (wrapper.getSqlComment() == null || !wrapper.getSqlComment().contains(PluginConst.WRAPPER_NOT_ACTIVE));
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
     * <p>activeOrElse.</p>
     *
     * @param wrapper a W object
     * @param mapper  a {@link java.util.function.Function} object
     * @param other   a U object
     * @param <W>     a W class
     * @param <T>     a T class
     * @param <U>     a U class
     * @param <R>     a R class
     * @return a R object
     */
    public static <W extends AbstractWrapper<T, ?, ?>, T, U extends R, R>
    R activeOrElse(W wrapper, Function<? super W, U> mapper, U other) {
        return isActive(wrapper) ? mapper.apply(wrapper) : other;
    }

    /**
     * <p>notActive.</p>
     *
     * @param wrapper a T object
     * @param <T>     a T class
     * @return a T object
     */
    public static <T extends Join<?>> T notActive(T wrapper) {
        return notActive(true, wrapper);
    }

    /**
     * <p>notActive.</p>
     *
     * @param condition a {@link java.lang.Boolean} object
     * @param wrapper   a T object
     * @param <T>       a T class
     * @return a T object
     */
    public static <T extends Join<?>> T notActive(Boolean condition, T wrapper) {
        wrapper.comment(Boolean.TRUE.equals(condition), PluginConst.WRAPPER_NOT_ACTIVE);
        return wrapper;
    }

    /**
     * <p>lambdaQuery.</p>
     *
     * @param data      a E object
     * @param condition a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <T>       a T class
     * @param <E>       a E class
     * @return a {@link io.github.vampireachao.stream.core.optional.Opp} object
     */
    public static <T, E extends Serializable> Opp<LambdaQueryWrapper<T>> lambdaQuery(E data, SFunction<T, E> condition) {
        return Opp.of(data).map(value -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition)))
                .eq(condition, value)).filter(Database::isActive);
    }

    /**
     * <p>lambdaQuery.</p>
     *
     * @param dataList  a {@link java.util.Collection} object
     * @param condition a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <T>       a T class
     * @param <E>       a E class
     * @return a {@link io.github.vampireachao.stream.core.optional.Opp} object
     */
    public static <T, E extends Serializable>
    Opp<LambdaQueryWrapper<T>> lambdaQuery(Collection<E> dataList, SFunction<T, E> condition) {
        return Opp.ofColl(dataList)
                .map(value -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition)))
                        .in(condition, new HashSet<>(value))).filter(Database::isActive);
    }

    /**
     * <p>lambdaQuery.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <T>         a T class
     * @param <K>         a K class
     * @return a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
     */
    public static <T, K> LambdaQueryWrapper<T> lambdaQuery(SFunction<T, K> keyFunction) {
        return Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(keyFunction)));
    }

    /**
     * <p>select.</p>
     *
     * @param wrapper a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
     * @param columns a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <T>     a T class
     * @return a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
     */
    @SafeVarargs
    public static <T> LambdaQueryWrapper<T> select(LambdaQueryWrapper<T> wrapper, SFunction<T, ?>... columns) {
        return select(wrapper, LambdaQueryWrapper::select, columns);
    }

    /**
     * <p>select.</p>
     *
     * @param wrapper            a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
     * @param whenAllMatchColumn a {@link io.github.vampireachao.stream.core.lambda.function.SerBiCons} object
     * @param columns            a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <T>                a T class
     * @return a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
     */
    @SafeVarargs
    public static <T>
    LambdaQueryWrapper<T> select(LambdaQueryWrapper<T> wrapper,
                                 SerBiCons<LambdaQueryWrapper<T>, SFunction<T, ?>[]> whenAllMatchColumn,
                                 SFunction<T, ?>... columns) {
        if (Stream.of(columns).allMatch(func -> Objects.nonNull(func) &&
                PropertyNamer.isGetter(LambdaHelper.resolve(func).getLambda().getImplMethodName()))) {
            whenAllMatchColumn.accept(wrapper, columns);
        }
        return wrapper;
    }

    /**
     * 以几条sql方式插入（批量）需要实现IMapper
     *
     * @param entityList 数据
     * @param <T>        a T class
     * @return 成功与否
     */
    public static <T> boolean saveFewSql(Collection<T> entityList) {
        return saveFewSql(entityList, PluginConst.DEFAULT_BATCH_SIZE);
    }

    /**
     * 以几条sql方式修改插入（批量）需要实现IMapper
     *
     * @param entityList 数据
     * @param batchSize  分批条数
     * @return 成功与否
     * @param <T> a T class
     */
    public static <T> boolean saveOrUpdateFewSql(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList) || batchSize < 0) {
            return false;
        }
        Class<T> entityClass = getEntityClass(entityList);
        TableInfo tableInfo = getTableInfo(entityClass);
        Map<Boolean, List<T>> isInsertDataListMap = Steam.of(entityList)
                .partition(entity -> Objects.isNull(tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty())));
        return saveFewSql(isInsertDataListMap.get(true)) && updateFewSql(isInsertDataListMap.get(false));
    }

    /**
     * 以几条sql方式修改插入（批量）需要实现IMapper
     *
     * @param entityList 数据
     * @return 成功与否
     * @param <T> a T class
     */
    public static <T> boolean saveOrUpdateFewSql(Collection<T> entityList) {
        return saveOrUpdateFewSql(entityList, PluginConst.DEFAULT_BATCH_SIZE);
    }

    /**
     * 以几条sql方式插入（批量）需要实现IMapper
     *
     * @param entityList 数据
     * @param batchSize  分批条数
     * @return 成功与否
     * @param <T> a T class
     */
    public static <T> boolean saveFewSql(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList) || batchSize <= 0) {
            return false;
        }
        return execute(getEntityClass(entityList),
                (IMapper<T> baseMapper) -> entityList.size() == baseMapper.saveFewSql(entityList, batchSize));
    }


    /**
     * 以单条sql方式插入（批量）需要实现IMapper
     *
     * @param entityList 数据
     * @return 成功与否
     * @param <T> a T class
     */
    public static <T> boolean updateOneSql(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        return execute(getEntityClass(entityList),
                (IMapper<T> baseMapper) -> entityList.size() == baseMapper.updateOneSql(entityList));
    }

    /**
     * 以几条sql方式插入（批量）需要实现IMapper
     *
     * @param entityList 数据
     * @return 成功与否
     * @param <T> a T class
     */
    public static <T> boolean updateFewSql(Collection<T> entityList) {
        return updateFewSql(entityList, PluginConst.DEFAULT_BATCH_SIZE);
    }

    /**
     * 以几条sql方式插入（批量）需要实现IMapper
     *
     * @param entityList 数据
     * @param batchSize  分批条数
     * @return 成功与否
     * @param <T> a T class
     */
    public static <T> boolean updateFewSql(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList) || batchSize <= 0) {
            return false;
        }
        return execute(getEntityClass(entityList),
                (IMapper<T> baseMapper) -> entityList.size() == baseMapper.updateFewSql(entityList, batchSize));
    }

    /**
     * 强制根据id修改，指定的字段不管是否为null也会修改
     *
     * @param entity     实体对象
     * @param updateKeys 指定字段
     * @return 是否成功
     * @param <T> a T class
     */
    @SafeVarargs
    public static <T> boolean updateForceById(T entity, SFunction<T, ?>... updateKeys) {
        if (Objects.isNull(entity) || ArrayUtils.isEmpty(updateKeys)) {
            return Db.updateById(entity);
        }
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) entity.getClass();
        TableInfo tableInfo = getTableInfo(entityClass);
        T bean = ClassUtils.newInstance(entityClass);
        String keyProperty = tableInfo.getKeyProperty();
        ReflectHelper.setFieldValue(bean, keyProperty, ReflectionKit.getFieldValue(entity, keyProperty));
        LambdaUpdateWrapper<T> updateWrapper = Stream.of(updateKeys).reduce(Wrappers.lambdaUpdate(bean),
                (wrapper, field) -> wrapper.set(field, field.apply(entity)), (l, r) -> r);
        return Db.update(bean, updateWrapper);
    }

    /**
     * 根据 Wrapper，查询一条记录 <br/>
     * <p>结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")</p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return T
     * @param <T> a T class
     */
    public static <T> T getOne(AbstractWrapper<T, ?, ?> queryWrapper) {
        return getOne(queryWrapper, true);
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param throwEx      有多个 result 是否抛出异常
     * @return T
     * @param <T> a T class
     */
    public static <T> T getOne(AbstractWrapper<T, ?, ?> queryWrapper, boolean throwEx) {
        if (!isActive(queryWrapper)) {
            return null;
        }
        Class<T> entityClass = getEntityClass(queryWrapper);
        if (throwEx) {
            return execute(entityClass, baseMapper -> baseMapper.selectOne(queryWrapper));
        }
        return execute(entityClass, baseMapper -> SqlHelper.getObject(log, baseMapper.selectList(queryWrapper)));
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return {@link java.util.Map}<{@link java.lang.String}, {@link java.lang.Object}>
     * @param <T> a T class
     */
    public static <T> Map<String, Object> getMap(AbstractWrapper<T, ?, ?> queryWrapper) {
        return activeOrElse(queryWrapper, Db::getMap, null);
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return long
     * @param <T> a T class
     */
    public static <T> long count(AbstractWrapper<T, ?, ?> queryWrapper) {
        return activeOrElse(queryWrapper, Db::count, 0L);
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return {@code java.util.List<T>}
     * @param <T> a T class
     */
    public static <T> List<T> list(AbstractWrapper<T, ?, ?> queryWrapper) {
        return activeOrElse(queryWrapper, Db::list, new ArrayList<>());
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return {@code List<Map<String, Object>>}
     * @param <T> a T class
     */
    public static <T> List<Map<String, Object>> listMaps(AbstractWrapper<T, ?, ?> queryWrapper) {
        return activeOrElse(queryWrapper, Db::listMaps, new ArrayList<>());
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return {@code List<Object>}
     * @param <T> a T class
     */
    public static <T> List<Object> listObjs(AbstractWrapper<T, ?, ?> queryWrapper) {
        return activeOrElse(queryWrapper, Db::listObjs, new ArrayList<>());
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param mapper       转换函数
     * @return {@code List<V>}
     * @param <T> a T class
     * @param <V> a V class
     */
    public static <T, V> List<V> listObjs(AbstractWrapper<T, ?, ?> queryWrapper, SFunction<? super T, V> mapper) {
        return activeOrElse(queryWrapper, w -> Db.listObjs(w, mapper), new ArrayList<>());
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return {@code E}
     * @param <T> a T class
     * @param <E> a E class
     */
    public static <T, E extends IPage<Map<String, Object>>> E pageMaps(E page, AbstractWrapper<T, ?, ?> queryWrapper) {
        return activeOrElse(queryWrapper, w -> Db.pageMaps(page, w), page);
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @return {@link com.baomidou.mybatisplus.core.metadata.IPage}<{@code T}>
     * @param <T> a T class
     */
    public static <T> IPage<T> page(IPage<T> page, AbstractWrapper<T, ?, ?> queryWrapper) {
        return activeOrElse(queryWrapper, w -> Db.page(page, w), page);
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类
     * @param mapper       转换函数
     * @return {@code V}
     * @param <T> a T class
     * @param <V> a V class
     */
    public static <T, V> V getObj(AbstractWrapper<T, ?, ?> queryWrapper, SFunction<? super T, V> mapper) {
        return activeOrElse(queryWrapper, w -> Db.getObj(queryWrapper, mapper), null);
    }

    /**
     * 通过entityClass获取BaseMapper，再传入lambda使用该mapper，本方法自动释放链接
     *
     * @param entityClass 实体类
     * @param sFunction   lambda操作
     * @return {@link com.baomidou.mybatisplus.core.mapper.BaseMapper} 返回lambda执行结果
     * @param <T> a T class
     * @param <R> a R class
     * @param <M> a M class
     */
    @SuppressWarnings("unchecked")
    public static <T, R, M extends BaseMapper<T>> R execute(Class<T> entityClass, SFunction<M, R> sFunction) {
        SqlSession sqlSession = SqlHelper.sqlSession(entityClass);
        try {
            return sFunction.apply((M) SqlHelper.getMapper(entityClass, sqlSession));
        } finally {
            SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(entityClass));
        }
    }

    /**
     * 获取po对应的 {@code Map<属性,字段>}
     *
     * @param entityClass 实体类型
     * @return {@link java.util.Map}<{@link java.lang.String}, {@link java.lang.String}>
     */
    public static Map<String, String> getPropertyColumnMap(Class<?> entityClass) {
        return TABLE_PROPERTY_COLUMN_CACHE.computeIfAbsent(entityClass,
                clazz -> {
                    TableInfo tableInfo = getTableInfo(clazz);
                    Map<String, String> propertyColumnMap = Steam.of(tableInfo.getFieldList())
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
        return TABLE_COLUMN_PROPERTY_CACHE.computeIfAbsent(entityClass,
                clazz -> {
                    TableInfo tableInfo = getTableInfo(clazz);
                    Map<String, String> columnPropertyMap = Steam.of(tableInfo.getFieldList())
                            .toMap(TableFieldInfo::getColumn, TableFieldInfo::getProperty);
                    columnPropertyMap.put(tableInfo.getKeyColumn(), tableInfo.getKeyProperty());
                    return Collections.unmodifiableMap(columnPropertyMap);
                });
    }

    /**
     * 通过属性lambda获取字段名
     *
     * @param property 属性lambda
     * @return 字段名
     * @param <T> a T class
     * @param <R> a R class
     */
    public static <T, R extends Comparable<R>> String propertyToColumn(SFunction<T, R> property) {
        LambdaMeta lambdaMeta = LambdaUtils.extract(property);
        return propertyToColumn(lambdaMeta.getInstantiatedClass(),
                PropertyNamer.methodToProperty(lambdaMeta.getImplMethodName()));
    }

    /**
     * 通过属性名获取字段名
     *
     * @param clazz    实体类型
     * @param property 属性名
     * @return 字段名
     */
    public static String propertyToColumn(Class<?> clazz, String property) {
        return getPropertyColumnMap(clazz).get(property);
    }

    /**
     * 通过字段名获取属性名
     *
     * @param clazz  实体类型
     * @param column 字段名
     * @return 属性名
     */
    public static String columnToProperty(Class<?> clazz, String column) {
        return getColumnPropertyMap(clazz).get(column);
    }

    /**
     * 将orders里的column从property转column
     *
     * @param page  page对象
     * @param clazz 实体类型
     * @param <T> a T class
     */
    public static <T> void ordersPropertyToColumn(Page<T> page, Class<T> clazz) {
        page.getOrders().forEach(SerCons.multi(
                order -> Sf.of(order.getColumn()).takeUnless(SqlInjectionUtils::check)
                        .require(() -> new IllegalArgumentException(
                                String.format("order column { %s } must not null or be sql injection",
                                        order.getColumn()))),
                order -> order.setColumn(propertyToColumn(clazz, order.getColumn()))
        ));
    }

    /**
     * 从集合中获取实体类型
     *
     * @param entityList 实体集合
     * @return 实体类型
     */
    private static <T> Class<T> getEntityClass(Collection<T> entityList) {
        Class<T> entityClass = null;
        for (T entity : entityList) {
            if (entity != null && entity.getClass() != null) {
                entityClass = SerFunc.<Class<?>, Class<T>>cast().apply(entity.getClass());
                break;
            }
        }
        Assert.notNull(entityClass, "error: can not get entityClass from entityList");
        return entityClass;
    }

    /**
     * 从wrapper中尝试获取实体类型
     *
     * @param queryWrapper 条件构造器
     * @return 实体类型
     */
    private static <T> Class<T> getEntityClass(AbstractWrapper<T, ?, ?> queryWrapper) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        if (entityClass == null) {
            T entity = queryWrapper.getEntity();
            if (entity != null) {
                entityClass = SerFunc.<Class<?>, Class<T>>cast().apply(entity.getClass());
            }
        }
        Assert.notNull(entityClass, "error: can not get entityClass from wrapper");
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
                .orElseThrow(() -> ExceptionUtils.mpe("error: can not find TableInfo from Class: \"%s\".",
                        entityClass.getName()));
    }

}
