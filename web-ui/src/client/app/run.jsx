import React from 'react';
import {render} from 'react-dom';
import { Router, Route, hashHistory } from 'react-router';
import firewall from './services/oauth-firewall.js';
import {Storage} from './services/storage.js';
import {RestService} from './services/rest.js';
import {App} from './containers/app.jsx';
import {URL} from './containers/url.jsx';
import {Editor} from './containers/editor.jsx';

const getApiViolationsByURL = RestService.getApiViolationsByURL.bind(RestService);
const getApiViolationsBySchema = RestService.getApiViolationsBySchema.bind(RestService);

export function run () {
	return firewall().then(() => {
	  return render((
      <Router history={hashHistory}>
        <Route component={App}>
          <Route
            path="/"
            component={URL}
            getApiViolations={getApiViolationsByURL}
            Storage={Storage}
          />
          <Route
            path="/editor"
            component={Editor}
            getApiViolations={getApiViolationsBySchema}
            Storage={Storage}
          />
        </Route>
      </Router>
    ), document.getElementById('app'));
	});
}
