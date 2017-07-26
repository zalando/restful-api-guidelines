'use strict';

const _ = require('lodash');
const path = require('path');

module.exports = (options = {}) => {
  return function windowEnvHandler (req, res) {
    const windowEnv = _.merge({}, options.windowEnv, {
      MOUNTPATH: path.join(req.app.mountpath, '/')
    });
    const jsOutput = `window.env = ${JSON.stringify(windowEnv)}`;
    res.setHeader('content-type', 'text/javascript');
    res.write(jsOutput);
    res.end();
  };
};
