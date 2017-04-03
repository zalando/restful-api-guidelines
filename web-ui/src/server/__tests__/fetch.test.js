'use strict';

const mockSuccessResponse = {
  'status': 200,
  'statusText': 'Success'
};

const mockErrorResponse = {
  'status': 500,
  'statusText': 'Error'
};

jest.mock('node-fetch', () => jest.fn(() => {})
  .mockImplementationOnce(() => Promise.resolve(mockSuccessResponse))
  .mockImplementationOnce(() => Promise.resolve(mockErrorResponse))
);

describe('server.fetch', () => {
  const fetch = require('../fetch');
  let fetchPromise;

  test('it should export a function', () => {
    expect(fetch).toBeInstanceOf(Function);
  });

  describe('when invoking the function', () => {
    beforeEach(() => {
      return fetchPromise = fetch();
    });

    test('should response with data', () => {
      return fetchPromise.then((response) => {
        expect(response).toEqual(mockSuccessResponse);
      });
    });
  });

  describe('when invoking the function and getting error during fetch', () => {
    beforeEach(() => {
      return fetchPromise = fetch().catch(() => {});
    });

    test('should response with error', async () => {
      return fetchPromise.catch((error) => {
        expect(error.status).toEqual(500);
      });
    });
  });
});
