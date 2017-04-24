'use strict';


const express = require('express');
const _ = require('lodash');
const path = require('path');
const ASSETS_DIR = path.resolve(__dirname, '../client/public/assets');

const DEFAULT_OPTIONS = {
  windowEnv: {
    OAUTH_ENABLED: false,
    DEBUG: true,
    ZALLY_API_URL: 'http://localhost:8080'
  },
  handlers: {
    assets: () => express.static(ASSETS_DIR),
    windowEnv: require('./window-env-handler'),
    spa: () => {
      return (req, res) => {
        res.sendFile(path.resolve(__dirname, '../client/public/index.html'));
      };
    }
  }
};

module.exports = (options = {}) => {
  const app = express();
  const _options = _.merge({}, DEFAULT_OPTIONS, options);

  app.use('/assets/', _options.handlers.assets(_options));
  app.get('/env.js', _options.handlers.windowEnv(_options));
  app.get('*', _options.handlers.spa(_options));

  return app;
};
