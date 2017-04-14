import React from 'react';
import {render} from 'react-dom';
import firewall from './services/oauth-firewall.js';
import {Storage} from './services/storage.js';
import {RestService} from './services/rest.js';
import {Root} from './containers/root.jsx';

export function run () {
	return firewall().then(() => {

    // clean the hash if contains the access token
    // e.g. http://myapp.com/#access_token=xxx
    if(firewall.hasAuthResponse()) {
      window.location.hash = '';
    }

    return render(<Root
      RestService={RestService}
      Storage={Storage}
    />
    , document.getElementById('app'));
	});
}
