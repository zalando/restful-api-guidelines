import React from 'react';
import {If} from '../components/util.jsx';

export function Login (props) {
  const {user} = props.route;
  return (
    <div style={{textAlign: 'center'}}>
      <If test={() => !user.authenticated}>
        <p>
          You should <span className="dc-link" onClick={props.route.login}>login</span> to access Zally API Linter <br /><br />
          <button type="button" className="dc-btn dc-btn--primary" onClick={props.route.login}>LOGIN</button>
        </p>
      </If>
      <If test={() => user.authenticated}>
        <p>You are logged in!</p>
      </If>
    </div>);
}
