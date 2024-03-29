# vuepress-plugin-thirdparty-search

此插件是基于官方的搜索框插件 [@vuepress/plugin-search](https://github.com/vuejs/vuepress/tree/master/packages/@vuepress/plugin-search) 做了功能添加，添加的功能如下：

* 在搜索结果最底部添加第三方搜索链接



### 使用

安装

```sh
npm i vuepress-plugin-thirdparty-search -D
```

在`.vuepress/config.js`的`plugins`选项添加插件，并配置第三方搜索链接：

```js
// .vuepress/config.js
module.exports = {
  plugins: [
    ['thirdparty-search', {
      thirdparty: [ // 可选，默认 []
        {
          title: '在MDN中搜索', // 在搜索结果显示的文字
          frontUrl: 'https://developer.mozilla.org/zh-CN/search?q=', // 搜索链接的前面部分
          behindUrl: '' // 搜索链接的后面部分，可选，默认 ''
        },
        {
          title: '在Runoob中搜索',
          frontUrl: 'https://www.runoob.com/?s=',
        },
        {
          title: '在Vue API中搜索',
          frontUrl: 'https://cn.vuejs.org/v2/api/#',
        }
      ]
    }], 
  ]
}
```

* 此插件会覆盖原本官方的 `plugin-search` 插件，但**原插件的参数选项仍适用**。更多配置请查看：[plugin-search](https://v1.vuepress.vuejs.org/plugin/official/plugin-search.html)

* 此插件在官方默认主题也适用。

