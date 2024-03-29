const path = require('path')

module.exports = (options = {}, context) => ({
  define () {
    const { siteConfig = {}} = context
    const hm = options.hm || siteConfig.hm
    const HM_ID = hm || false
    return { HM_ID }
  },

  enhanceAppFiles: [
    path.resolve(__dirname, 'inject.js')
  ]
})
