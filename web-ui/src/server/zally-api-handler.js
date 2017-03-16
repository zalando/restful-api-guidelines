const env = require('./env');
const request = require('request');

module.exports = function (req, res) {
  var url = env.ZALLY_API_URL + req.url.replace('/zally-api', '');
  req.pipe(request(url)).pipe(res);
};
