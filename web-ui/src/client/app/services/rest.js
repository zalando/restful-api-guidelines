import fetch from './fetch';
import {OAuthProvider} from './oauth';

export default {
  getApiViolations (apiDefinition) {
    const options = {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        api_definition: apiDefinition
      })
    };

    if (OAuthProvider.hasAccessToken()) {
      options.headers.Authorization = `Bearer ${OAuthProvider.getAccessToken()}`;
    }

    return fetch('/zally-api/api-violations', options)
      .then((response) => {
        return response.json();
      });
  }
};

