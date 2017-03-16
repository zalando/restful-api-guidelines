const dotenvParseVariables = require('dotenv-parse-variables');

const defaults = {
  PORT: 8442,
  SSL_ENABLED: false,
  SSL_CERT: "cert",
  OAUTH_ENABLED: false,
  OAUTH_AUTHORIZATION_URL: '',
  OAUTH_REDIRECT_URI: '',
  OAUTH_TOKENINFO_URL:'',
  OAUTH_CLIENT_ID:'',
  OAUTH_SCOPES:''
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
  'OAUTH_CLIENT_ID',
  'OAUTH_SCOPES'
];

const env = Object.assign(
  defaults,
  dotenvParseVariables.default(require('dotenv').config().parsed)
);

const publicEnv = publicEnvKeys.reduce((acc, key) => {
  acc[key] = env[key];
  return acc;
}, {});

env.public = () => { return publicEnv };

module.exports = env;
