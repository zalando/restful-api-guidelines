import {client} from './http-client.js';
import fetch from './fetch.js';
import {debug} from './debug.js';

export function refreshToken () {
  return fetch('/auth/refresh-token', {
    method: 'POST',
    credentials: 'same-origin',
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
    debug('token refreshed', body);
    return { response, body };
  })
  .catch((error) => {
    console.warn('Can\'t refresh the token, an error occurred', error); // eslint-disable-line no-console
    return Promise.reject(error);
  });
}

export function me () {
  return client.fetch('/auth/me', {
    method: 'POST',
    credentials: 'same-origin',
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
    error.message = error.message || 'invalid access token';
    return Promise.reject(error);
  });
}

export function createAnonymousUser () {
  return {
    username: 'anonymous',
    authenticated: false
  };
}

export function logout () {
  window.location.href = '/auth/logout';
}

export function login () {
  window.location.href = '/auth/login';
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
