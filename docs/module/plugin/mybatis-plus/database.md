### execute

> 通过entityClass获取BaseMapper，再传入lambda使用该mapper
>
>参数一:要操作数据库对应实体类的Class对象
>
>参数二:lambda操作 操作的对象继承BaseMapper对象，应当转换为对应的子类(推荐使用内部封装IMapper<T>)
>
>！如操作类型不是当前Class对象的子类则则抛出ClassCastException
>
> 对于LambdaQueryWrapper中包含忽略注释的，不会进行查询

本方法适用于自定义操作

```java
// list ==> 模拟实体对象集合

// 向UserInfo所对于的表中插入list
Database.execute(UserInfo.class, (IMapper<UserInfo> m) -> m.insertOneSql(list));
```

### insertFewSql

> (批量插入)以几条sql方式插入（批量）需要实现IMapper
>
>在不满足参数二的情况下为单条sql，如果满足则进行分批插入
>
>参数一:要进行插入的集合
>
>参数二:更新批次数量(选填，默认值为500)(可选)

```java
// list ==> 模拟实体对象集合

Database.insertFewSql(list);
// 此时的批量插入执行的是
INSERT INTO user_info ( id,name,age,email,version,gmt_deleted ) VALUES ( default, ?, default, default, default, default ) , ( default, ?, ?, ?, default, default )

Database.insertFewSql(list, 1);// 当有一个值的时候就对其进行分批插入，而不是一次全插入    
// 此时的sql为    
INSERT INTO user_info ( id,name,age,email,version,gmt_deleted ) VALUES ( default, ?, default, default, default, default )

INSERT INTO user_info ( id,name,age,email,version,gmt_deleted ) VALUES ( default, ?, ?, ?, default, default )

```

### save

> 单值插入
>
>参数:entity

```java
UserInfo entity = new UserInfo();
entity.setName("ruben");
Database.save(entity);
// 此时执行sql为
INSERT INTO user_info ( name ) VALUES ( ? )
```

### saveBatch

> mp的循环插入（此方法为单值循环插入）

```java
// list ==> 模拟实体对象集合
Database.saveBatch(list);
// 此时执行sql为
INSERT INTO user_info ( name ) VALUES ( ? )

INSERT INTO user_info ( name, age, email ) VALUES ( ?, ?, ? )
```

### updateFewSql

> (批量更新)以几条sql方式更新（批量）需要实现IMapper
>
>在不满足参数二的情况下为单条sql，如果满足则进行分批更新
>
>参数一:要进行更新的集合
>
>参数二:更新批次数量(选填，默认值为500)(可选)

```sql
// list ==> 模拟实体对象集合

Database.updateFewSql(list);
// 此时批量更新执行的sql
UPDATE user_info SET name=case id when ? then ? when ? then ? end, age=case id when ? then age when ? then age end, email=case id when ? then email when ? then email end, version=case id when ? then version when ? then version end WHERE id IN ( ? , ? ) AND gmt_deleted='2001-01-01 00:00:00'
```

### updateById

> 根据 ID 选择修改
>
>参数: entity

```java
UserInfo entity = new UserInfo();

entity.setId(1L);
entity.setName("bee bee I'm a sheep");

Database.updateById(entity);
// 此时执行sql为
UPDATE user_info SET name=? WHERE id=? AND gmt_deleted='2001-01-01 00:00:00'
```

### update

> 根据 whereEntity 条件，更新记录
>
>参数一: 要更新成的entity(可选)
>
>参数二:实体对象封装操作类(!当不写入参数一时，需要设置sqlset)

```java
Database.update(Wrappers.lambdaUpdate(UserInfo.class).eq(UserInfo::getId, 1L).set(UserInfo::getName, "be better");

UserInfo entity = new UserInfo();
entity.setId(1L);
entity.setName("be better");
Database.update(entity, Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, 1L));
// 此时两个语句执行的sql是一样的 ,都是这条sql    
UPDATE user_info SET name=? WHERE gmt_deleted='2001-01-01 00:00:00' AND (id = ?)
```

### updateBatchById

> mp的根据entity的id批量更新
>
>参数一:要进行更新的集合
>
>参数二:更新批次数量(选填，默认值为500)(可选)

### updateForceById

> `强制`根据id修改，指定的字段不管是否为null也会修改
>
>参数一: 实体对象
>
>参数二: 可变长参数SFunction操作 (传入lambda指定要修改字段)

```java
UserInfo sheep = new UserInfo();

sheep.setId(1L);
sheep.setName("bee bee I'm a sheep");

Database.updateForceById(sheep, UserInfo::getName, UserInfo::getAge);
// 即使我们当前对象中没有age也会将数据库对应id的age更新为null
```

### remove

> 根据 whereEntity 条件，删除记录
>
>参数:实体对象封装操作类(!当不写入参数一时，需要设置sqlset)

```java
Database.remove(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, 1L));

// 将UserInfo所对应的表中的id为1的数据进行删除
```

### removeById

> 重载方法
>
>单参数：实体对象(根据实体对象ID删除)
>
>双参数：
>
> - 参数一: 主键ID
>
> - 参数二: 实体类的Class对象

```java
UserInfo entity = new UserInfo();
entity.setId(1L);

Database.removeById(entity);
// 两句话做的是同样的事情
Database.removeById(1L, UserInfo.class);
```

### removeByIds

> 根据主键id批量删除
>
> - 参数一: 主键ID或实体列表
>
> - 参数二: 操作实体类的Class对象

```java
Database.removeByIds(Arrays.asList(1L, 2L), UserInfo.class);

// 将UserInfo所对应的表中的主键id为1,2的数据删除
```

### removeByMap

> 根据map删除数据

```java
Database.removeByMap(Collections.singletonMap("id", 1L), UserInfo.class);

// 将UserInfo所对应的表中的字段名为id值为1的数据删除
```

### saveOrUpdate

> 保存或更新
>
> - 参数一: 要保存的对象
>
> - 参数二: 实体对象封装操作类
>
>如果两个参数的话会根据updateWrapper尝试修改，否继续执行saveOrUpdate(T)方法
>
>此次修改主要是减少了此项业务代码的代码量（存在性验证之后的saveOrUpdate操作）

```java
Database.saveOrUpdate(entity);

// 如果当前的对象中id为null直接插入，如果不为null，并且数据存在就更新，如果数据不存在就进行插入
```

### getOne

> 获取一条数据
>
> - 参数一: 根据的queryWrapper对象
>
> - 参数二: 如果得到多条数据是否抛出异常 (默认为true)(选填)
>
>!如果有多条数据的话想要随机取一条应当在queryWrapper中加上限制条件 queryWrapper.last("LIMIT 1")
>
>！！！加了限制条件会有sql注入风险

```java
LambdaQueryWrapper<UserInfo> wrapper = Wrappers.lambdaQuery(UserInfo.class);
Database.getOne(wrapper.eq(UserInfo::getAge,20));  // 此时如果有多个年龄等于20岁的用户就会抛出异常
Database.getOne(wrapper, false);  // 这样就不会抛出异常  
```

### listByMap

> 查询（根据 columnMap 条件）

```java
Map<String, Object> map = new HashMap<>();
map.put("id", 1L);

List<UserInfo> list = Database.listByMap(map, UserInfo.class);
// 得到UserInfo所对应表中id为1的所有数据封装到list中
```

### listByIds

> 根据ids得到所对应的所有数据集合

```java
Database.listByIds(Arrays.asList(1L, 2L), UserInfo.class);

// 得到UserInfo所对应表中id为1还有2的数据封装到list中
```

### getMap

> 根据 Wrapper，查询一条记录，并且将其字段名为key，值为value封装到map中返回

```java
Database.getMap(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getAge,20));

// 得到UserInfo所对应表中age为20的数据封装到map中
```

### list

> 重载方法
>
>- 当传入参数为entity的Class对象时，是查询所有
>
>- 当传入参数为queryWrapper时，根据条件查询列表

```java
Database.list(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getAge,20));
// 得到UserInfo所对应表中age为20的数据封装到list中

Database.list(UserInfo.class);
// 得到UserInfo所对应表中所有数据封装到list中
```

### listMaps

> 重载方法
>
>- 当传入参数为entity的Class对象时，是查询所有数据将其单条数据封装成map之后再封装到list中
>
>- 当传入参数为queryWrapper时，根据条件查询列表将其单条数据封装成map之后再封装到list中

```java
Database.listMaps(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getAge,20));
// 得到UserInfo所对应表中age为20的数据,将其单条数据封装成map之后再封装到list中

Database.listMaps(UserInfo.class);
// 得到UserInfo所对应表中所有数据,将其单条数据封装成map之后再封装到list中
```

### listObjs

> 重载方法
>
>一个参数时
>
>- 当传入参数为entity的Class对象时，是查询所有封装到list中
>
>两个参数时
>
>- 如果第一个参数为entity的Class对象时，是查询所有数据然后得到第二个参数传入的SFunction操作映射到list中
>- 如果第一个参数为queryWrapper时，根据条件查询列表然后得到第二个参数传入的SFunction操作映射到list中

```java
Database.listObjs(UserInfo.class);
// 得到UserInfo所对应表中所有数据封装到list中

Database.listObjs(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getAge,20), UserInfo::getId);
// 得到UserInfo所对应表中所有age为20的数据将其所有的id封装到list中

Database.listObjs(UserInfo.class, UserInfo::getName);
// 得到UserInfo所对应表中所有数据将其所有的name封装到list中
```

### pageMaps

> 分页查询
>
>重载方法：第一个参数都为分页对象
>
>- 第二个参数如果为entity的Class对象那么就进行无条件分页查询
>- 第二个参数如果为queryWrapper那么就进行条件分页查询
>
>其中分页里的records不是实体对象而是其字段名和值封装成的map

```java
Database.pageMaps(new Page<>(1, 1), UserInfo.class);
// 就是进行分页封装里边的records是map而已
```

### page

> 分页查询
>
>重载方法：第一个参数都为分页对象
>
>- 第二个参数如果为entity的Class对象那么就进行无条件分页查询
>- 第二个参数如果为queryWrapper那么就进行条件分页查询
>
>这个里边封装的是实体对象

```java
Database.page(new Page<>(1, 1), UserInfo.class);
// 常规分页查询

Database.page(new Page<>(1, 1), Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getAge,20));
// 根据条件分页查询
```

### getObj

> 根据 Wrapper，查询一条记录，并对其进行转换操作
>
>参数一: 条件
>
>参数二: 传入的SFunction操作

```java
Database.getObj(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, 1L), UserInfo::getName);
// 得到UserInfo所对应表中id为1的数据,并进行映射操作
```

