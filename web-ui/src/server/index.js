const env = require('./env');
const express = require('express');
const path = require('path');
const app = express();

/**
 * Use webpack middleware just in development
 */
if(process.env.NODE_ENV === 'development') {
  const webpackDevMiddleware = require('webpack-dev-middleware');
  const webpack = require('webpack');
  const webpackConfig = require('../../webpack.config');
  var compiler = webpack(webpackConfig);

  app.use(webpackDevMiddleware(compiler, {
    publicPath: '/assets/',
    stats: { colors: true }
  }));
}

/**
 * Serve static assets
 */
app.use('/assets/', express.static(path.resolve(__dirname, '../client/public/assets')));


/**
 * Main entry point
 */
app.get('/', function (req, res) {
  res.sendFile(path.resolve(__dirname, '../client/public/index.html'));
});

/**
 * Serve /env.js
 * Mimic process.env on the client side
 */
app.get('/env.js', (req, res)  => {
  const js = `window.process = {}; window.process.env = ${JSON.stringify(env.public())}`;
  res.setHeader('content-type', 'text/javascript');
  res.write(js);
  res.end();

});

/**
 * Start listening for connections
 */
app.listen(env.PORT, function () {
  console.log(`[server] listening on port ${env.PORT}`);
});

