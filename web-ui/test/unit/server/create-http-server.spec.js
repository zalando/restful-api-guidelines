describe('server.create-http-server', () => {
  let CreateHttpServer, server;
  const mockCreateHttpServer = jest.fn();
  const mockCreateHttpsServer = jest.fn();
  const mockReadFileSync = jest.fn();

  jest.mock('http', () => ({
          createServer: mockCreateHttpServer
      }));
  jest.mock('https', () => ({
          createServer: mockCreateHttpsServer
      }));
  jest.mock('fs', () => ({
          readFileSync: mockReadFileSync
      }));

  describe('when env.SSL_ENABLED is not defined', () => {
    beforeEach(() => {
      jest.mock('../../../src/server/env', () => ({
      }));

      CreateHttpServer = require('../../../src/server/create-http-server');
      server = new CreateHttpServer();
    });

    test('should create a http server', () => {
      expect(mockCreateHttpServer).toHaveBeenCalled();
    });
  });

  describe('when env.SSL_ENABLED is false', () => {
    beforeEach(() => {
      jest.resetModules();
      jest.mock('../../../src/server/env', () => ({
          'SSL_ENABLED': false
      }));

      CreateHttpServer = require('../../../src/server/create-http-server');
      server = new CreateHttpServer();
    });

    test('should create a http server', () => {
      expect(mockCreateHttpServer).toHaveBeenCalled();
    });
  });

  describe('when env.SSL_ENABLED is true', () => {
    beforeEach(() => {
      jest.resetModules();
      jest.mock('../../../src/server/env', () => ({
          'SSL_ENABLED': true
      }));

      CreateHttpServer = require('../../../src/server/create-http-server');
      server = new CreateHttpServer();
    });

    test('should read key and crt files', () => {
      expect(mockReadFileSync).toHaveBeenCalled();
    });

    test('should create a https server',() => {
      expect(mockCreateHttpsServer).toHaveBeenCalled();
    });
  });
});
