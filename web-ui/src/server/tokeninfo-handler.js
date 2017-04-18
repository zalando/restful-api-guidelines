'use strict';

const env = require('./env');
const request = require('request');
const logger = require('./logger');

module.exports = function (req, res) {
  const url = env.OAUTH_TOKENINFO_URL;
  logger.debug(`proxying request to: ${url}`);
  req.pipe(request(url)).pipe(res);
};
