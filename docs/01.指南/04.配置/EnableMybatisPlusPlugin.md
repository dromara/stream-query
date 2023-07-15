---
title: 动态Mapper
date: 2023-07-15 14:20:00
article: false
permalink: /pages/30c2c9/
---
### 概述

该文档描述了一个动态Mapper注解的作用和用法。该注解用于在Java代码中标记特定的实体类，并为其提供动态Mapper，无需再为每一个实体类创建Mapper接口。
### [@EnableMybatisPlusPlugin](#enablemybatisplusplugin)

- 描述：指定实体类生成动态Mapper接口
- 使用位置：配置类

```java
@SpringBootApplication
@EnableMybatisPlusPlugin
public class MybatisPlusTestApplication {}
```

### 使用示例

#### 指定package

**basePackages** 指定需要增强的实体类的包路径，支持 ***** 通配符 

```java
@EnableMybatisPlusPlugin(basePackages = "org.dromara.streamquery.stream.plugin.mybatisplus.annotation.pojo.*")
public class EnableMybatisPlusPluginByBasePackagesTest {}
```

#### 指定注解

在需要增强的实体类上添加 **annotation** 属性指定的注解

```java
@EnableMybatisPlusPlugin(annotation = GenerateMapper.class)
public class EnableMybatisPlusPluginByAnnotationTest {}
```

```java
@Data
@GenerateMapper
public class Student {
 	private String name;
 	private int age;
 }
```

#### 指定接口

在需要增强的实体类上实现 **interfaceClass** 属性指定的接口

```java
@EnableMybatisPlusPlugin(interfaceClass = IGenerateMapper.class)
public class EnableMybatisPlusPluginByInterfaceClassTest {}
```

```java
@Data
public class Student implements IGenerateMapper {
 	private String name;
 	private int age;
 }
```

#### 指定实体类

**classes** 属性指定需要增强的实体类

```java
@EnableMybatisPlusPlugin(classes = {Student.class})
public class EnableMybatisPlusPluginByClassesTest {}
```

