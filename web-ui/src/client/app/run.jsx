import React from 'react';
import {render} from 'react-dom';
import {createUser, logout} from './services/oauth-util.js';
import firewall from './services/oauth-firewall.js';
import {Storage} from './services/storage.js';
import {RestService} from './services/rest.js';
import {Root} from './containers/root.jsx';

export function run () {
	return firewall().then((response) => {

    console.log('firewall response', response);

    const user = createUser(response);

    // clean the hash if contains the access token
    // e.g. http://myapp.com/#access_token=xxx
    if (firewall.hasAuthResponse()) {
      window.location.hash = '';
    }

    return render(
      <Root
        user={user}
        logout={logout}
        RestService={RestService}
        Storage={Storage}
      />,
      document.getElementById('app')
    );
  });
}
