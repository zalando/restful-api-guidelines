const env = require('./env');
const express = require('express');
const path = require('path');
const app = express();
const createHttpServer = require('./create-http-server');
const ASSETS_DIR = path.resolve(__dirname, '../client/public/assets');

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
app.use('/assets/', express.static(ASSETS_DIR));


/**
 * Main entry point
 */
app.get('/', (req, res) => {
  res.sendFile(path.resolve(__dirname, '../client/public/index.html'));
});

/**
 * Serve /env.js
 * Mimic process.env on the client side
 */
app.get('/env.js', require('./env-handler'));


/**
 * Serve favicon.ico
 */
app.get('/favicon.ico',(req, res) => {
  res.sendFile(path.join(ASSETS_DIR, 'favicon.ico'));
});


/**
 * Proxy tokeninfo to avoid CORS restriction
 */
app.use('/tokeninfo', require('./tokeninfo-handler'));

/**
 * Proxy zally api to avoid CORS restriction
 */
app.use('/zally-api', require('./zally-api-handler'));


/**
 * Start listening for connections
 */
createHttpServer(app).listen(env.PORT, () => {
  console.log(`[server] listening on port ${env.PORT}, NODE_ENV=${env.NODE_ENV}`);
});

