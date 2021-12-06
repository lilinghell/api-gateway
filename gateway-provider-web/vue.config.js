const path = require('path');
const apiMocker = require('mocker-api');

module.exports = {
  configureWebpack: (config) => {
    if (process.env.NODE_ENV === 'production') {// 为生产环境修改配置...
      config.mode = 'production';
      config["performance"] = {//打包文件大小配置
        "maxEntrypointSize": 10000000,
        "maxAssetSize": 30000000
      }
    }
  },
  lintOnSave: process.env.NODE_ENV !== 'production',
  pluginOptions: {
    quasar: {
      theme: 'mat',
      importAll: true
    }
  },
  transpileDependencies: [
    'vue-echarts',
    'resize-detector',
    /[\\/]node_modules[\\/]quasar[\\/]/
  ],
  devServer: {
    overlay: {
      errors: true
    },
    // before(app) {
    //   // unnecessary fill in all mock files
    //   apiMocker(app, path.resolve('./mock/index.js'));
    // }
    proxy: "http://127.0.0.1:8090/",
  },
  // publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
  publicPath: '/gateway/mweb',
  css: {
    loaderOptions: {
      sass: {
        // additionalData: `@import "~@/styles/quasar.sass";`
      }
    }
  }
};
