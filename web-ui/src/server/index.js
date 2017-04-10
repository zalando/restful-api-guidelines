'use strict';

const env = require('./env');
const logger = require('./logger');
const createHttpServer = require('./create-http-server');
const app = require('./app');

/**
 * Start listening for connections
 */
createHttpServer(app).listen(env.PORT, () => {
  logger.info(`application server running at ${env.SSL_ENABLED ? 'https' : 'http'}://localhost:${env.PORT}, NODE_ENV=${env.NODE_ENV}`);
});

