'use strict';

const env = require('./env');

function envHandler (req, res) {
  const jsOutput = `window.env = ${JSON.stringify(env.public())}`;
  res.setHeader('content-type', 'text/javascript');
  res.write(jsOutput);
  res.end();
}

module.exports = envHandler;
