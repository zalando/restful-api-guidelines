/* global global */

describe('OAuthProvider', () => {
  let OAuthProvider;
  beforeEach(() => {
    jest.resetModules();
    jest.mock('oauth2-client-js');

    global.window = {
      env: {
        OAUTH_AUTHORIZATION_URL: 'https://foo.com/authorize'
      }
    };
    OAuthProvider = require('../oauth-provider').default;
  });

  afterEach(() => {
    delete global.window;
  });

  test('should be initialized with the expected values', () => {
    expect(OAuthProvider.options.id).toEqual('zally');
    expect(OAuthProvider.options.authorization_url).toEqual(window.env.OAUTH_AUTHORIZATION_URL);
  });
});
