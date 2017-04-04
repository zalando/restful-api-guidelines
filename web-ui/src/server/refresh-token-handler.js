'use strict';

const querystring = require('query-string');
const env = require('./env');
const fetch = require('./fetch');
const logger = require('./logger');

module.exports = function (req, res) {
  let url = env.OAUTH_REFRESH_TOKEN_URL;

  logger.debug(`Try to refresh the token, proxying request to: ${url}`);

  // append query parameters
  url += (url.indexOf('?') === -1 ? '?' : '&') + querystring.stringify(req.query);

  fetch(url, { method: 'POST' })
  .then((response) => {
    return response.json();
  })
  .then((token) => {
    res.json(token);
  })
  .catch((error) => {
    logger.warn('refresh token error', error);
    res.status(400);
    res.json({
      title: error.message,
      detail: 'Refresh token request failed'
    });
  });
};
