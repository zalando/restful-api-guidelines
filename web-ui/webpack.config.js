'use strict';

const env = require('./src/server/env');
const webpack = require('webpack');
const path = require('path');
const pkg = require('./package.json');

const BUILD_DIR = path.resolve(__dirname, 'src/client/public/assets');
const APP_DIR = path.resolve(__dirname, 'src/client/app');

const config = {
  entry: APP_DIR + '/index.jsx',
  output: {
    path: BUILD_DIR,
    filename: 'bundle.js'
  },
  module : {
    rules : [
      {
        test : /\.jsx?/,
        include : APP_DIR,
        loader : 'babel-loader'
      },
      {
        test: /\.css$/,
        use: [ 'style-loader', 'css-loader' ]
      }
    ]
  },
  plugins: [
    new webpack.BannerPlugin(`
@name ${pkg.name}
@version ${pkg.version}
@license ${pkg.license}
    `)
  ],
  devtool: 'source-map',
  devServer: {
    https: env.DEV_SSL_ENABLED,
    port: env.DEV_PORT,
    publicPath: '/assets/'
  }
};

module.exports = config;
