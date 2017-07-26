import {refreshToken, me} from '../oauth-util.js';
import fetch from '../fetch.js';
import {client} from '../http-client.js';

jest.mock('../fetch');
jest.mock('../http-client');


describe('refreshToken', () => {

  afterEach(() => {
    fetch.mockReset();
  });

  test('when success should resolve with response and body' , () => {
    const mockJson = jest.fn();
    const mockResponse = {
      json: mockJson
    };
    mockJson.mockReturnValueOnce(Promise.resolve('body'));
    fetch.mockReturnValueOnce(Promise.resolve(mockResponse));

    return refreshToken().then(({response, body}) => {
      expect(response).toEqual(mockResponse);
      expect(body).toEqual('body');
    });
  });

  test('when failure should reject with an error', (done) => {
    const error = new Error('test refreshToken error fails');
    fetch.mockReturnValueOnce(Promise.reject(error));
    refreshToken().catch((e) => {
      try {
        expect(e).toEqual(error);
        done();
      } catch (e) {
        done.fail(e);
      }
    });
  });
});

describe('me', () => {

  afterEach(() => {
    client.fetch.mockReset();
  });

  test('resolve with a "user" object', () => {
    const mockMe = {};
    client.fetch.mockReturnValueOnce(Promise.resolve({
      json: () => mockMe
    }));
    me().then((body) => {
      expect(body).toBeDefined();
      expect(body).toBe(mockMe);
    });
  });

  test('reject with an error if token is not valid', (done) => {
    const mockError = new Error('test "me" fails');
    client.fetch.mockReturnValueOnce(Promise.reject(mockError));
    me().catch((error) => {
      try {
        expect(error).toBeDefined();
        expect(error).toBe(mockError);
        done();
      } catch (e) {
        done.fail(e);
      }
    });
  });
});
