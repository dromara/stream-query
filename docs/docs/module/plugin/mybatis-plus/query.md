# query

> `eq`和`in`函数会判空或空集合，**如果为空，不进行查询**
>
> `query`函数包含重载，可自行探索~

| 方法名        | 中间操作说明                                                                                       |
|------------|----------------------------------------------------------------------------------------------|
| of         | 进行查询器构造【User::getId】，对user表进行查询，id作为条件，in或者eq(等于)查询                                          |
| eq         | 条件构造与其参数相等                                                                                   |
| in         | 条件构造包含其参数                                                                                    |
| value      | 得到其指定属性(在每个操作中的意义不同)                                                                         |
| condition  | 额外条件，传入lambda【w->w.eq(User::getId,1L)】，参数为LambdaQueryWrapper，如果lambda中返回值为null【w->null】不进行查询 |
| peek       | 接收一个无返回值操作,对peek的数据进行操作(如果list对每一个元素进行操作)                                                    |
| parallel   | 并行                                                                                           |
| sequential | 串行                                                                                           |
| query      | 执行查询                                                                                         |

## One

> 单条数据查询

根据拼接条件查询单条数据，比之前版本定制化更高

```java
// 查询UserInfo实体类所对应表id为1的数据
UserInfo userInfo = One.of(UserInfo::getId).eq(1L).query();

// 查询UserInfo实体类所对应表id为1的数据其名字
String name = One.of(UserInfo::getId).eq(1L).value(UserInfo::getName).query();

// 查询UserInfo实体类所对应表id为1，并且年龄小于等于20的人的名字
String leAgeName = One.of(UserInfo::getId).eq(1L).value(UserInfo::getName)
                .condition(w -> w.le(UserInfo::getAge, 20))
                .query();
```

## Many

> 多条数据查询

```java
// 没有写查询条件的时候会将所有数据查出来
List<UserInfo> userInfoList = Many.of(UserInfo::getId).query();

// 获取所有名字为ZVerify的电子邮箱封装成list(串行执行)
List<String> emailList = Many.of(UserInfo::getName).eq("ZVerify").value(UserInfo::getEmail).sequential().query();

// 查询UserInfo实体类所对应表名字为ZVerify年龄小于等于20的电子邮箱封装成list
List<String> emailList = Many.of(UserInfo::getName).eq("ZVerify").value(UserInfo::getEmail).parallel()
                .condition(w -> w.le(UserInfo::getAge, 20))
                .query();
```

## OneToOne

> 一对一查询 ？ 将结果集通过匹配查询条件与value操作将结果集封装成map
>
>map的key为of里的筛选条件，value默认为entity对象，使用value之后为其传入lambda返回值

```java
// 返回map key为id，value为entity对象
Map<Long, UserInfo> idUserMap = OneToOne.of(UserInfo::getId).in(userIds).query();

// 返回map key为id，value为查询到entity的name
Map<Long, String> userIdNameMap = OneToOne.of(UserInfo::getId).in(userIds).value(UserInfo::getName).query();

// 返回map key为id，value为一个boolean类型的值，因为我们传入的value(SFunction)是一个判断操作，判断key所对应的entity对象的name是否不为null，并且包含a字符串
Map<Long, String> userIdHasANameMap = OneToOne.of(UserInfo::getId).in(userIds).condition(w -> w.select(UserInfo::getId, UserInfo::getName)).value(userInfo -> userInfo.getName() != null && userInfo.getName().contains("a")).query();
```

## OneToMany

> 一对多查询？ 将结果集通过匹配查询的数据查询出来然后封装成 <xxx,List<xxx>>类型的map
>
>map的key为of封装的查询条件，value是对其通过of里的条件进行分组之后的数据默认包装的是entity对象，如果使用value，包装的则为其传入lambda返回值

```java
// 返回map key为age,value中list的包装对象为entity对象(在进行peek等操作，且大数据量情况下的时候可以考虑并行)
Map<Integer, List<UserInfo>> ageUsersMap = OneToMany.of(UserInfo::getAge).in(userAges).parallel().query();

// 返回map key为age, value中的list的包装对象为entity对象的name
Map<Integer, List<String>> userAgeNameMap = OneToMany.of(UserInfo::getAge).in(userAges).value(UserInfo::getName).query();

// 返回map key为age, value中的list的包装对象为entity对象的name(新增的条件，只会查出年龄小于等于22岁的)
Map<Integer, List<String>> userAgeNameMap = OneToMany.of(UserInfo::getAge).in(userAges).value(UserInfo::getName).condition(w -> w.le(UserInfo::getAge, 22)).query();
```

## OneToManyToOne

> 一对多对一查询？ 主子表关联查询将结果集通过匹配查询的数据查询出来然后封装成 <xxx,List<xxx>>类型的map
>
>map的key为of封装的主表字段，value是进行连表查询之后查询到的子表数据封装到了对应的key的list集合，默认list里边包裹的为entity，如果指定的话包装的则为其传入lambda返回值

| 特殊方法    | 方法传参说明                           |
| ----------- | -------------------------------------- |
| of          | map的key，通过其进行筛选查询           |
| value       | 主表中与子表关联的字段                 |
| attachKey   | 子表中与主表关联的字段                 |
| attachValue | 指定value中的list的包裹的数据          |
| peek        | 对主表中的数据进行peek操作可传入消费者 |
| attachPeek  | 对子表中的数据进行peek操作可传入消费者 |

```java
// 此时查询的主表为UserRole所对应的数据库表，map的key为其userId属性，条件为userids集合中所包含的，与子表关联的属性为roleId，子表与主表关联的属性为id
Map<Long, List<RoleInfo>> userIdRoleInfosMap = OneToManyToOne.of(UserRole::getUserId).in(userIds).value(UserRole::getRoleId).attachKey(RoleInfo::getId).query();
```