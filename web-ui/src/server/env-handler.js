const fs = require('fs');
const env = require('./env');

function buildJS(publicEnv) {
  return `window.process = {}; window.process.env = ${JSON.stringify(publicEnv)}`;
}

function envHandler(req, res) {
  const publicEnv = env.public();

  if(env.ZALANDO_OAUTH && env.CREDENTIALS_DIR) {
    return fs
      .readFile(`${env.CREDENTIALS_DIR}/client.json`, (err, data) => {
        if(err) { throw err }
        const credentials = JSON.parse(data);
        publicEnv.OAUTH_CLIENT_ID = credentials.client_id;
        res.setHeader('content-type', 'text/javascript');
        res.write(buildJS(publicEnv));
        res.end();
      });
  }

  res.setHeader('content-type', 'text/javascript');
  res.write(buildJS(publicEnv));
  res.end();
}

module.exports = envHandler;
