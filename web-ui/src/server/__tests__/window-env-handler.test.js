'use strict';


describe('server.window-env-handler', () => {

  const envHandler = require('../window-env-handler');
  let mockWrite, res, req;

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
      envHandler({
        windowEnv: { foo: 'bar' }
      })(req, res);
    });

    test('should send the response', () => {
      expect(mockWrite).toHaveBeenCalledWith('window.env = {"foo":"bar"}');
    });
  });
});
