const env = require('./env');
const request = require('request');

module.exports = function (req, res) {
  var url = env.OAUTH_TOKENINFO_URL;
  req.pipe(request(url)).pipe(res);
};
