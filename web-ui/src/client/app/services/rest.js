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
      .fetch('/zally-api/api-violations', options)
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
  }
};

