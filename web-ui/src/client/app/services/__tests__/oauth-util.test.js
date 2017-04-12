/* global global, window, jasmine */

import {Request} from 'oauth2-client-js';
import {requestToken, refreshToken} from '../oauth-util.js';
import OAuthProvider from '../oauth-provider.js';
import fetch from '../fetch.js';

jest.mock('../oauth-provider');
jest.mock('../fetch');

describe('requestToken', () => {
  beforeEach(() => {
    global.window = {
      location: {},
      env: {
        OAUTH_CLIENT_ID: 'client_id',
        OAUTH_REDIRECT_URI: 'redirect_uri',
        OAUTH_SCOPES: 'scope'
      }
    };
  });

  afterEach(() => {
    OAuthProvider.requestToken.mockReset();
    OAuthProvider.remember.mockReset();
    delete global.window;
  });

  test('redirect to authorize url and remember the request', () => {
    const authorizeURL = 'http://www.google.com';
    OAuthProvider.requestToken.mockReturnValueOnce(authorizeURL);

    requestToken();
    expect(OAuthProvider.remember).toHaveBeenCalledWith(jasmine.any(Request));

    expect(global.window.location.href).toBe(authorizeURL);
  });
});

describe('refreshToken', () => {
  beforeEach(() => {
    global.window = {
      env: {
        OAUTH_CLIENT_ID: 'client_id'
      }
    };
  });

  afterEach(() => {
    fetch.mockReset();
    delete global.window;
  });

  test('when success should access and refresh tokens' , () => {
    const mockJson = jest.fn();
    const newAccessToken = 'foo';
    const newRefreshToken = 'bar';
    mockJson.mockReturnValueOnce(Promise.resolve({
      access_token: newAccessToken,
      refresh_token: newRefreshToken
    }));
    fetch.mockReturnValueOnce(Promise.resolve({
      json: mockJson
    }));
    return refreshToken().then(() => {
      expect(OAuthProvider.getAccessToken()).toEqual(newAccessToken);
      expect(OAuthProvider.getRefreshToken()).toEqual(newRefreshToken);
    });
  });

  test('when failure should reject with an error', (done) => {
    const error = new Error('test refreshToken error fails');
    fetch.mockReturnValueOnce(Promise.reject(error));
    refreshToken().catch((e) => {
      try {
        expect(e).toEqual(error);
        done();
      } catch (e) {
        done.fail(e);
      }
    });
  });
});
