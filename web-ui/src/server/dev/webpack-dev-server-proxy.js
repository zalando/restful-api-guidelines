'use strict';

const webpackDevServerProxyHandler = require('./webpack-dev-server-proxy-handler');

/**
 * @param app - express app
 * @param webpackConfig - webpack configuration object
 */
function webpackDevServerProxy (app, webpackConfig = { devServer: {} }) {

  const {devServer} = webpackConfig;
  const {protocol, host, port, publicPath} = Object.assign({}, webpackDevServerProxy.DEFAULTS, devServer, {
    protocol: devServer.https ? 'https' : 'http'
  });

  app.use(publicPath, webpackDevServerProxyHandler({
    publicPath, protocol, host, port
  }));
}

// webpack-dev-server defaults
webpackDevServerProxy.DEFAULTS = {
  protocol: 'http',
  host: 'localhost',
  port: 8080,
  publicPath: '/'
};

module.exports = webpackDevServerProxy;
