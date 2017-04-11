const mockRun = jest.fn();
jest.mock('../run', () => ({ run:  mockRun}));
require('../index');

describe('index', () => {
  test('should execute run function', () => {
    expect(mockRun).toHaveBeenCalled();
  });
});

