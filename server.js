const app = require('./src/server')();
const webpackDevServerProxy = require('./src/server/webpack-dev-server-proxy');

/**
 * Proxy to webpack-dev-server for development
 */
if (process.env.NODE_ENV === 'development') {
  process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';
  webpackDevServerProxy(app, require('./webpack.config'));
}

app.listen(3000, () => {
  console.log('zally-web-ui running at http://localhost:3000');
});
