import {client} from './http-client.js';

export const RestService = {
  getApiViolations (body) {
    const options = {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    };
    return client
      .fetch(`${window.env.ZALLY_API_URL}/api-violations`, options)
      .then((response) => {
        return response.json();
      })
      .catch((response) => {
        return response.json().then((body) => {
          return Promise.reject(body);
        });
      });
  },

  getApiViolationsByURL (apiDefinitionURL) {
    return this.getApiViolations({
      api_definition_url: apiDefinitionURL
    });
  },

  getApiViolationsBySchema (schema) {
    return this.getApiViolations({
      api_definition: schema
    });
  },

  getSupportedRules (params) {
    const options = {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    };
    const url = `${window.env.ZALLY_API_URL}/supported-rules` + this.objectToParams(params);
    return client
      .fetch(url, options)
      .then((response) => {
        return response.json();
      })
      .catch((response) => {
        return response.json().then((body) => {
          return Promise.reject(body);
        });
      });
  },

  objectToParams (params) {
    if (params) {
      return '?' + Object.keys(params)
          .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k]))
          .join('&');
    }
    return '';
  }
};

