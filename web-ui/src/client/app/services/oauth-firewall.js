import OAuthProvider from './oauth-provider.js';
import {requestToken, checkTokenIsValid} from './oauth-util.js';

/**
 * Authenticate user with Oauth2 Implicit Grant Flow
 * The Access Token and Refresh Token are saved in the local storage
 *
 * @return {Promise}
 */
export default function firewall () {

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
