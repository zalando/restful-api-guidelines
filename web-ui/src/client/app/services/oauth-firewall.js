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

      // OAuthError just reject
      if (response instanceof Error) {
        return Promise.reject(response);
      }

      window.location.href = window.location.origin;

      return Promise.resolve(response);

    } catch (err) {

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
