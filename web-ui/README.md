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
OAUTH_ENABLED=false
OAUTH_AUTHORIZATION_URL=""
OAUTH_REDIRECT_URI=""
OAUTH_TOKENINFO_URL=""
OAUTH_CLIENT_ID=""
OAUTH_SCOPES=""
``

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

