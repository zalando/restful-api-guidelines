'use strict';

describe('server.zally-api-handler', () => {
  const mockDebug = jest.fn();
  const mockPipe = jest.fn(() => req);
  const mockRequest = jest.fn();
  const res = {};
  const req = {
    pipe: mockPipe,
    url: '/zally-api/path-to-api',
  };
  jest.mock('../env', () => ({
    'ZALLY_API_URL': 'https://example.com'
  }));

  jest.mock('../logger', () => ({
    debug: mockDebug
  }));

  jest.mock('request', () => mockRequest);

  const zallyApiHandler = require('../zally-api-handler');

  test('should export a function', () => {
    expect(zallyApiHandler).toBeInstanceOf(Function);
  });

  describe('when invoking the function', () => {
    beforeAll(() => {
      return zallyApiHandler(req, res);
    });
    test('should log the debug message', () => {
      expect(mockDebug).toHaveBeenCalledWith('Proxying request to: https://example.com/path-to-api');
    });

    test('should modified url from  env.ZALLY_API_URL', () => {
      expect(mockRequest).toHaveBeenCalledWith('https://example.com/path-to-api');
    });

    test('should proxy the request', () => {
      expect(mockPipe.mock.calls.length).toBe(2);
    });
  });


});

