import {Provider} from 'oauth2-client-js';

const OAuthProvider = new Provider({
  id: 'zally',
  authorization_url: window.env.OAUTH_AUTHORIZATION_URL
});

export default OAuthProvider;
