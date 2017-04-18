'use strict';

const {stringToBool} = require('./util');
const dotenvParseVariables = require('dotenv-parse-variables').default;
const dotenv = require('dotenv').config();
const dotenvParsedVariables = dotenvParseVariables(dotenv.parsed || {});

const defaults = {
  PORT: 8442,
  LOG_LEVEL: 'debug',
  SSL_ENABLED: false,
  SSL_CERT: '',
  SSL_KEY: '',
  ZALLY_API_URL: '',
  OAUTH_ENABLED: false,
  OAUTH_AUTHORIZATION_URL: '',
  OAUTH_REDIRECT_URI: '',
  OAUTH_TOKENINFO_URL:'',
  OAUTH_REFRESH_TOKEN_URL: '',
  OAUTH_CLIENT_ID:'',
  OAUTH_SCOPES:'',
  OAUTH_USERNAME_PROPERTY: 'cn',
  DEV_PORT: 8441,
  DEV_SSL_ENABLED: false,
  DEBUG: false
};


/**
 * Those keys will be exposed to the client
 * @type {string[]}
 */
const publicEnvKeys = [
  'OAUTH_ENABLED',
  'OAUTH_CLIENT_ID',
  'OAUTH_AUTHORIZATION_URL',
  'OAUTH_REDIRECT_URI',
  'OAUTH_TOKENINFO_URL',
  'OAUTH_SCOPES',
  'OAUTH_USERNAME_PROPERTY',
  'DEBUG'
];

const env = stringToBool(Object.assign(
  defaults,
  process.env,
  dotenvParsedVariables
));

const publicEnv = publicEnvKeys.reduce((acc, key) => {
  acc[key] = env[key];
  return acc;
}, {});

env.public = () => { return publicEnv; };

module.exports = env;
