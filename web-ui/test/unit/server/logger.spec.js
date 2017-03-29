describe('server.logger', () => {
  const logger = require('../../../src/server/logger');

  test('should export a logger', () => {
    expect(logger).toBeDefined();
  });

  test('should have a log level', () => {
    expect(logger.level).toBeDefined();
  });
});
