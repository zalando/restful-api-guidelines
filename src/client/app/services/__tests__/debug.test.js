/* global global */
/* eslint-disable no-console */
const {debug} = require('../debug');

describe('debug', () => {

  console.log = jest.genMockFn();

  afterEach(() => {
    delete global.window;
  });

  describe('don\'t log', () => {

    test('log if window is not defined', () => {
      debug();
      expect(console.log).not.toHaveBeenCalled();
    });

    test('if window.env is not defined', () => {
      global.window = {};
      debug();
      expect(console.log).not.toHaveBeenCalled();
    });

    test('don\'t log if window.env.DEBUG is != true', () => {
      global.window = { env: { DEBUG: false } };
      debug();
      expect(console.log).not.toHaveBeenCalled();
    });
  });

  describe('log', () => {
    beforeEach(() => {
      global.window = { env: { DEBUG: true } };
      delete console.debug;
    });

    test('if window.env.DEBUG is == true', () => {
      debug();
      expect(console.log).toHaveBeenCalled();
    });

    test('using console.debug, if window.env.DEBUG is == true and console.debug is a function', () => {
      console.debug = jest.genMockFn();
      debug();
      expect(console.debug).toHaveBeenCalled();
    });
  });

});
