
describe('server.zally-api-handler', () => {
  let zallyApiHandler;
  const mockDebug = jest.fn();
  const mockError = jest.fn();
  const apiDefinitionURL = 'https://example.com/api-definition';
  const req = {
    url: "/zally-api/path-to-api",
    body: {
      api_definition: apiDefinitionURL
    },
    headers: {
      authorization: ''
    }
  };
  const mockJson = jest.fn();
  const res = {
    json: mockJson,
    status: () => {}
  };
  const mockFetch = jest.fn()
    .mockImplementationOnce(() => {
      throw { message: 'Test Exception', status: 500}
    })
    .mockImplementation(() => ({
      text: () => '{"foo": "bar"}',
      json: () => ({ "baz": "qux"})
    }));

  jest.mock('../../../src/server/env', () => ({
    "ZALLY_API_URL": "https://example.com"
  }));

  jest.mock("../../../src/server/logger", () => ({
    "error": mockError,
    "debug": mockDebug
  }));

  jest.mock("../../../src/server/fetch", () => mockFetch);

  zallyApiHandler = require('../../../src/server/zally-api-handler');

  test('should export a function', () => {
    expect(zallyApiHandler).toBeInstanceOf(Function);
  });

  describe('when invoking the function and encountering an error', () => {
    beforeAll(() => {
      return zallyApiHandler(req, res);
    });

    test('should log the error', () => {
      expect(mockError).toHaveBeenCalledWith({message: 'Test Exception', status: 500});
    });

    test('should respond with the status of error and error message', () => {
      expect(mockJson).toHaveBeenCalledWith({ type: 'about:blank', title: 'Test Exception', detail: 'Test Exception', status: 500 });
    })
  });

  describe('when invoking the function', () => {
    beforeAll(() => {
      mockFetch.mockClear();
      mockDebug.mockClear();
      mockJson.mockClear();

      return zallyApiHandler(req, res);
    });

    test('should fetch apiDefintest', () => {
      expect(mockFetch.mock.calls[0][0]).toBe(`${apiDefinitionURL}`);
    });

    test('should fetch violations', () => {
      expect(mockFetch.mock.calls[1][0]).toBe('https://example.com/path-to-api');
    });

    test('should respond with violations as json', () => {
      expect(mockJson).toHaveBeenCalledWith({"baz": "qux"});
    })
  });

});

