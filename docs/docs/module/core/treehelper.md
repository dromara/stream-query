# TreeHelper

> 在1.1.17版本中我们将树先生分离了出来不再依赖于Steam，而且用起来更佳方便</br>
> 如果想了解源码但是看不太懂的话可以查看这篇文章-> [关于树先生我们是这样做的](https://zverify.cn/cizai/befed892.html)

## 此文档使用前置数据声明

```java
List<Student> originStudentList; ->
        Student.builder().id(1L).name("dromara").matchParent(true).build(),
        Student.builder().id(2L).name("baomidou").matchParent(true).build(),
        Student.builder().id(3L).name("hutool").parentId(1L).build(),
        Student.builder().id(4L).name("sa-token").parentId(1L).build(),
        Student.builder().id(5L).name("mybatis-plus").parentId(2L).build(),
        Student.builder().id(6L).name("looly").parentId(3L).build(),
        Student.builder().id(7L).name("click33").parentId(4L).build(),
        Student.builder().id(8L).name("jobob").parentId(5L).build()
List<Student> originStudentTree; ->
        Student.builder().id(1L).name("dromara").matchParent(true)
            .children(asList(
                Student.builder().id(3L).name("hutool").parentId(1L)
                    .children(singletonList(Student.builder().id(6L).name("looly").parentId(3L).build()))
                    .build(),
                Student.builder().id(4L).name("sa-token").parentId(1L)
                    .children(singletonList(Student.builder().id(7L).name("click33").parentId(4L).build()))
                    .build()))
        .build(),
        Student.builder().id(2L).name("baomidou").matchParent(true)
            .children(singletonList(
                Student.builder().id(5L).name("mybatis-plus").parentId(2L)
                .children(singletonList(
                    Student.builder().id(8L).name("jobob").parentId(5L).build()
                ))
                .build()))
        .build()
TreeHelper<Student, Long> studentTreeHelper;
// Student属性
private String name;
private Integer age;
private Long id;
private Long parentId;
private List<Student> children;
private Boolean matchParent;
```

# 使用讲解

## of

> 构建树先生来帮我们操作</br>
> 参数说明：节点id,父节点id,父节点id值,获取子节点,操作子节点

```java
studentTreeHelper = TreeHelper.of(Student::getId, Student::getParentId, null, Student::getChildren, Student::setChildren);
```

> 通过以上操作可以构建我们的树先生出来帮助我们做一些操作了是不是操作很简单呢
> 接下来看一些树先生可以帮你做的事情

## toTree

> 传入List集合通过创建树先生时所传入信息去构造树结构

```java
List<Student> studentTree = studentTreeHelper.toTree(originStudentList);
```

## flat

> 将树结构进行扁平化

```java
List<Student> studentList = studentTreeHelper.flat(originStudentTree);
```

## filter

> 根据给定的条件过滤列表中的元素，并且递归过滤子元素列表

```java
List<Student> studentTree = studentTreeHelper.filter(originStudentTree, s -> "looly".equals(s.getName()));
/* 这样我们把包含这个果实的整个树枝都拿到了就,此时studentTree为 —>
        Student.builder().id(1L).name("dromara").matchParent(true)
            .children(singletonList(Student.builder().id(3L).name("hutool").parentId(1L)
                .children(singletonList(Student.builder().id(6L).name("looly").parentId(3L).build()))
                .build()))
            .build())
*/
```

## forEach

> 对列表中的元素以及它们的子元素列表进行递归遍历，并在每个元素上执行给定的操作

```java
List<Student> studentList = studentTreeHelper.forEach(originStudentTree, s -> s.setName("【open source】" + s.getName()));
```


