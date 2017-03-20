const fetch = require('node-fetch');

function handleResponse(response) {
  if (response.status >= 200 && response.status < 300) {
    return Promise.resolve(response)
  } else {
    var error = new Error(response.statusText || response.status);
    error.response = response;
    return Promise.reject(error);
  }
}

module.exports = function () {
  return fetch
    .apply(null, arguments)
    .then(handleResponse);
};
