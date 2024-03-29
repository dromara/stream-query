module.exports = {
  root: true,
  env: {
    node: true
  },
  'extends': [
    // 配置参考 https://eslint.vuejs.org/rules/
    'plugin:vue/base',
    'plugin:vue/essential',
    'plugin:vue/strongly-recommended',
    'plugin:vue/recommended'
  ],
  rules: {
    // 以下风格参照 https://cn.vuejs.org/v2/style-guide/index.html
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    // 配置参考 https://eslint.vuejs.org/rules/
    'vue/no-v-html': 'off',
    'vue/array-bracket-spacing': 'error',
    'vue/arrow-spacing': 'error',
    'vue/block-spacing': 'error',
    'vue/camelcase': 'error',
    'vue/comma-dangle': 'error',
    'vue/component-name-in-template-casing': ['error', 'kebab-case', {
      'registeredComponentsOnly': true,
      'ignores': []
    }],
    'vue/eqeqeq': 'error',
    'vue/key-spacing': 'error',
    'vue/match-component-file-name': ['error', {
      'extensions': ['jsx'],
      'shouldMatchCase': false
    }],
    'vue/no-restricted-syntax': 'error',
    'vue/object-curly-spacing': 'error',
    'vue/require-direct-export': 'error',
    'vue/space-infix-ops': 'error',
    'vue/space-unary-ops': 'error',
    'vue/v-on-function-call': ['error', 'never']
  },
  parserOptions: {
    parser: 'babel-eslint'
  }
}
