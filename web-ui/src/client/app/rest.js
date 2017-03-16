import $ from 'jquery';


export default {
  getApiViolations(apiDefinition, accessToken) {
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

    if(accessToken) {
      options.headers = { Authorization: `Bearer ${accessToken}`}
    }

    return $.ajax(options);
  }
}

