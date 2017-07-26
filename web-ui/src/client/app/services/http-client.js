import {HttpClient} from 'aurelia-fetch-client';
import OAuthInterceptor from './oauth-interceptor.js';

const client = new HttpClient();

// add a request clone to the original request object
// this is required for other interceptors interested in retry the request
// since the original request object cannot be used twice if it's already consumed
const retryCloneInterceptor = {
  request (request) {
    request.retryClone = request.clone();
    return request;
  }
};

client.configure(config => {
  config
    .withDefaults({ credentials: 'same-origin' })
    .rejectErrorResponses()
    .withInterceptor(retryCloneInterceptor)
    .withInterceptor(OAuthInterceptor);
});

export {client, retryCloneInterceptor};
