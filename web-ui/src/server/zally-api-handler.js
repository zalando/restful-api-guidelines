const env = require('./env');
const logger = require('./logger');
const fetch = require('./fetch');
const yaml = require('js-yaml');

const parseSchema = (text) => {
  try {
    // try json first
    return JSON.parse(text);
  } catch(err) {}

  return yaml.safeLoad(text);
};

module.exports = function (req, res) {
  const url = env.ZALLY_API_URL + req.url.replace('/zally-api', '');
  const apiDefinitionURL = req.body.api_definition;

  logger.debug(`Fetch swagger schema: ${apiDefinitionURL}`);

  fetch(apiDefinitionURL)
    .then((response) => {
      return response.text();
    })
    .then((text) => {
      logger.debug(`Parse schema: ${apiDefinitionURL}`);
      return parseSchema(text);
    })
    .then((schema) => {
      logger.debug(`Proxying request to: ${url}`);
      return fetch(url, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
          'Authorization': req.headers.Authorization
        },
        body: JSON.stringify({ api_definition: schema })
      })
    })
    .then((response) => {
      return response.json();
    })
    .then((json) => {
      res.json(json);
    })
    .catch((error) => {
      const status = error.response ? error.response.status : 400;
      res.status(status);

      // use https://zalando.github.io/problem/schema.yaml#/Problem'
      res.json({
        type: 'about:blank',
        title: error.message,
        detail: error.message,
        status
      });
    });
};
