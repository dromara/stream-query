<h1 align="center">stream-query</h1>
<p align="center">
  <strong>hardcore extreme opinionated.</strong>
</p>
<p align="center">
	👉 <a href="https://vampireachao.gitee.io/stream-query-docs/#/">stream-query</a> 👈
</p>
<p align="center">
    <a target="_blank" href="https://search.maven.org/artifact/io.github.vampireachao/stream-query">
        <img src="https://img.shields.io/maven-central/v/io.github.vampireachao/stream-query.svg?label=Maven%20Central" />
    </a>
    <a target="_blank" href='https://www.apache.org/licenses/LICENSE-2.0.html'>
        <img src='https://img.shields.io/badge/license-Apache%202-4EB1BA.svg'/>
    </a>	
    <a target="_blank" href='https://gitee.com/VampireAchao/stream-query'>
        <img src='https://gitee.com/vampireachao/stream-query/badge/star.svg' alt='star'/>
    </a>
    <a target="_blank" href='https://github.com/VampireAchao/stream-query'>
        <img src="https://img.shields.io/github/stars/vampireachao/stream-query.svg?style=social" alt="github star"/>
    </a>
</p>

## 📚简介

封装 热门orm常用操作
封装 使用`stream`进行数据返回处理

## 📝文档

[中文文档](https://vampireachao.gitee.io/stream-query-docs/)&nbsp;  &nbsp;[仓库地址](https://gitee.com/VampireAchao/stream-query-docs)
## 📦安装

### 🍊Maven

在项目的pom.xml的dependencies中加入以下内容:

```xml
<!-- https://mvnrepository.com/artifact/io.github.vampireachao/stream-plugin-mybatis-plus -->
<dependency>
    <groupId>io.github.vampireachao</groupId>
    <artifactId>stream-plugin-mybatis-plus</artifactId>
    <version>1.1.6</version>
</dependency>
<!-- https://mvnrepository.com/artifact/io.github.vampireachao/stream-core -->
<dependency>
    <groupId>io.github.vampireachao</groupId>
    <artifactId>stream-core</artifactId>
    <version>1.1.6</version>
</dependency>
```
### 🍊Gradle

在项目的build.gradle的dependencies中加入以下内容:
```Gradle
implementation group: 'io.github.vampireachao', name: 'stream-core', version: '1.1.6'
```


## 🐞提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本、stream-query版本和相关依赖库版本。如果可以请尽量详细或加图片以便于我们去复现

[Gitee issue](https://gitee.com/VampireAchao/stream-query/issues)<br/>
[Github issue](https://github.com/VampireAchao/stream-query/issues)

## 🏗️添砖加瓦️
如果您感觉我们的代码有需要优化的地方或者有更好的方案欢迎随时提pr
### 📚包说明
| 包名            | 内容                       |
|---------------|--------------------------|
| stream-query  | 对Optional的优化和对Stream流的封装 |
| stream-plugin | 对复杂的CRUD进行封装             |

### 🐾贡献代码的步骤
1. 在`Gitee`或者`Github`上`fork`项目到自己的`repo`
2. 把`fork`过去的项目也就是你的项目`clone`到你的本地
3. 修改代码
4. `commit`后`push`到自己的库
5. 登录`Gitee`或`Github`在你仓库首页可以看到一个 `pull request` 按钮，点击它，填写一些说明信息，然后提交即可。
   等待维护者合并

### 📐PR遵照的原则
`stream-query`欢迎任何人为`stream-query`添砖加瓦，贡献代码，不过维护者是一个强迫症患者，为了照顾病人，需要提交的pr（pull request）符合一些规范，规范如下：

- 注释完备，尤其每个新增的方法应按照Java文档规范标明方法说明、参数说明、返回值说明等信息，必要时请添加单元测试，如果愿意，也可以加上你的大名。
- 新加的方法不要使用额外的第三方库方法
- 我们如果关闭了你的issue或pr，请不要诧异，这是我们保持问题处理整洁的一种方式，你依旧可以继续讨论，当有讨论结果时我们会重新打开。

powered by [GitHub Copilot](https://copilot.github.com/)   
