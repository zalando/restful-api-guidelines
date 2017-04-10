'use strict';

const request = require('request');
const path = require('path');
const logger = require('./logger');


module.exports = function ({publicPath, protocol, host, port}) {
  return function (req, res) {
    const segment = path.join(publicPath, req.url);
    const url = `${protocol}://${host}:${port}${segment}`;
    logger.debug(`Proxying request to webpack dev server: ${url}`);
    req.pipe(request(url)).pipe(res);
  };
};

