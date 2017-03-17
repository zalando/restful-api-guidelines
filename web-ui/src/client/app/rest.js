import $ from 'jquery';
import {OAuthProvider} from './oauth';

export default {
  getApiViolations(apiDefinition) {
    const options = {
      url: '/zally-api/api-violations',
      type: 'POST',
      dataType: 'json',
      processData: false,
      contentType: 'application/json',
      data: JSON.stringify({
        api_definition: apiDefinition
      })
    };

    if(OAuthProvider.getAccessToken()) {
      options.headers = { Authorization: `Bearer ${OAuthProvider.getAccessToken()}`}
    }

    return $.ajax(options);
  }
}

