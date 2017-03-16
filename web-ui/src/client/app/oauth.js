import {Provider, Request} from 'oauth2-client-js';
import $ from 'jquery';

/**
 * Authenticate user with Oauth2 Implicit Grant Flow
 * The Access Token and Refresh Token are saved in the local storage
 *
 * @param {Object} oauthConfiguration
 * @return {Promise}
 */
function firewall(oauthConfiguration) {

  const OAuthProvider = new Provider({
    id: 'zally',
    authorization_url: oauthConfiguration.authorization_url
  });

  if(!oauthConfiguration || !oauthConfiguration.enabled) { return Promise.resolve({OAuthProvider}); }

  function requestToken() {
    var request = new Request({
      client_id: oauthConfiguration.client_id,
      redirect_uri: oauthConfiguration.redirect_uri,
      scopes: oauthConfiguration.scopes
    });
    OAuthProvider.remember(request);
    window.location.href = OAuthProvider.requestToken(request);
  }


  // do we have a response from auth server?
  // check if we can parse the url fragment
  if (window.location.hash.length) {
    let response;
    try {
      response = OAuthProvider.parse(window.location.hash);
    } catch(err) {
      if (response instanceof Error) {
        return console.error(response);
      }
    }
  }

  if (!OAuthProvider.hasAccessToken()) {
    requestToken();
  }

  // check validity of token
  return $.ajax({
    url: '/tokeninfo',
    type: 'POST',
    dataType: 'json',
    headers: {
      Authorization: 'Bearer ' + OAuthProvider.getAccessToken()
    }
  }).then((response) => {
    return {OAuthProvider, response};
  })
  .catch((error) => {
     console.error(error);
     requestToken();
     throw {OAuthProvider, error};
  });
}

export {firewall}
