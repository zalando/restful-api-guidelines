const fs = require('fs');
const env = require('./env');
const logger = require('./logger');
const path = require('path');

function buildJS(publicEnv) {
  return `window.process = {}; window.process.env = ${JSON.stringify(publicEnv)}`;
}

function sendResponse(res, content) {
  res.setHeader('content-type', 'text/javascript');
  res.write(content);
  res.end();
}

function zalandoEnvHandler(req, res) {
  const publicEnv = env.public();
  fs
    .readFile(path.join(env.CREDENTIALS_DIR, 'client.json'), (err, data) => {
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

  sendResponse(res, buildJS(publicEnv))
}

function envHandler(req, res) {
  const publicEnv = env.public();

  if(env.ZALANDO_OAUTH && env.CREDENTIALS_DIR) {
    zalandoEnvHandler(req, res);
    return;
  }

  sendResponse(res, buildJS(publicEnv));
}

module.exports = envHandler;
