'use strict';

const express = require('express');
const _ = require('lodash');
const path = require('path');
const ASSETS_DIR = path.resolve(__dirname, '../client/public/assets');
const VIEW_DIR = path.resolve(__dirname, './views');

const DEFAULT_OPTIONS = {
  windowEnv: {
    OAUTH_ENABLED: false,
    DEBUG: true,
    ZALLY_API_URL: 'http://localhost:8080'
  },
  env: {
    HEAD_TITLE: 'Zally API Linter WEB UI'
  },
  logger: console,
  handlers: {
    assets: () => express.static(ASSETS_DIR),
    windowEnv: require('./handlers/window-env'),
    index: require('./handlers/index')
  }
};

module.exports = (options = {}) => {
  const app = express();
  const _options = _.merge({}, DEFAULT_OPTIONS, options);

  app.on('mount', function (/* parent*/) {
    _options.logger.info(`zally-web-ui mounted on "${app.mountpath}"`);
  });

  app.set('views', VIEW_DIR);
  app.set('view engine', 'pug');

  // routes
  app.use('/assets/', _options.handlers.assets(_options));
  app.get('/env.js', _options.handlers.windowEnv(_options));
  app.get('*', _options.handlers.index(_options));

  return app;
};
