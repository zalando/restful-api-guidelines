'use strict';

describe('server.tokeninfo-handler', () => {
  const mockPipe = jest.fn(() => req);
  const req = {
    pipe: mockPipe
  };
  const mockDebug = jest.fn();
  const mockRequest = jest.fn();
  const res = {};

  jest.mock('../env', () => ({
    'OAUTH_TOKENINFO_URL': 'https://example.com'
  }));

  jest.mock('../logger', () => ({
    debug: mockDebug
  }));

  jest.mock('request', () => mockRequest);

  const tokeninfoHandler = require('../tokeninfo-handler.js');

  tokeninfoHandler(req, res);

  test('should export a function', () => {
    expect(tokeninfoHandler).toBeInstanceOf(Function);
  });

  describe('when invoking the function', () => {
    test('should log the debug message', () => {
      expect(mockDebug).toHaveBeenCalledWith('Proxying request to: https://example.com');
    });

    test('should read the URL from env.OAUTH_TOKENINFO_URL', () => {
      expect(mockRequest).toHaveBeenCalledWith('https://example.com');
    });

    test('should proxy the request', () => {
      expect(mockPipe.mock.calls.length).toBe(2);
    });
  });
});
