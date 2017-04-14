import React from 'react';
import { Router, Route, browserHistory } from 'react-router';
import {App} from './app.jsx';
import {URL} from './url.jsx';
import {Editor} from './editor.jsx';

export function Root(props) {
  return (<Router history={browserHistory}>
    <Route component={App}>
      <Route
        path="/"
        component={URL}
        getApiViolations={props.RestService.getApiViolationsByURL.bind(props.RestService)}
        Storage={props.Storage}
      />
      <Route
        path="/editor"
        component={Editor}
        getApiViolations={props.RestService.getApiViolationsBySchema.bind(props.RestService)}
        Storage={props.Storage}
      />
    </Route>
  </Router>)
}
