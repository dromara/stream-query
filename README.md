<h1 align="center">stream-query</h1>
<p align="center">
  <strong>hardcore extreme opinionated.</strong>
</p>
<p align="center">
	👉 <a href="http://stream-query.dromara.org/">stream-query</a> 👈
</p>
<p align="center">
    <a target="_blank" href="https://search.maven.org/artifact/org.dromara.stream-query/stream-query">
        <img src="https://img.shields.io/maven-central/v/org.dromara.stream-query/stream-query.svg?label=Maven%20Central" />
    </a>
    <a target="_blank" href='https://www.apache.org/licenses/LICENSE-2.0.html'>
        <img src='https://img.shields.io/badge/license-Apache%202-4EB1BA.svg'/>
    </a>	
    <a target="_blank" href='https://gitee.com/dromara/stream-query'>
        <img src='https://gitee.com/dromara/stream-query/badge/star.svg' alt='star'/>
    </a>
    <a target="_blank" href='https://github.com/dromara/stream-query'>
        <img src="https://img.shields.io/github/stars/dromara/stream-query.svg?style=social" alt="github star"/>
    </a>
</p> 

## 📚简介

允许完全摆脱`Mapper`的`mybatis-plus`体验！

封装`stream`和`lambda`操作进行数据返回处理

## 🍺Committer卑微求职 北京、雄安求内推
v：Cizai_

<img src="https://zang-1307996497.cos.ap-beijing.myqcloud.com/undefinedWechatIMG379.jpg" alt="微信">
## 📝文档

[中文文档](http://stream-query.dromara.org)
&nbsp;[仓库地址](https://gitee.com/dromara/stream-query)
&nbsp;[视频教程](https://www.bilibili.com/video/BV1UP411F7Ai)

## 📦安装

### 🍊Maven

在项目的pom.xml的dependencies中加入以下内容:

```xml
<!-- 已包含mybatis-plus、stream-core、不用重复引入 -->
<!-- https://mvnrepository.com/artifact/org.dromara.stream-query/stream-plugin-mybatis-plus -->
<dependency>
   <groupId>org.dromara.stream-query</groupId>
   <artifactId>stream-plugin-mybatis-plus</artifactId>
   <version>2.1.0</version>
</dependency>
<!-- 可单独引入 -->
<!-- https://mvnrepository.com/artifact/org.dromara/stream-core -->
<dependency>
  <groupId>org.dromara.stream-query</groupId>
  <artifactId>stream-core</artifactId>
  <version>2.1.0</version>
</dependency>
```

### 🍊Gradle

在项目的build.gradle的dependencies中加入以下内容:

```Gradle
implementation group: 'org.dromara.stream-query', name: 'stream-plugin-mybatis-plus', version: 'x.x.x'
implementation group: 'org.dromara.stream-query', name: 'stream-core', version: 'x.x.x'
```

## 🔧 配置

注入动态`Mapper`处理器

```java
@Bean
public DynamicMapperHandler dynamicMapperHandler(SqlSessionFactory sqlSessionFactory) throws Exception {
    // 使用ClassHelper的scanClasses方法扫描对应路径下的po生成Class文件集合放入第二个参数就可以了
    final List<Class<?>>entityClassList=ClassHelper.scanClasses("com.ruben.pojo.po");
    return new DynamicMapperHandler(sqlSessionFactory,entityClassList);
}
```

## 📚使用

```java
Database.saveBatch(userList);
```

```java
// 批量保存
Database.saveBatch(userList);
// 使用userIds进行in查询，得到map key为id，value为entity对象
Map<Long, UserInfo> idUserMap=OneToOne.of(UserInfo::getId).in(userIds).query();
```

[更多使用姿势-Database](http://stream-query.dromara.org/pages/454a9b/)

[更多使用姿势-One/Many/OneToMany](http://stream-query.dromara.org/pages/4f06cd/)

更多请看[文档](http://stream-query.dromara.org)

## 🐞提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本、stream-query版本和相关依赖库版本。如果可以请尽量详细或加图片以便于我们去复现

[Gitee issue](https://gitee.com/dromara/stream-query/issues)<br/>
[Github issue](https://github.com/dromara/stream-query/issues)

## 🏗️添砖加瓦️

如果您感觉我们的代码有需要优化的地方或者有更好的方案欢迎随时提pr

### 📚包说明

| 包名            | 内容                              |
|---------------|---------------------------------|
| stream-query  | 优雅的流式操作                         |
| stream-plugin | orm框架 -> 使用优雅的Lambda进行对数据库进行一系列操作 |

### 🐾贡献代码的步骤

1. 在`Gitee`或者`Github`上`fork`项目到自己的`repo`
2. 把`fork`过去的项目也就是你的项目`clone`到你的本地
3. 修改代码
4. `commit`后`push`到自己的库
5. 登录`Gitee`或`Github`在你仓库首页可以看到一个 `pull request` 按钮，点击它，填写一些说明信息，然后提交即可。
   等待维护者合并

### 📐PR遵照的原则

`stream-query`欢迎任何人为`stream-query`添砖加瓦，贡献代码，规范如下：

- 注释完备，尤其每个新增的方法应按照Java文档规范标明方法说明、参数说明、返回值说明等信息，必要时请添加单元测试，如果愿意，也可以加上你的大名。
- 新加的方法尽可能不要使用额外的第三方库方法
- 我们如果关闭了你的issue或pr，请不要诧异，这是我们保持问题处理整洁的一种方式，你依旧可以继续讨论，当有讨论结果时我们会重新打开。

powered by [GitHub Copilot](https://copilot.github.com)
powered by [JetBrains](https://www.jetbrains.com)Open source license


## 赞助商

[![xiaonuo](https://plus.hutool.cn/images/ad/xiaonuo_banner.jpg)](https://www.xiaonuo.vip/?from=hutool)
[![yinmaisoft](https://plus.hutool.cn/images/zz/yinmaisoft.jpg)](https://www.jnpfsoft.com/index.html?from=stream-query)
