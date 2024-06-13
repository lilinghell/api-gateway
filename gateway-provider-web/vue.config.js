const path = require('path');
const apiMocker = require('mocker-api');
// let Version = "2022091600001";
let Version = new Date().getTime();
module.exports = {
  configureWebpack: (config) => {
    //config.output.chunkFilename = 'js/[name].[' + Version + '].js' //这种方式适合设备缓存不严重的
    config.output.filename = 'js/[name].js?v=' + Version    //这种是给打包后的chunk文件加版本号。
    config.output.chunkFilename = 'js/[name].js?v=' + Version
    // if (process.env.NODE_ENV === 'production') {// 为生产环境修改配置...
    //   config.mode = 'production';
    //   config["performance"] = {//打包文件大小配置
    //     "maxEntrypointSize": 10000000,
    //     "maxAssetSize": 30000000
    //   }
    // }
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
    proxy: "http://127.0.0.1:8190/",
  },
  // publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
  publicPath: '/gateway/mweb',
  // filenameHashing: false,
  css: {
    // loaderOptions: {
    //   sass: {
    //     // additionalData: `@import "~@/styles/quasar.sass";`
    //   }
    // }
    extract: {
      //一种方式，打包后的css 会带版本号，不改变文件名的。
      filename: 'css/[name].css?v=' + Version,
      chunkFilename: 'css/[name].css?v=' + Version,
      //一种方式，每次打包后的css文件名会变更新。
      //filename: 'css/[name].[' + Version + '].css',
      //chunkFilename: 'css/[name].[' + Version + '].css'
    }
  }
};
