/* global jasmine */

import {RestService} from '../rest.js';
import {client} from '../http-client.js';

jest.mock('../http-client');

describe('RestService', () => {
  afterEach(() => {
    client.fetch.mockReset();
  });

  test('getApiViolations call api-violations api and resolve with response body representing violations', () => {
    const mockViolations = [];
    const violationsResponse = {
      json:() => mockViolations
    };
    client.fetch.mockReturnValueOnce(Promise.resolve(violationsResponse));

    return RestService.getApiViolations({
      foo: 'bar'
    }).then((violations) => {
      expect(violations).toBe(mockViolations);
    });
  });

  test('getApiViolations call api-violations api and reject with error json body', (done) => {
    const mockError = { detail: 'some error' };
    const errorResponse = {
      json:() => (Promise.resolve(mockError))
    };
    client.fetch.mockReturnValueOnce(Promise.reject(errorResponse));

    return RestService.getApiViolations({
      foo: 'bar'
    }).catch((error) => {
      try {
        expect(error.detail).toBe(mockError.detail);
        done();
      } catch (e) {
        done.fail(e);
      }
    });
  });

  test('getApiViolationsByURL send expected body', () => {
    const mockViolations = [];
    const violationsResponse = {
      json:() => mockViolations
    };
    const apiDefinitionURL = 'foo.json';
    client.fetch.mockReturnValueOnce(Promise.resolve(violationsResponse));

    return RestService.getApiViolationsByURL(apiDefinitionURL).then((violations) => {
      expect(violations).toBe(mockViolations);
      expect(client.fetch).toHaveBeenCalledWith(jasmine.any(String), {
        method: 'POST',
        headers: jasmine.any(Object),
        body: JSON.stringify({
          api_definition_url: apiDefinitionURL
        })
      });
    });
  });

  test('getApiViolationsBySchema send expected body', () => {
    const mockViolations = [];
    const violationsResponse = { json:() => mockViolations };
    const apiDefinitionSchema = { schema: '2.0' };
    client.fetch.mockReturnValueOnce(Promise.resolve(violationsResponse));

    return RestService.getApiViolationsBySchema(apiDefinitionSchema).then((violations) => {
      expect(violations).toBe(mockViolations);
      expect(client.fetch).toHaveBeenCalledWith(jasmine.any(String), {
        method: 'POST',
        headers: jasmine.any(Object),
        body: JSON.stringify({
          api_definition: apiDefinitionSchema
        })
      });
    });
  });


});
