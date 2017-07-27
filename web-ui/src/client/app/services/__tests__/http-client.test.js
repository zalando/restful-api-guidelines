const {RequestMock} = require('../__mocks__/util-mocks');

describe('http-client', () => {
  beforeEach(() => {
    jest.resetModules();
    jest.mock('aurelia-fetch-client');
    jest.mock('../oauth-interceptor', () => ({}));
  });

  describe('retryCloneInterceptor', () => {
    let retryCloneInterceptor;

    beforeEach(() => {
      retryCloneInterceptor = require('../http-client').retryCloneInterceptor;
    });

    test('add a cloned instance to the request', () => {
      const request = new RequestMock();
      const modifiedRequest = retryCloneInterceptor.request(request);
      expect(modifiedRequest.retryClone).toBeDefined();
      expect(modifiedRequest.retryClone).toBeInstanceOf(RequestMock);
    });
  });
});
