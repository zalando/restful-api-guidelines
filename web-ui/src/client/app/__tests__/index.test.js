const mockRun = jest.fn();
jest.mock('../run', () => ({ run:  mockRun}));
jest.mock('../index.scss', () => {});
require('../index');

describe('index', () => {
  test('should execute run function', () => {
    expect(mockRun).toHaveBeenCalled();
  });
});

