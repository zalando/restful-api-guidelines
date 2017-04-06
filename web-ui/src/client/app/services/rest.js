import {client} from './http-client.js';

export default {
  getApiViolations (apiDefinitionUrl) {
    const options = {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        api_definition_url: apiDefinitionUrl
      })
    };
    return client
      .fetch('/zally-api/api-violations', options)
      .then((response) => {
        return response.json();
      });
  }
};

