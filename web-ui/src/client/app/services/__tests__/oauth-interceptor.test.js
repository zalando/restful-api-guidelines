/* global global */

const {RequestMock} = require('../__mocks__/util-mocks');

const createAuthorizedRequest = () => {
  const request = new RequestMock();
  request.headers.append('Authorization', 'Bearer foo');
  return request;
};

const createUnauthorizedResponse = () => {
  return { status: 401 };
};

describe('OAuthInterceptor', () => {

  let OAuthInterceptor, OAuthProviderMock, mockRefreshToken, mockRequestToken, mockFetch;

  beforeEach(() => {
    jest.resetModules();

    global.window = {
      env: {}
    };

    mockRefreshToken = jest.fn();
    mockRequestToken = jest.fn();
    mockFetch = jest.fn();

    jest.mock('../oauth-provider');
    jest.mock('../oauth-util', () => ({ refreshToken: mockRefreshToken, requestToken: mockRequestToken }));
    jest.mock('../fetch', () => (mockFetch));


    OAuthProviderMock = require('../oauth-provider').default;
    OAuthInterceptor = require('../oauth-interceptor').default;
  });

  afterEach(() => {
    delete global.window;
  });

  describe('request interceptor', () => {
    test('should add the expected authorization header if OAuthProvider has an access token', () => {
      OAuthProviderMock._accessToken = 'foo';
      const request = OAuthInterceptor.request(new RequestMock());
      expect(request.headers.get('Authorization')).toEqual('Bearer foo');
    });

    test('shouldn\'t add the Authorization header if OAuthProvider has not an access token', () => {
      OAuthProviderMock._accessToken = undefined;
      const request = OAuthInterceptor.request(new RequestMock());
      expect(request.headers.get('Authorization')).toBeUndefined();
    });
  });

  describe('responseError interceptor skip', () => {
    test('if request doesn\'t contain Authorization header', (done) => {
      const response = {};
      const request = new RequestMock();
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

    test('if OAuthProvider doesn\'t have a refresh token', (done) => {
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
  });

  describe('responseError interceptor try to refresh the token', () => {
    let response, request;

    beforeEach(() => {
      request = createAuthorizedRequest();
      response = createUnauthorizedResponse();
      OAuthProviderMock._refreshToken = 'bar';
    });

    afterEach(() => {
      delete OAuthProviderMock._refreshToken;
    });

    test('if success, retry failed requests', (done) => {

      mockRefreshToken.mockReturnValueOnce(Promise.resolve());
      mockFetch.mockReturnValueOnce(Promise.resolve());

      OAuthInterceptor
        .responseError(response, request)
        .then(() => {
          expect(mockRefreshToken).toHaveBeenCalled();
          expect(mockFetch).toHaveBeenCalled();
          done();
        });
    });

    test('if fails, reject failed requests and request a new token with implicit flow', (done) => {
      mockRefreshToken.mockReturnValueOnce(Promise.reject('test refresh token fails'));

      OAuthInterceptor
        .responseError(response, request)
        .catch(() => {
          expect(mockRefreshToken).toHaveBeenCalled();
          expect(mockFetch).not.toHaveBeenCalled();
          expect(mockRequestToken).toHaveBeenCalled();
          done();
        });
    });

    test('should skip refresh token operation if already in progress', (done) => {
      mockRefreshToken.mockReturnValueOnce(Promise.resolve());
      mockFetch.mockReturnValue(Promise.resolve());

      OAuthInterceptor.responseError(response, request);
      OAuthInterceptor
        .responseError(response, request)
        .then(() => {
          expect(mockRefreshToken.mock.calls.length).toEqual(1);
          expect(mockFetch.mock.calls.length).toEqual(2);
          done();
        });
    });
  });

});
