1. 使用license-eye完善开源许可证，罗列相关LICENSES信息
2. 添加CLA(Contributor License Agreements)
3. 新增TreeHelper#cascadeSelect，类似前端级联选择器功能
4. 升级MyBatis-Plus版本至3.5.6
5. 升级Byte-Buddy版本至1.14.12
6. 新增Github Action代替Gitee Go进行CI，目前支持代码风检格检查、许可证检查、JDK8/JDK17场景下的H2/MySQL/PostGreSQL数据库分别进行测试
7. 移除JsonFieldHandler，因为MyBatis-Plus已内置
8. 加ClassHelper#cast用于类型转换，并替换掉原SerFunc#cast
