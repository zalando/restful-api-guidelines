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

  test('pipe the request to the the expected url', () => {
    const req = {
      pipe: jest.fn(),
      url: '/bundle.js'
    };
    req.pipe.mockReturnValueOnce(req);

    webpackDevServerProxyHandler(req);
    expect(mockRequest).toHaveBeenCalledWith('http://localhost:8000/assets/bundle.js');
  });
});
