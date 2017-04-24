'use strict';

module.exports = (options = {}) => {
  return function envHandler (req, res) {
    const jsOutput = `window.env = ${JSON.stringify(options.windowEnv)}`;
    res.setHeader('content-type', 'text/javascript');
    res.write(jsOutput);
    res.end();
  };
};
