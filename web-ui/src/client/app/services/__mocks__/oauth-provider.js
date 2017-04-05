export default {
  hasAccessToken () { return !!this._accessToken; },
  getAccessToken () { return this._accessToken; },
  hasRefreshToken () { return !!this._refreshToken; },
  parse () { return this._response; }
};
