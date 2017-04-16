import {Request} from 'oauth2-client-js';
import querystring from 'query-string';
import OAuthProvider from './oauth-provider.js';
import {client} from './http-client.js';
import fetch from './fetch.js';
import {debug} from './debug.js';
import get from 'lodash/get';

export function requestToken () {
  const request = new Request({
    client_id: window.env.OAUTH_CLIENT_ID,
    redirect_uri:  window.env.OAUTH_REDIRECT_URI,
    scopes:  window.env.OAUTH_SCOPES
  });
  OAuthProvider.remember(request);
  window.location.href = OAuthProvider.requestToken(request);
}

export function refreshToken () {
  const query = {
    refresh_token: OAuthProvider.getRefreshToken(),
    client_id: window.env.OAUTH_CLIENT_ID
  };
  return fetch(`/refresh-token?${querystring.stringify(query)}`, {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    }
  }).then((response) => {
    return response.json().then((body) => {
      return {response, body};
    });
  })
  .then(({response, body}) => {
    OAuthProvider.setAccessToken(body.access_token);
    OAuthProvider.setRefreshToken(body.refresh_token);
    debug('token refreshed, new token', body);
    return { response, body };
  })
  .catch((error) => {
    console.warn('Can\'t refresh the token, an error occurred', error); // eslint-disable-line no-console
    return Promise.reject(error);
  });
}

export function checkTokenIsValid () {
  return client.fetch('/tokeninfo', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    }
  })
  .then((response) => {
    return response.json();
  })
  .catch((error) => {
    console.error(error); // eslint-disable-line no-console
    return Promise.reject(error);
  });
}

export function getUsername (tokenInfoBody){
  const usernamePath = window.env.OAUTH_USERNAME_PROPERTY;
  return get(tokenInfoBody, usernamePath, 'anonymous');
}

export function createUser (tokenInfoBody) {
  return {
    username: getUsername(tokenInfoBody)
  };
}

export function logout (){
  OAuthProvider.deleteTokens();
  window.location.reload();
}

export function login () {
  requestToken();
}

/**
 * Return a function meant to be used with the ```onEnter``` react router hook.
 * If auth is enabled and the user is not authenticated the function will "redirect" to login.
 *
 * @param {Object} props
 * @param {Object} props.user - a user
 * @param {string} props.loginLocation - login page location (eg. '/login')
 * @return {Function}
 */
export function onEnterRequireLogin (props) {
  return window.env.OAUTH_ENABLED
  && !props.user.authenticated ? (o, replace) => replace(props.loginLocation) : () => {};
}
