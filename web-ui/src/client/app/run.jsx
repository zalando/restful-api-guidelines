import React from 'react';
import {render} from 'react-dom';
import firewall from './services/oauth-firewall.js';
import {Storage} from './services/storage.js';
import {RestService} from './services/rest.js';
import {Root} from './containers/root.jsx';

export function run () {
	return firewall().then(() => {
	  return render(<Root
      RestService={RestService}
      Storage={Storage}
    />
    , document.getElementById('app'));
	});
}
