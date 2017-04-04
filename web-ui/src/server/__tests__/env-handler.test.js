'use strict';

describe('server.env-handler', () => {
  let envHandler;
  const mockWrite = jest.fn();
  const res = {
    setHeader: () => {},
    write: mockWrite,
    end: () => {}
  };
  const req = {};
  const mockReadFile = jest.fn()
    .mockImplementationOnce((path, cb) => cb({message: 'Dummy file read error.'}, null))
    .mockImplementationOnce((path, cb) => cb(null, 'A non json data.'))
    .mockImplementationOnce((path, cb) => cb(null, '{"client_id": "id:123"}'));
  const mockJoin = jest.fn()
    .mockImplementationOnce(() => 'clientJsonFilePathWithError')
    .mockImplementationOnce(() => 'clientJsonFilePath')
    .mockImplementationOnce(() => 'clientJsonFilePath');
  const mockDebug = jest.fn();
  const mockError = jest.fn();

  jest.mock('fs', () => ({
    readFile: mockReadFile
  }));

  jest.mock('path', () => ({
    join: mockJoin
  }));

  jest.mock('../logger', () => ({
    debug: mockDebug,
    error: mockError
  }));

  envHandler = require('../env-handler');

  test('should export a function', () => {
    expect(envHandler).toBeInstanceOf(Function);
  });

  describe('when invoking the function without ZALANDO_OAUTH && CREDENTIALS_DIR', () => {
    beforeEach(() => {
      jest.resetModules();
      jest.mock('../env', () => ({
        public: () => {
          return {'foo' : 'bar'};
        }
      }));

      envHandler = require('../env-handler');
      envHandler(req, res);
    });

    test('should send the response', () => {
      expect(mockWrite).toHaveBeenCalledWith('window.env = {"foo":"bar"}');
    });

  });

  describe('when invoking the function with ZALANDO_OAUTH && CREDENTIALS_DIR and cannot read clientJSON', () => {
    beforeEach(() => {
      jest.resetModules();
      jest.mock('../env', () => ({
        public: () => {
          return {'foo' : 'bar'};
        },
        'ZALANDO_OAUTH': 'zalandoOauth',
        'CREDENTIALS_DIR': 'credentialsDir'
      }));

      envHandler = require('../env-handler');
      envHandler(req, res);
    });

    test('should create clientJSONPath', () => {
      expect(mockJoin).toHaveBeenCalledWith('credentialsDir', 'client.json');
    });

    test('should log a debug message', () => {
      expect(mockDebug).toHaveBeenCalledWith('Reading clientJsonFilePathWithError');
    });

    test('should read file', () => {
      expect(mockReadFile.mock.calls[0][0]).toBe('clientJsonFilePathWithError');
    });

    test('should log error', () => {
      expect(mockError).toHaveBeenCalledWith('Cannot read client.json!');
    });

    test('should send a response', () => {
      expect(mockWrite).toHaveBeenCalledWith('window.env = {"foo":"bar"}');
    });
  });

  describe('when invoking the function with ZALANDO_OAUTH && CREDENTIALS_DIR and cannot parse clientJson file data', () => {
    beforeEach(() => {
      jest.resetModules();
      jest.mock('../env', () => ({
        public: () => {
          return {'foo' : 'bar'};
        },
        'ZALANDO_OAUTH': 'zalandoOauth',
        'CREDENTIALS_DIR': 'credentialsDir'
      }));

      envHandler = require('../env-handler');
      envHandler(req, res);
    });

    test('should create clientJSONPath', () => {
      expect(mockJoin).toHaveBeenCalledWith('credentialsDir', 'client.json');
    });

    test('should log a debug message', () => {
      expect(mockDebug).toHaveBeenCalledWith('Reading clientJsonFilePath');
    });

    test('should read file', () => {
      expect(mockReadFile.mock.calls[1][0]).toBe('clientJsonFilePath');
    });

    test('should log json parse error', () => {
      expect(mockError).toHaveBeenCalledWith('Cannot parse client.json');
    });

    test('should send a response', () => {
      expect(mockWrite).toHaveBeenCalledWith('window.env = {"foo":"bar"}');
    });

  });

  describe('when invoking the function with ZALANDO_OAUTH && CREDENTIALS_DIR and can successfully read clientJson file', () => {
    beforeEach(() => {
      jest.resetModules();
      jest.mock('../env', () => ({
        public: () => {
          return {'foo' : 'bar'};
        },
        'ZALANDO_OAUTH': 'zalandoOauth',
        'CREDENTIALS_DIR': 'credentialsDir'
      }));

      envHandler = require('../env-handler');
      envHandler(req, res);
    });

    test('should create clientJSONPath', () => {
      expect(mockJoin).toHaveBeenCalledWith('credentialsDir', 'client.json');
    });

    test('should log a debug message', () => {
      expect(mockDebug).toHaveBeenCalledWith('Reading clientJsonFilePath');
    });

    test('should read file', () => {
      expect(mockReadFile.mock.calls[2][0]).toBe('clientJsonFilePath');
    });

    test('should send a response', () => {
      expect(mockWrite).toHaveBeenCalledWith('window.env = {"foo":"bar","OAUTH_CLIENT_ID":"id:123"}');
    });
  });
});
