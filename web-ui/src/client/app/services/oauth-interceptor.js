import OAuthProvider from './oauth-provider.js';
import fetch from './fetch';
import {requestToken, refreshToken} from './oauth-util.js';
import {debug} from './debug.js';

// OAuthInterceptor state
const state = {
  refreshingToken: false,
  unauthorizedRequests: []
};

const OAuthInterceptor = {
  request (request) {
    if (OAuthProvider.hasAccessToken()) {
      request.headers.append('Authorization', `Bearer ${OAuthProvider.getAccessToken()}`);
    }
    return request;
  },
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

        // `rejectUnauthorizedRequests()` statement (in theory) is redundant because `requestToken`
        // will redirect to authorize endpoint, but who knows, something can always go wrong.
        rejectUnauthorizedRequests();
        requestToken();
      });

    return deferred;
  }
};

function shouldHandleResponseError (response, request) {
  return request.headers.has('Authorization')
   && response.status === 401 && OAuthProvider.hasRefreshToken();
}


function createDeferredPromise () {
  let resolve, reject;
  const deferred = new Promise((rs, rj) => {
    resolve = rs;
    reject = rj;
  });
  return {deferred, resolve, reject};
}

function retryUnauthorizedRequests () {
  debug('Retrying unauthorized requests', state.unauthorizedRequests.concat([]));

  const promises = state.unauthorizedRequests.map(({request, resolve, reject}) => {
    const retryRequest = request.retryClone || request;

    retryRequest.headers.delete('Authorization');
    retryRequest.headers.append('Authorization', `Bearer ${OAuthProvider.getAccessToken()}`);

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

function rejectUnauthorizedRequests () {
  state.unauthorizedRequests.map(({response, reject}) => {
    reject(response);
  });
  state.unauthorizedRequests.length = 0;
}




export default OAuthInterceptor;
