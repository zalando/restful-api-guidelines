'use strict';

/* global jasmine */

describe('webpack-dev-server-proxy', () => {
  let webpackDevServerProxy, mockWebpackDevServerProxyHandlerFactory, appMock;

  beforeEach(() => {
    jest.resetModules();
    mockWebpackDevServerProxyHandlerFactory = jest.fn();
    appMock = {
      use: jest.fn()
    };
    jest.mock('../webpack-dev-server-proxy-handler', () => mockWebpackDevServerProxyHandlerFactory);
    mockWebpackDevServerProxyHandlerFactory.mockReturnValueOnce(() => {});
    webpackDevServerProxy = require('../webpack-dev-server-proxy');
  });

  test('use default values if webpack config is not provided', () => {
    webpackDevServerProxy(appMock);
    expect(appMock.use).toHaveBeenCalledWith(webpackDevServerProxy.DEFAULTS.publicPath, jasmine.any(Function));
    expect(mockWebpackDevServerProxyHandlerFactory).toHaveBeenCalledWith(webpackDevServerProxy.DEFAULTS);
  });


  test('use merged configuration if webpack config is provided', () => {
    webpackDevServerProxy(appMock, {
      devServer: {
        publicPath: '/assets',
        https: true
      }
    });
    expect(appMock.use).toHaveBeenCalledWith('/assets', jasmine.any(Function));
    expect(mockWebpackDevServerProxyHandlerFactory).toHaveBeenCalledWith({
      publicPath: '/assets',
      protocol: 'https',
      port: webpackDevServerProxy.DEFAULTS.port,
      host: webpackDevServerProxy.DEFAULTS.host
    });
  });
});
