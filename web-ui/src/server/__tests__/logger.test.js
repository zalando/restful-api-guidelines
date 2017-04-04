'use strict';

describe('server.logger', () => {
  const logger = require('../logger');

  test('should export a logger', () => {
    expect(logger).toBeDefined();
  });

  test('should have a log level', () => {
    expect(logger.level).toBeDefined();
  });
});
