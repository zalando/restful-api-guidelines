import React from 'react';

export function Login (props) {
  const {user} = props.route;
  return (
    <div style={{textAlign: 'center'}}>
      { !user.authenticated ?
        <p dataTestId="not-authenticated">
          You should <span className="dc-link" onClick={props.route.login}>login</span> to access Zally API Linter
          <br /><br />
          <button type="button" className="dc-btn dc-btn--primary" onClick={props.route.login}>LOGIN</button>
        </p> : <p dataTestId="authenticated">You are logged in!</p>
      }
    </div>);
}
