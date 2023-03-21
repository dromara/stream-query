## DynamicMapperHandler

> 动态映射处理器，使用之后可实现无需Mapper即可完成crud操作，推荐使用stream-query内置方法进行操作

### 使用方法(推荐)

#### 1、新建配置类SQConfig

#### 2、将DynamicMapperHandler放入Spring Bean中

#### 3、在yml配置中加入一个自定义配置值为po所在包因为会根据po动态生成对应的Mapper

```yaml
pojoPackageScan: com.ciao.blog.entity
```

#### 4、在配置类中获取

```java
@EnableMybatisPlusPlugin
@Configuration
public class SQConfig {

    @Value("${pojoPackageScan}")
    private String entityPackagePath;
    @Bean
    public DynamicMapperHandler dynamicMapperHandler(SqlSessionFactory sqlSessionFactory) throws Exception {
        // 使用ClassHelper的scanClasses方法扫描对应路径下的po生成Class文件集合放入第二个参数就可以了
        final List<Class<?>> entityClassList = ClassHelper.scanClasses(entityPackagePath);
        return new DynamicMapperHandler(sqlSessionFactory, entityClassList);
    }

}
```

#### 5、再次启动项目即可实现动态加载Mapper