import React from 'react';
import {render} from 'react-dom';
import {logout, login, onEnterRequireLogin} from './services/oauth-util.js';
import firewall from './services/oauth-firewall.js';
import {Storage} from './services/storage.js';
import {RestService} from './services/rest.js';
import {Root} from './containers/root.jsx';

export function run () {
  return firewall()
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
