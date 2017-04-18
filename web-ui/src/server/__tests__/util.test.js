'use strict';

const {stringToBool} = require('../util');


describe('server.util', () => {
  describe('stringToBool', () => {
    test('transform "false" values into real booleans while keeping other values as they are', () => {
      expect(stringToBool({
        foo: 'false',
        bar: false
      })).toEqual({
        foo: false,
        bar: false
      });
    });

    test('transform "true" values into real booleans while keeping other values as they are', () => {
      expect(stringToBool({
        foo: 'true',
        bar: 'true',
        john: 'doe'
      })).toEqual({
        foo: true,
        bar: true,
        john: 'doe'
      });
    });
  });
});
