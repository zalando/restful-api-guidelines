'use strict';


describe('server.env-handler', () => {

  const envHandler = require('../env-handler');
  let mockWrite, res, req;

  jest.mock('../env', () => ({
    public: () => {
      return {'foo' : 'bar'};
    }
  }));

  test('should export a function', () => {
    expect(envHandler).toBeInstanceOf(Function);
  });

  describe('when invoking the function', () => {
    beforeEach(() => {
      mockWrite = jest.fn();
      res = {
        setHeader: () => {},
        write: mockWrite,
        end: () => {}
      };
      req = {};
      envHandler(req, res);
    });

    test('should send the response', () => {
      expect(mockWrite).toHaveBeenCalledWith('window.env = {"foo":"bar"}');
    });
  });
});
