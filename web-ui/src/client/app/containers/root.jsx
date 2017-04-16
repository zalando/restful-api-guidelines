import React from 'react';
import { Router, Route, browserHistory, Redirect } from 'react-router';
import {App} from './app.jsx';
import {Login} from './login.jsx';
import {ViolationsTab} from './violations.jsx';
import {URL} from './url.jsx';
import {Editor} from './editor.jsx';

export function Root (props) {
  const {OAUTH_ENABLED} = props.env;
  return (<Router history={browserHistory}>
    <Route
      component={App}
      login={props.login}
      logout={props.logout}
      showUserInfo={OAUTH_ENABLED === true}
      user={props.user}>

      { OAUTH_ENABLED === true ?
        <Route
          path="/login"
          component={Login}
          user={props.user}
          login={props.login} /> : null
      }

      <Route
        component={ViolationsTab}
        onEnter={props.onEnterRequireLogin}>
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
      <Redirect from='*' to='/' /> { /* redirect not found to root */ }
    </Route>
  </Router>);
}
