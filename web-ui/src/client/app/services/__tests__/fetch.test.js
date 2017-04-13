import fetch from '../fetch.js';

describe('fetch', () => {
  let mockFetch;
  beforeEach(() => {
    mockFetch = jest.fn();
    global.fetch = mockFetch;
  });

  afterEach(() => {
    delete global.fetch;
  });

  test('should handle success http response', () => {
    const url = 'https://www.zally.com';
    const response = {
      status: 200
    };

    mockFetch.mockReturnValueOnce(Promise.resolve(response));

    return fetch(url).then((res) => {
      expect(mockFetch).toHaveBeenCalledWith(url);
      expect(res).toBe(response);
    });
  });

  test('should handle http response with an error status code with a rejection', (done) => {
    const url = 'https://www.zally.com';
    const response = {
      status: 401,
      statusText: 'Not Authorized'
    };

    mockFetch.mockReturnValueOnce(Promise.resolve(response));

    fetch(url).catch((error) => {
      try {
        expect(mockFetch).toHaveBeenCalledWith(url);
        expect(error instanceof Error).toBe(true);
        expect(error.message).toBe(response.statusText);
        expect(error.status).toBe(response.status);
        done();
      }catch(e) {
        done.fail(e);
      }
    });
  });

  test('should handle http response with an error status code with a rejection and use response.status as message if response.statusText is "falsy"', (done) => {
    const url = 'https://www.zally.com';
    const response = {
      status: 401
    };

    mockFetch.mockReturnValueOnce(Promise.resolve(response));

    fetch(url).catch((error) => {
      try {
        expect(error.message).toBe('401');
        expect(error.status).toBe(response.status);
        done();
      }catch(e) {
        done.fail(e);
      }
    });
  });
});
