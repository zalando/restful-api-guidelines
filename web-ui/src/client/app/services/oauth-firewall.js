import {me, createAnonymousUser} from './oauth-util.js';

export default firewall;

/**
 * @return {Promise}
 */
function firewall () {

  if (!window.env.OAUTH_ENABLED) {
    return Promise.resolve({ body: null, user: createAnonymousUser() });
  }

  return me().then((body) => {
    return ({ body, user: body  });
  })
  .catch((error) => {
    console.error(`Firewall Error occurred: ${error.message}`); // eslint-disable-line no-console
    console.error(error); // eslint-disable-line no-console
    return Promise.resolve({ body: null, error: error, user: createAnonymousUser() });
  });
}
