'use strict';

const path = require('path');

module.exports = (options) => {
  return function indexHandler (req, res) {
    const mountpath = path.join(req.app.mountpath, '/');
    res.render('index', { mountpath: mountpath, headTitle: options.env.HEAD_TITLE });
  };
};
