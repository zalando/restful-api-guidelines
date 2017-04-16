import React from 'react';
import {render} from 'react-dom';
import {createUser, logout, login, onEnterRequireLogin} from './services/oauth-util.js';
import firewall from './services/oauth-firewall.js';
import {Storage} from './services/storage.js';
import {RestService} from './services/rest.js';
import {Root} from './containers/root.jsx';

export function run () {
  return firewall().then((response) => {
    const user = createUser(response);
    user.authenticated = true;

    // clean the hash if contains the access token
    // e.g. http://myapp.com/#access_token=xxx
    if (firewall.hasAuthResponse()) {
      window.location.hash = '';
    }
    return { user };
  })
  .catch((error) => {
    console.error(`Firewall Error occurred: ${error.message}`); // eslint-disable-line no-console
    console.error(error); // eslint-disable-line no-console

    const user = createUser(error);
    user.authenticated = false;
    return Promise.resolve({user});
  })
  .then(({user}) => {
    return render(
      <Root
        user={user}
        logout={logout}
        login={login}
        onEnterRequireLogin={onEnterRequireLogin({user, loginLocation: '/login'})}
        env={window.env}
        RestService={RestService}
        Storage={Storage}
        />,
      document.getElementById('app')
    );
  });
}
