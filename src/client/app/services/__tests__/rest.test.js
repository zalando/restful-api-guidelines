/* global global, jasmine */

import {RestService} from '../rest.js';
import {client} from '../http-client.js';

jest.mock('../http-client');

describe('RestService', () => {

  beforeEach(() => {
    global.window = {
      env: {
        ZALLY_API_URL : '/zally-api'
      }
    };
  });

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

  test('getSupportedRules call api and resolve with response body representing rules', () => {
    const mockRules = [];
    const rulesResponse = {
      json:() => mockRules
    };
    client.fetch.mockReturnValueOnce(Promise.resolve(rulesResponse));

    return RestService.getSupportedRules().then((rules) => {
      expect(rules).toBe(mockRules);
      expect(client.fetch).toHaveBeenCalledWith('/zally-api/supported-rules', {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
      });
    });
  });

  test('getSupportedRules call api with filter query and resolve with response body representing rules', () => {
    const mockRules = [];
    const rulesResponse = {
      json:() => mockRules
    };
    client.fetch.mockReturnValueOnce(Promise.resolve(rulesResponse));

    return RestService.getSupportedRules({'is_active': true}).then((rules) => {
      expect(rules).toBe(mockRules);
      expect(client.fetch).toHaveBeenCalledWith('/zally-api/supported-rules?is_active=true', {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
      });
    });
  });

  test('objectToQuery call should return correct query when pass one param', () => {
    expect(RestService.objectToParams({a: 1})).toEqual('?a=1');
  });

  test('objectToQuery call should return correct query when pass multiple params', () => {
    expect(RestService.objectToParams({a: 1, b:2, c:3})).toEqual('?a=1&b=2&c=3');
  });

  test('objectToQuery call should return empty query when do not pass params', () => {
    expect(RestService.objectToParams()).toEqual('');
  });
});
