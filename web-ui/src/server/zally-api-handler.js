'use strict';

const env = require('./env');
const logger = require('./logger');
const request = require('request');

module.exports = function (req, res) {
  // remove the prefix found in the incoming req.url and concatenate
  // the remaining path to ZALLY_API_URL
  //
  // ex.
  // ZALLY_API_URL=https://api.zally.com
  // req.url=/zally-api/api-violations?some-filter=true
  //
  // url -> https://api.zally.com/api-violations?some-filter=true
  //
  const url = env.ZALLY_API_URL + req.url.replace('/zally-api', '');


  logger.debug(`Proxying request to: ${url}`);
  req.pipe(request(url)).pipe(res);

};
