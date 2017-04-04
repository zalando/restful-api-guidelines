import {Provider, Request} from 'oauth2-client-js';
import fetch from './fetch';

const OAuthProvider = new Provider({
  id: 'zally',
  authorization_url: window.env.OAUTH_AUTHORIZATION_URL
});

function requestToken () {
  const request = new Request({
    client_id: window.env.OAUTH_CLIENT_ID,
    redirect_uri:  window.env.OAUTH_REDIRECT_URI,
    scopes:  window.env.OAUTH_SCOPES
  });
  OAuthProvider.remember(request);
  window.location.href = OAuthProvider.requestToken(request);
}

function checkTokenIsValid () {
  return fetch('/tokeninfo', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + OAuthProvider.getAccessToken()
    }
  })
  .then((response) => {
    return response.json();
  })
  .catch((error) => {
    console.error(error); // eslint-disable-line no-console
    requestToken();
    return Promise.reject(error);
  });
}
/**
 * Authenticate user with Oauth2 Implicit Grant Flow
 * The Access Token and Refresh Token are saved in the local storage
 *
 * @return {Promise}
 */
function firewall () {

  if (!window.env.OAUTH_ENABLED) { return Promise.resolve(); }

  // do we have a response from auth server?
  // check if we can parse the url fragment
  if (window.location.hash.length && window.location.hash.startsWith('#access_token')) {
    let response;
    try {
      response = OAuthProvider.parse(window.location.hash);
      window.location.href = window.location.href.substr(0, window.location.href.indexOf('#'));
    } catch (err) {
      if (response instanceof Error) {
        return Promise.reject(response);
      }
      requestToken();
      return Promise.reject(err);
    }
  }

  if (!OAuthProvider.hasAccessToken()) {
    requestToken();
    return Promise.reject();
  }

  return checkTokenIsValid();
}

export {firewall, OAuthProvider};
