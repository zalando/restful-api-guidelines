export class HeadersMock {
  constructor () {
    this._headers = {};
  }

  append (key, value) {
    this._headers[key] = value;
    return this;
  }

  delete (key) {
    delete this._headers[key];
  }

  get (key) {
    return this._headers[key];
  }

  has (key) {
    return typeof this._headers[key] !== 'undefined';
  }
}

export class RequestMock {
  constructor () {
    this.headers = new HeadersMock();
  }
}
