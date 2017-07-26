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

  clone () {
    const clone = new HeadersMock();
    Object.keys(this._headers, (key) => {
      clone.append(key, this._headers[key]);
    });
    return clone;
  }
}

export class RequestMock {
  constructor () {
    this.headers = new HeadersMock();
  }

  clone () {
    const clone = new RequestMock();
    clone.headers = this.headers.clone();
    return clone;
  }
}
