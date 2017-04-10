import React from 'react';
import {render} from 'react-dom';
import firewall from './services/oauth-firewall.js';
import RestService from './services/rest.js'
import App from './components/app.jsx';

export function run () {
	return firewall().then(() => {
	  return render(
	    <App RestService={RestService} />,
	    document.getElementById('app')
	  );
	});
}
