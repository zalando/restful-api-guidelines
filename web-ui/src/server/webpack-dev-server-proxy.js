'use strict';

const request = require('request');
const path = require('path');
const logger = require('./logger');

// webpack-dev-server defaults
const defaults = {
  protocol: 'http',
  host: 'localhost',
  port: 8080,
  publicPath: '/'
};

/**
 * @param app - express app
 * @param webpackConfig - webpack configuration object
 */
module.exports = function (app, webpackConfig = {}) {

  const {devServer} = webpackConfig;
  const {protocol, host, port, publicPath} = Object.assign({}, defaults, devServer, {
    protocol: devServer.https ? 'https' : 'http'
  });

  app.use(publicPath, (req, res) => {
    const segment = path.join(publicPath, req.url);
    const url = `${protocol}://${host}:${port}${segment}`;
    logger.debug(`Proxying request to webpack dev server: ${url}`);
    req.pipe(request(url)).pipe(res);
  });
};

