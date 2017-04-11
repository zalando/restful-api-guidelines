const mockRun = jest.fn();
jest.mock('../run', () => ({ run:  mockRun}));
jest.mock('../index.css', () => ({ run:  mockRun}));
require('../index');

describe('index', () => {
  test('should execute run function', () => {
    expect(mockRun).toHaveBeenCalled();
  });
});

