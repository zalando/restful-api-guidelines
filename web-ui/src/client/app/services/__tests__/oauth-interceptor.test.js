/* global global */

const {RequestMock} = require('../__mocks__/util-mocks');

const createAuthorizedRequest = () => {
  return new RequestMock();
};

const createUnauthorizedResponse = () => {
  return { status: 401 };
};

describe('OAuthInterceptor', () => {

  let OAuthInterceptor, mockRefreshToken, mockLogin, mockFetch;

  beforeEach(() => {
    jest.resetModules();

    global.window = {
      env: {}
    };

    mockRefreshToken = jest.fn();
    mockLogin = jest.fn();
    mockFetch = jest.fn();

    jest.mock('../oauth-util', () => ({ refreshToken: mockRefreshToken, login: mockLogin }));
    jest.mock('../fetch', () => (mockFetch));

    OAuthInterceptor = require('../oauth-interceptor').default;
  });

  afterEach(() => {
    delete global.window;
  });


  describe('responseError interceptor skip', () => {

    test('if OAuth is disabled', (done) => {
      window.env.OAUTH_ENABLED = false;
      const request = createAuthorizedRequest();
      const response = createUnauthorizedResponse();

      OAuthInterceptor
        .responseError(response, request)
        .catch((e) => {
          expect(e).toEqual(response);
          expect(mockRefreshToken).not.toHaveBeenCalled();
          done();
        });
    });

    test('if response status !== 401', (done) => {
      const response = {
        status: 500
      };
      const request = createAuthorizedRequest();
      OAuthInterceptor
        .responseError(response, request)
        .catch((e) => {
          expect(e).toEqual(response);
          expect(mockRefreshToken).not.toHaveBeenCalled();
          done();
        });
    });


  });

  describe('responseError interceptor try to refresh the token', () => {
    let response, request;

    beforeEach(() => {
      window.env.OAUTH_ENABLED = true;
      request = createAuthorizedRequest();
      response = createUnauthorizedResponse();
    });

    test('if success, retry failed requests', () => {

      mockRefreshToken.mockReturnValueOnce(Promise.resolve());
      mockFetch.mockReturnValueOnce(Promise.resolve());

      return OAuthInterceptor
        .responseError(response, request)
        .then(() => {
          expect(mockRefreshToken).toHaveBeenCalled();
          expect(mockFetch).toHaveBeenCalled();
        });
    });

    test('if fails, reject failed requests and login', (done) => {
      mockRefreshToken.mockReturnValueOnce(Promise.reject('test refresh token fails'));

      OAuthInterceptor
        .responseError(response, request)
        .then(() => {
          done(new Error('it should reject'));
        })
        .catch(() => {
          expect(mockRefreshToken).toHaveBeenCalled();
          expect(mockFetch).not.toHaveBeenCalled();
          expect(mockLogin).toHaveBeenCalled();
          done();
        });
    });

    test('should skip refresh token operation if already in progress', () => {
      mockRefreshToken.mockReturnValueOnce(Promise.resolve());
      mockFetch.mockReturnValue(Promise.resolve());

      OAuthInterceptor.responseError(response, request);
      return OAuthInterceptor
        .responseError(response, request)
        .then(() => {
          expect(mockRefreshToken.mock.calls.length).toEqual(1);
          expect(mockFetch.mock.calls.length).toEqual(2);
        });
    });
  });

});
