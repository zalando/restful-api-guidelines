import 'whatwg-fetch'; // fetch polyfill
import React from 'react';
import {render} from 'react-dom';
import firewall from './services/oauth-firewall.js';
import RestService from './services/rest.js'
import App from './components/app.jsx';

firewall().then(() => {
  render(
    <App RestService={RestService} />,
    document.getElementById('app')
  );
});

