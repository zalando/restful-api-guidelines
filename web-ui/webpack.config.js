'use strict';

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
      }
    ]
  },
  plugins: [
    new webpack.BannerPlugin(`
@name ${pkg.name}
@version ${pkg.version}
@license ${pkg.license}
    `)
  ]
};

module.exports = config;
