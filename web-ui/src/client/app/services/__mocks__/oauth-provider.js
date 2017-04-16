

export default {
  setAccessToken (accessToken) { this._accessToken = accessToken; },
  hasAccessToken () { return !!this._accessToken; },
  getAccessToken () { return this._accessToken; },
  getRefreshToken () { return this._refreshToken; },
  setRefreshToken (refreshToken) { this._refreshToken = refreshToken; },
  hasRefreshToken () { return !!this._refreshToken; },
  deleteTokens () { delete this._accessToken; delete this._refreshToken;},
  parse () { return this._response; },
  remember: jest.fn(),
  requestToken: jest.fn()
};
