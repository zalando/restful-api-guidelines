const fs = require('fs');
const env = require('./env');
const logger = require('./logger');
const path = require('path');

/**
 * @param {Object} publicEnv - an object representing the environment variables available for the client
 * @return {String}
 */
function buildJS(publicEnv) {
  return `window.env = ${JSON.stringify(publicEnv)}`;
}

/**
 * @param res - http server response
 * @param {String} content
 */
function sendResponse(res, content) {
  res.setHeader('content-type', 'text/javascript');
  res.write(content);
  res.end();
}

/**
 * Zalando specific env handler,
 * should be removed when Zalando OAuth2 stop rotating client credentials
 *
 * @param req
 * @param res
 */
function zalandoEnvHandler(req, res) {

  const publicEnv = env.public();
  const clientJSONPath = path.join(env.CREDENTIALS_DIR, 'client.json');

  logger.debug(`Reading ${clientJSONPath}`);

  fs
    .readFile(clientJSONPath, (err, data) => {

      if(err) {
        logger.error('Cannot read client.json!');
        logger.error(err);
        sendResponse(res, buildJS(publicEnv));
        return;
      }

      try {
        const credentials = JSON.parse(data);
        publicEnv.OAUTH_CLIENT_ID = credentials.client_id;
      }catch(error) {
        logger.error('Cannot parse client.json')
      }

      sendResponse(res, buildJS(publicEnv));
    });
}

/**
 * @param req
 * @param res
 */
function envHandler(req, res) {
  const publicEnv = env.public();

  if(env.ZALANDO_OAUTH && env.CREDENTIALS_DIR) {
    zalandoEnvHandler(req, res);
    return;
  }

  sendResponse(res, buildJS(publicEnv));
}

module.exports = envHandler;
