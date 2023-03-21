## 🔧 配置

注入动态`Mapper`处理器

```java
    @Bean
    public DynamicMapperHandler dynamicMapperHandler(SqlSessionFactory sqlSessionFactory) throws Exception {
        // 使用ClassHelper的scanClasses方法扫描对应路径下的po生成Class文件集合放入第二个参数就可以了
        final List<Class<?>> entityClassList = ClassHelper.scanClasses("com.ruben.pojo.po");
        return new DynamicMapperHandler(sqlSessionFactory, entityClassList);
    }
```

动态`Mapper`注入后，即可使用`Database`进行`CRUD`操作

## 📚使用

```java
    // 批量保存
    Database.saveBatch(userList);
    // 使用userIds进行in查询，得到map key为id，value为entity对象
    Map<Long, UserInfo> idUserMap = OneToOne.of(UserInfo::getId).in(userIds).query();
```

[更多使用姿势-Database](/docs/module/plugin/mybatis-plus/database)

[更多使用姿势-One/Many/OneToMany](/docs/module/plugin/mybatis-plus/query?id=one)
