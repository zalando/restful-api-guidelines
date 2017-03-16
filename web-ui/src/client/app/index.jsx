import React from 'react';
import {render} from 'react-dom';
import {firewall} from './oauth';
import Form from './components/form.jsx';


class App extends React.Component {
  constructor(props) {
    super(props);
  }
  render () {
    return (
      <div className="dc-page">
        <div className="dc-container">
          <div className="dc-card">
            <h2 className="dc-h2">Zally Report</h2>
            <Form OAuthProvider={this.props.OAuthProvider}/>
          </div>
        </div>
      </div>
    );
  }
}

firewall({
  enabled: window.process.env.OAUTH_ENABLED,
  client_id: window.process.env.OAUTH_CLIENT_ID,
  authorization_url: window.process.env.OAUTH_AUTHORIZATION_URL,
  redirect_uri:  window.process.env.OAUTH_REDIRECT_URI,
  scopes: window.process.env.OAUTH_SCOPES
})
.then(({OAuthProvider}) => {
  // clean the hash
  window.location.hash = '';
  render(<App OAuthProvider={OAuthProvider} />, document.getElementById('app'));
});

