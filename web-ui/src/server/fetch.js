'use strict';

const fetch = require('node-fetch');

function handleResponse (response) {
  if (response.status >= 400) {
    const error = new Error(response.statusText || response.status);
    error.status = response.status || 500;
    return Promise.reject(error);
  }

  return Promise.resolve(response);
}

module.exports = function () {
  return fetch
    .apply(null, arguments)
    .then(handleResponse);
};
