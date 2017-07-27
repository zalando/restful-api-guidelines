import React from 'react';
import {useBasename} from 'history';
import {Router, Route, browserHistory, Redirect} from 'react-router';
import {App} from './app.jsx';
import {Login} from './login.jsx';
import {ViolationsTab} from './violations.jsx';
import {URL} from './url.jsx';
import {Editor} from './editor.jsx';
import {Rules} from './rules.jsx';



export function Root (props) {
  const {OAUTH_ENABLED} = props.env;

  const createRouterHistory = () => {
    return useBasename(() => browserHistory)({ basename: props.env.MOUNTPATH });
  };

  return (<Router history={createRouterHistory()}>
    <Route
      component={App}
      login={props.login}
      logout={props.logout}
      showUserInfo={OAUTH_ENABLED === true}
      env={props.env}
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
        <Route path="/rules"
               onEnter={(nextState, replace) => {
                 if (typeof nextState.location.query.is_active === 'undefined' ) {
                   replace({pathname: '/rules', query: {is_active: true}});
                 }
               }}
               component={Rules}
               getSupportedRules={props.RestService.getSupportedRules.bind(props.RestService)}
        />
      </Route>
      <Redirect from='*' to='/' /> { /* redirect not found to root */ }
    </Route>
  </Router>);
}
