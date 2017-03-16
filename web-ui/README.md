Zally WEB-UI
============

Zally Web User Interface provide a web friendly user interface to lint your api specs.
Proudly build with NodeJS and React.

## Requirements

* NodeJS >= 6.0.0


## Install and Run

```
cd web-ui
npm install --production
npm start
```

## Configuration

The web-ui rely on [dotenv](https://github.com/motdotla/dotenv) nodejs module to handle configuration.
Following the [The Twelve-Factor App](https://12factor.net/config) methodology.
So customization can be done by setting environment variables and or by providing a `.env` file in the root directory of your project.<br><br>

Below the full reference of "available" environment variables and their default values.

```
PORT=8442
SSL_ENABLED=false
SSL_CERT_DIR="cert"
OAUTH_ENABLED=false
OAUTH_CLIENT_ID=""
OAUTH_AUTHORIZATION_URL=""
OAUTH_REDIRECT_URI=""
OAUTH_TOKENINFO_URL=""
OAUTH_SCOPES=""
ZALLY_API_URL=""
``

* **PORT**: HTTP(S) Server port
* **SSL_ENABLED**: Start the server as HTTPS 
* **SSL_CERT_DIR**: Directory where ```server.key``` and ```server.crt``` files are located
* **OAUTH_ENABLED**: Enable client side Oauth2 implicit grant flow protection
* **OAUTH_CLIENT_ID**: Oauth2 client id assigned to your app
* **OAUTH_REDIRECT_URI**: The route that should handle the Oauth2 access token response
* **OAUTH_TOKENINFO_URL**: The url used to validate the access token and retrieve token informations
* **OAUTH_SCOPES**: Comma separated list of scopes that the user should grant to your app
* **ZALLY_API_URL**: The URL pointing to Zally Api Server

## Development

### Install

```
cd web-ui
npm install
```

### Run in development mode

```
npm run dev
```

> This task will start the server with webpack-dev-server-middleware and watch for changes.

