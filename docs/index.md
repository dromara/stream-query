---
home: true
heroImage: https://plus.hutool.cn/images/dromara/stream-query.png
heroText: 
tagline: 允许完全摆脱Mapper的mybatis-plus🌽体验！可以使用类似“工具类”🔧这样的静态函数进行数据库操作
actionText: 开始使用 →
actionLink: /pages/a2f161/
bannerBg: none # auto => 网格纹背景(有bodyBgImg时无背景)，默认 | none => 无 | '大图地址' | background: 自定义背景样式       提示：如发现文本颜色不适应你的背景时可以到palette.styl修改$bannerTextColor变量

features: # 可选的
  - title: 动态Mapper
    details: 允许完全摆脱Mapper的Mybatis-Plus体验！使用Byte-Buddy运行时生成Mapper
  - title: 查询后Stream处理封装
    details: 一对一、一对多等，轻松完成多次单表所需数据查询
  - title: 简化Stream操作
    details: 提供一个Steam类，Steam等于Stream减去R(Repeat)，减少Stream重复代码

# 文章列表显示方式: detailed 默认，显示详细版文章列表（包括作者、分类、标签、摘要、分页等）| simple => 显示简约版文章列表（仅标题和日期）| none 不显示文章列表
postList: none
---
<p align="center">
  <a class="become-sponsor" href="/pages/1b12ed/">支持这个项目</a>
</p>

<style>
.become-sponsor {
  padding: 8px 20px;
  display: inline-block;
  color: #11a8cd;
  border-radius: 30px;
  box-sizing: border-box;
  border: 1px solid #11a8cd;
}
.repo-link::before {
    content: 'Gitee';
    font-size: initial;
}
.repo-link{
    font-size: 0;
}
.repo-link>span{
    position: relative;
    bottom: 4px;
}
</style>

<br/>
<p align="center">
  <a href="https://search.maven.org/artifact/org.dromara/stream-query" target="_blank"><img src="https://img.shields.io/maven-central/v/org.dromara.stream-query/stream-query.svg?label=Maven%20Central" alt="maven" class="no-zoom"></a>
  <a href="https://gitee.com/dromara/stream-query" target="_blank"><img src='https://gitee.com/dromara/stream-query/badge/star.svg' alt='Gitee stars' class="no-zoom"></a>
  <a href="https://gitee.com/dromara/stream-query" target="_blank"><img src='https://img.shields.io/github/stars/dromara/stream-query.svg?style=social' alt='GitHub stars' class="no-zoom"></a>
</p>

<br/>
<!-- <p align="center" style="color: #999;">
  赞助商（招募中）
</p>
<p align="center">
  <a href="#" target="_blank"><img :src="$withBase('/img/gif/洛琪希哭唧唧.gif')" alt="sponsor" class="no-zoom" style="height: 150px;border-radius: 2px;"></a>
</p>
-->
<a target="_blank" href="https://www.xiaonuo.vip/?from=hutool">
  <img src="https://plus.hutool.cn/images/ad/xiaonuo_banner.jpg" />
</a>

<a target="_blank" href="https://www.jnpfsoft.com/index.html?from=stream-query">
  <img src="/img/sponsor/yinmaisoft.jpg" />
</a>

## ⛄开发团队成员
::: cardList 3
```yaml
- name: 阿超
  desc: '- 实现动态Mapper<br>- 编写Steam类'
  avatar: /img/commiter/VampireAchao.jpg # 头像，可选
  link: https://gitee.com/VampireAchao # 链接，可选
  bgColor: '#32040d' # 背景色，可选，默认var(--bodyBg)。颜色值有#号时请添加引号
  textColor: '#d7dadc' # 文本色，可选，默认var(--textColor)
- name: 臧臧
  desc: '- 维护Core模块<br>- 文档编写'
  avatar: /img/commiter/Eliauk.jpeg # 头像，可选
  link: https://gitee.com/EliaukU # 链接，可选
  bgColor: '#35171b' # 背景色，可选，默认var(--bodyBg)。颜色值有#号时请添加引号
  textColor: '#edcac4' # 文本色，可选，默认var(--textColor)
- name: KamToHung
  desc: '- spring配置方式优化'
  avatar: /img/commiter/KamToHung.png # 头像，可选
  link: https://gitee.com/KamToHung # 链接，可选
  bgColor: '#cb4b41' # 背景色，可选，默认var(--bodyBg)。颜色值有#号时请添加引号
  textColor: '#f4f4fc' # 文本色，可选，默认var(--textColor)
- name: Cason
  desc: '- 实现动态Mapper<br>- 适配solon框架'
  avatar: /img/commiter/Cason.jpg # 头像，可选
  link: https://gitee.com/Casonhqc # 链接，可选
  bgColor: '#40f2ff' # 背景色，可选，默认var(--bodyBg)。颜色值有#号时请添加引号
  textColor: '#f4f4fc' # 文本色，可选，默认var(--textColor)
```
:::

## 🎖[Dromara](https://dromara.org/)组织项目
::: cardList 4
```yaml
#- name: 名称
#  desc: 描述
#  avatar: https://xxx.jpg # 头像，可选
#  link: https://xxx/ # 链接，可选
#  bgColor: '#CBEAFA' # 背景色，可选，默认var(--bodyBg)。颜色值有#号时请添加引号
#  textColor: '#6854A1' # 文本色，可选，默认var(--textColor)
- name: koalas-rpc
  desc: 企业生产级百亿日PV高可用可拓展的RPC框架。
  link: https://dromara.org
- name: sureness
  desc: 面向REST API的高性能认证鉴权框架。
  link: https://usthe.com/sureness
- name: Sa-Token
  desc: 一个轻量级 java 权限认证框架，让鉴权变得简单、优雅！
  link: https://sa-token.cc
- name: Jpom
  desc: 一款简而轻的低侵入式在线构建、自动部署、日常运维、项目监控软件。
  link: https://jpom.top
- name: cubic
  desc: 无侵入分布式监控，致力于应用级监控，帮助开发人员快速定位问题。
  link: https://cubic.jiagoujishu.com
- name: MaxKey
  desc: 业界领先的IAM身份管理和认证产品。
  link: https://www.maxkey.top
- name: hutool
  desc: 一个使Java保持甜美的工具类库。
  link: https://hutool.cn
- name: Forest
  desc: 高层的、极简的轻量级HTTP调用API框架。
  link: https://forest.dtflyx.com
- name: TLog
  desc: 轻量级的分布式日志标记追踪神器。
  link: https://tlog.yomahub.com
- name: LiteFlow
  desc: 一个轻量，快速的组件式流程引擎框架。
  link: https://liteflow.yomahub.com
- name: Myth
  desc: 可靠消息分布式事务解决方案。
  link: https://dromara.org
- name: Raincat
  desc: 强一致性分布式事务解决方案。
  link: https://dromara.org
- name: Hmily
  desc: 高性能一站式分布式事务解决方案。
  link: https://dromara.org
- name: stream-query
  desc: 允许完全摆脱Mapper的mybatis-plus体验！
  link: https://dromara.gitee.io/stream-query/
- name: J2EEFAST
  desc: J2eeFAST 是一个 Java EE 企业级快速开发平台。
  link: https://www.j2eefast.com/
- name: data-compare
  desc: 数据库比对工具：hive 表数据比对，mysql、Doris 数据比对。
  link: https://github.com/dromara/dataCompare
- name: payment-spring-boot
  desc: 最全最好用的微信支付V3 Spring Boot 组件。
  link: https://felord.gitee.io/payment-spring-boot/
- name: zyplayer-doc
  desc: 一款适合团队和个人私有化部署使用的知识库、笔记、WIKI文档管理工具。
  link: http://doc.zyplayer.com/
- name: ChatGPT
  desc: 支持ChatGPT在JetBrains系列IDE上运行的一款插件。
  link: https://chatgpt.cn.obiscr.com/
- name: Neutrino-Proxy
  desc: 一个基于netty的、开源的java内网穿透项目。
  link: https://gitee.com/dromara/neutrino-proxy
- name: EasyTrans
  desc: 一个注解搞定数据翻译,减少30%SQL代码量。
  link: http://easy-trans.fhs-opensource.top/
- name: open-capacity-platform
  desc: 基于Spring Cloud的企业级微服务框架。
  link: https://dromara.org
- name: electron-egg
  desc: 一个入门简单、跨平台、企业级桌面软件开发框架。
  link: https://www.yuque.com/u34495/mivcfg
- name: RedisFront
  desc: 一款开源免费的跨平台 Redis 桌面客户端工具
  link: https://www.redisfront.com/
- name: lamp-cloud
  desc: 基于Jdk11 + SpringCloud + SpringBoot 的微服务快速开发平台，其中的可配置的SaaS功能尤其闪耀
  link: https://tangyh.top
- name: go-view
  desc: GoView 是一个高效的拖拽式低代码数据可视化开发平台。
  link: https://www.mtruning.club/#/
- name: dante-cloud
  desc: 企业级技术中台微服务架构与服务能力开发平台
  link: https://www.herodotus.cn
- name: x-easypdf
  desc: 一个用搭积木的方式构建pdf的框架（基于pdfbox）
  link: http://www.x-easypdf.cn/
- name: gobrs-async
  desc: 一款功能强大、配置灵活、带有全链路异常回调、内存优化、异常状态管理于一身的高性能异步编排框架
  link: https://async.sizegang.cn
- name: mendmix
  desc: java企业级应用开发套件，定位是一站式分布式开发架构开源解决方案及云原生架构技术底座
  link: https://www.jeesuite.com
- name: dynamic-tp
  desc: 轻量级，基于配置中心实现对运行中线程池参数的动态修改，以及实时监控线程池
  link: https://dynamictp.cn
- name: easy-es
  desc: 一款简化ElasticSearch搜索引擎操作的开源框架,简化CRUD操作,可以更好的帮助开发者减轻开发负担。
  link: https://easy-es.cn
- name: hertzbeat
  desc: 一个拥有强大自定义监控能力，无需Agent的实时监控系统。
  link: https://hertzbeat.com
- name: open-giteye-api
  desc: 专为开源作者设计的数据图表服务工具类站点，提供了包括Star趋势图、贡献者列表、Gitee指数等数据图表服务。
  link: https://giteye.net
- name: fast-request
  desc: IDEA httpClient插件。
  link: https://dromara.gitee.io/fast-request
- name: northstar
  desc: 可替代付费商业软件的一站式量化交易平台。
  link: https://www.quantit.tech
- name: image-combiner
  desc: 专门用于图片合成的工具。
  link: https://dromara.org
```
:::

## 🎖[aizuda](http://doc.aizuda.com/)组织项目
::: cardList 4
```yaml
#- name: 名称
#  desc: 描述
#  avatar: https://xxx.jpg # 头像，可选
#  link: https://xxx/ # 链接，可选
#  bgColor: '#CBEAFA' # 背景色，可选，默认var(--bodyBg)。颜色值有#号时请添加引号
#  textColor: '#6854A1' # 文本色，可选，默认var(--textColor)
- name: mybatis-plus
  desc: 🚀为简化开发而生
  link: https://baomidou.com/
- name: Easy-Security
  desc: 简单易用的鉴权框架 只需四步完成使用
  link: https://easy-security.aizuda.com/

```
:::

<!-- <object type="image/svg+xml" data="/img/projects.svg"></object> -->

<br/>

## 🎉上新推荐
* `v2.0.2`
  - 升级mybatis-plus到3.5.4
  - 适配JDK17
  - BeanHelper.copyProperties支持lombok的@Accessors(chain = true)
  - 调整SerPred#isEqual方法参数为单个
  - 解决SimpleName一样的实体类导致只存在一个Mapper的问题，感谢@huang-up

更多上新请查阅：[**更新日志**](https://gitee.com/dromara/stream-query/releases)

<br/>

<!-- ## ⚡️未来...

::: tip
期待 [支持JDK17](https://gitee.com/dromara/stream-query/issues/I6SE3B)
期待 [支持MongoDB](https://gitee.com/dromara/stream-query/issues/I6OSE5)

一起来完善这个项目吧~
::: -->

<br/>

## ⚡ 反馈与交流

在使用过程中有任何问题和想法，请给我提 [Issue](https://gitee.com/dromara/stream-query/issues)。
你也可以在Issue查看别人提的问题和给出解决方案。

或者加入我们的交流群：

<table>
  <tbody>
    <tr>
      <td align="center" valign="middle">
        <img :src="$withBase('/img/qrcode/rubenachao.jpg')" alt="微信" class="no-zoom" style="width:300px;margin: 10px;">
        <p>Stream-Query微信群(添加我微信备注"进群")</p>
      </td>
      <td align="center" valign="middle">
        <img :src="$withBase('/img/qrcode/knowledge-planet.jpg')" alt="知识星球" class="no-zoom" style="width:300px;margin: 10px;">
        <p>Dromara知识星球</p>
      </td>
    </tr>
  </tbody>
</table>

## 其他

* 文档采取[vuepress-theme-vdoing](http://stream-query.dromara.org/)搭建
* 致谢[JetBrains](https://www.jetbrains.com/?from=stream-query)提供的免费开源许可证
* 致谢[Github Copilot](https://copilot.github.com/)提供的免费开源许可证
* 本项目遵循[Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0.html)协议

<!-- AD -->
<div class="wwads-cn wwads-horizontal page-wwads" data-id="136"></div>
<style>
  .page-wwads{
    width:100%!important;
    min-height: 0;
    margin: 0;
  }
  .page-wwads .wwads-img img{
    width:80px!important;
  }
  .page-wwads .wwads-poweredby{
    width: 40px;
    position: absolute;
    right: 25px;
    bottom: 3px;
  }
  .wwads-content .wwads-text, .page-wwads .wwads-text{
    height: 100%;
    padding-top: 5px;
    display: block;
  }
</style>
