const env = require('./env');
const winston = require('winston');

winston.level = env.LOG_LEVEL;

module.exports = winston;
