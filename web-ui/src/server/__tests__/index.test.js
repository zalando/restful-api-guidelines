'use strict';

/* global jasmine */

describe('server.index', () => {
  let mockCreateHttpServer, mockListen, mockLoggerInfo;
  beforeEach(() => {
    jest.resetModules();
    mockListen = jest.fn();
    mockCreateHttpServer = jest.fn();
    mockLoggerInfo = jest.fn();
    jest.mock('../env', () => ({
      PORT: 8000,
      NODE_ENV: 'foo',
      SSL_ENABLED: true
    }));
    jest.mock('../app');
    jest.mock('../create-http-server', () => (mockCreateHttpServer));
    jest.mock('../logger', () => ({
      info: mockLoggerInfo
    }));

    mockCreateHttpServer.mockReturnValueOnce({
      listen: mockListen
    });

    require('../index');
  });

  test('start listening on http connections', () => {
    expect(mockCreateHttpServer).toHaveBeenCalled();
    expect(mockListen).toHaveBeenCalledWith(8000, jasmine.any(Function));
    const listenCb = mockListen.mock.calls[0][1];
    listenCb();
    expect(mockLoggerInfo).toHaveBeenCalled();
  });
});
