# [vuepress-baidu-tongji](https://github.com/mlinquan/vuepress-baidu-tongji)

> Baitu tongji plugin for vuepress

## Install

```bash
yarn add -D vuepress-baidu-tongji
# OR npm install -D vuepress-baidu-tongji
```

## Usage

```javascript
module.exports = {
  plugins: ['vuepress-baidu-tongji', {
    hm: 'abcdefghijklmnopqrstuvwxyz123456'
  }]
}
```

## Options

### hm

- Type: `string`
- Default: `undefined`

Provide the Baidu Tongji ID to enable integration.
