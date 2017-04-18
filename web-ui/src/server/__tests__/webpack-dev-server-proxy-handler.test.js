'use strict';

describe('server.webpack-dev-server-proxy-handler', () => {
  let webpackDevServerProxyHandlerFactory, webpackDevServerProxyHandler, mockRequest;

  beforeEach(() => {
    jest.resetModules();
    mockRequest = jest.fn();
    jest.mock('request', () => mockRequest);
    jest.mock('../logger', () => ({debug: () => {}}));
    webpackDevServerProxyHandlerFactory = require('../webpack-dev-server-proxy-handler');
    webpackDevServerProxyHandler = webpackDevServerProxyHandlerFactory({
      publicPath: '/assets',
      protocol: 'http',
      host: 'localhost',
      port: 8000
    });
  });

  test('it\'s a function representing the handler', () => {
    expect(typeof webpackDevServerProxyHandler).toEqual('function');
  });

  test('pipe the response to the the expected url', () => {
    const req = { url: '/bundle.js' };
    const res = {};
    const mockRequestResponse = {
      on: jest.fn().mockImplementationOnce((event, cb) => {
        cb({statusCode: 200});
      }),
      pipe: jest.fn()
    };
    mockRequest.mockReturnValueOnce(mockRequestResponse);

    webpackDevServerProxyHandler(req, res);
    expect(mockRequest).toHaveBeenCalledWith('http://localhost:8000/assets/bundle.js');
    expect(mockRequestResponse.pipe).toHaveBeenCalledWith(res);
  });

  test('call the next middleware if proxy response error', () => {
    const req = { url: '/bundle.js' };
    const res = {};
    const mockNext = jest.fn();
    const mockRequestResponse = {
      on: jest.fn().mockImplementationOnce((event, cb) => {
        cb({statusCode: 400});
      })
    };
    mockRequest.mockReturnValueOnce(mockRequestResponse);

    webpackDevServerProxyHandler(req, res, mockNext);
    expect(mockRequest).toHaveBeenCalledWith('http://localhost:8000/assets/bundle.js');
    expect(mockNext).toHaveBeenCalled();
  });
});
