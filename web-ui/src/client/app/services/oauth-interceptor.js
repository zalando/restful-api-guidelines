import fetch from './fetch';
import {login, refreshToken} from './oauth-util.js';
import {debug} from './debug.js';

// OAuthInterceptor state
const state = {
  refreshingToken: false,
  unauthorizedRequests: []
};

const OAuthInterceptor = {
  responseError (response, request) {

    if (!shouldHandleResponseError(response, request)) {
      return Promise.reject(response);
    }

    const {deferred, resolve, reject} = createDeferredPromise();

    debug('Unauthorized request intercepted', request);
    state.unauthorizedRequests.push({request, response, resolve, reject});

    // if we are already refreshing the token return the deferred promise
    // the original request it's persisted in the unauthorizedRequests stack
    if (state.refreshingToken) { return deferred; }

    debug('refreshing token');
    state.refreshingToken = true;
    refreshToken()
      .then(() => {
        state.refreshingToken = false;
        retryUnauthorizedRequests();
      })
      .catch((error) => {
        console.error(error); // eslint-disable-line no-console
        state.refreshingToken = false;
        login();
        rejectUnauthorizedRequests();
      });

    return deferred;
  }
};

function shouldHandleResponseError (response) {
  return window.env.OAUTH_ENABLED && response.status === 401;
}

function createDeferredPromise () {
  let resolve, reject;
  const deferred = new Promise((rs, rj) => {
    resolve = rs;
    reject = rj;
  });
  return {deferred, resolve, reject};
}

function rejectUnauthorizedRequests () {
  state.unauthorizedRequests.map(({response, reject}) => {
    reject(response);
  });
  state.unauthorizedRequests.length = 0;
}

function retryUnauthorizedRequests () {
  debug('Retrying unauthorized requests', state.unauthorizedRequests.concat([]));

  const promises = state.unauthorizedRequests.map(({request, resolve, reject}) => {
    const retryRequest = request.retryClone || request;

    // to retry with use the low-level fetch instead of the http client to avoid the usage of interceptors
    // that can cause a never ending loop
    return fetch(retryRequest)
      .then((r) => {
        debug('Retry unauthorized request success', retryRequest);
        resolve(r);
        return r;
      })
      .catch((error) => {
        console.warn('Retry unauthorized request fails', retryRequest, error); // eslint-disable-line no-console
        reject(error);
        return Promise.reject(error);
      });
  });

  // fetch or others "synchronous" operations can throw some Error that could be "lost"
  // if we don't catch with Promise.all
  Promise
    .all(promises)
    .catch((error) => {
      console.error('Something went wrong while retryUnauthorizedRequests', error); // eslint-disable-line no-console
    });

  state.unauthorizedRequests.length = 0;
}

export default OAuthInterceptor;
