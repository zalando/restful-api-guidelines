import OAuthProvider from './oauth-provider.js';
import {checkTokenIsValid} from './oauth-util.js';

export default firewall;

/**
 * Authenticate user with Oauth2 Implicit Grant Flow
 * The Access Token and Refresh Token are saved in the local storage
 *
 * @return {Promise}
 */
function firewall () {

  if (!window.env.OAUTH_ENABLED) { return Promise.resolve(); }

  if (firewall.hasAuthResponse()) {

    let response;
    try {
      response = OAuthProvider.parse(window.location.hash);
      // OAuthError just reject
      if (response instanceof Error) {
        return Promise.reject(response);
      }
      return checkTokenIsValid();

    } catch (err) {
      return Promise.reject(err);
    }
  }

  if (!OAuthProvider.hasAccessToken()) {
    return Promise.reject(new Error('no access token'));
  }

  return checkTokenIsValid();
}

firewall.hasAuthResponse = () => {
  // check if we can parse the url fragment
  return window.location.hash.length && window.location.hash.startsWith('#access_token');
};
