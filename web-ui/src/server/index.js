const express = require('express');
const path = require('path');
const app = express();


if(process.env.NODE_ENV === 'development') {
  const webpackDevMiddleware = require('webpack-dev-middleware');
  const webpack = require('webpack');
  const webpackConfig = require('../../webpack.config');
  var compiler = webpack(webpackConfig);

  app.use(webpackDevMiddleware(compiler, {
    publicPath: '/assets/',
    stats: { colors: true }
  }));
}

if(process.env.NODE_ENV !== 'development') {
  app.use('/assets/', express.static(path.resolve(__dirname, '../client/public/assets')));
}

app.get('/', function (req, res) {
  res.sendFile(path.resolve(__dirname, '../client/public/index.html'));
});

app.listen(3000, function () {
  console.log('Listening on port 3000!');
});

