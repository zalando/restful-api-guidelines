[![dependencies Status](https://david-dm.org/zalando-incubator/zally-web-ui/status.svg)](https://david-dm.org/zalando-incubator/zally-web-ui)
[![codecov](https://codecov.io/gh/zalando-incubator/zally-web-ui/branch/master/graph/badge.svg)](https://codecov.io/gh/zalando-incubator/zally-web-ui)

Zally WEB-UI
============

Zally Web-UI project provides a web user interface to lint your api specs.


## Requirements

* NodeJS >= 7.6

## Install

```bash
npm install zally-web-ui --save
```

## Usage

### Basic

```js
const app = require('zally-web-ui')();

app.listen(3000, () => {
  console.log('zally-web-ui running at http://localhost:3000');
});
```

### Mount to an existing application

```js
const app = require('express')()
const zally = require('zally-web-ui')(/*options*/);

app.use('/api-linter', zally);
app.listen(3000, () => {
  console.log('server running at http://localhost:3000');
});
```

## Configuration options

When instantiating the app you can pass an `options` object to customize the behavior. 

```js
const options = { /* ..my options.. */}
const zally = require('zally-web-ui')(options);
```

### Options

* **windowEnv**: the windowEnv `object` contains all the values exposed to the client on `window.env` 
* **windowEnv.OAUTH_ENABLED** (default: `false`): enable OAuth or just Auth support on the client side (an http call will be fired on `/auth/me` endpoint to get the current logged in user, if any)  
* **windowEnv.ZALLY_API_URL** (default: `http://localhost:8080`): URL pointing to Zally REST API
* **windowEnv.DEBUG** (default: `true`): logs debugging message on the client side
* **logger** (default: `console`): custom logger
* **handlers**: the handlers `object` contains all route handlers used by zally-web-ui
* **handlers.assets**: handler that serve static assets
* **handlers.windowEnv**: handler that serve `/env.js` javascript file used to expose `windowEnv` values to the client on `window.env`
* **handlers.index**: handler that serve the single page application entrypoint on the wild card `*` to allow HTML5 History API working as expected

## Development

### Install, build and run in development mode

```
cd web-ui
npm install
npm run dev
```

> The `npm run dev` task starts the application server in development mode with **nodemon** and **webpack-dev-server** watching for changes.<br>
  The application server acts as a proxy to webpack-dev-server as the target.

### Run in production mode

```
npm run build
npm start
```

### Build optimized client javascript bundle

Build webpack bundle minified and source-map file(s).

```
npm run build
```

## Contributing

People interested contributing to the web-ui project can open issues and related pull requests. 

Before opening PRs, be sure the test are running by executing `npm test`.

### Contact

Feel free to contact one the [maintainers](MAINTAINERS)

### License

MIT license with an exception. See [license file](LICENSE).
